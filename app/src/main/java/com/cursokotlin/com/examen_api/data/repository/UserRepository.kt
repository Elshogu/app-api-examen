package com.cursokotlin.com.examen_api.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.cursokotlin.com.examen_api.data.model.user.User
import com.cursokotlin.com.examen_api.data.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*

class UserRepository {

    private val api = RetrofitInstance.api

    suspend fun getUsers() = api.getUsers()
    suspend fun getUser(id: Int) = api.getUser(id)

    // Función auxiliar para crear texto plano (reduce código repetido)
    private fun createPartFromString(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaType(), value)
    }

    // CORRECCIÓN: Comprimir imagen para asegurar que pese menos de 2MB (Límite Laravel)
    // y asegurar que siempre sea JPEG.
    private fun getFileFromUri(uri: Uri, context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // 1. Decodificar el stream a un Bitmap
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            // 2. Crear archivo temporal
            val tempFile = File(context.cacheDir, "upload_image_${UUID.randomUUID()}.jpg")
            val outputStream = FileOutputStream(tempFile)

            // 3. Comprimir a JPEG con calidad 80 (Reduce peso drásticamente sin perder calidad visual)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)

            outputStream.flush()
            outputStream.close()

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun prepareImagePart(uri: Uri, context: Context): MultipartBody.Part? {
        val file = getFileFromUri(uri, context) ?: return null

        // Al comprimir arriba, siempre resultará en un jpg
        val requestFile = file.asRequestBody("image/jpeg".toMediaType())

        // "image" debe coincidir con el nombre en tu Controller de Laravel ($request->file('image'))
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    suspend fun createUser(user: User, imageUri: Uri?, context: Context) {
        val namePart = createPartFromString(user.name)
        val emailPart = createPartFromString(user.email)
        val phonePart = createPartFromString(user.phone)

        val imagePart = imageUri?.let { prepareImagePart(it, context) }

        api.createUser(namePart, emailPart, phonePart, imagePart)
    }

    suspend fun updateUser(user: User, imageUri: Uri?, context: Context) {
        val namePart = createPartFromString(user.name)
        val emailPart = createPartFromString(user.email)
        val phonePart = createPartFromString(user.phone)

        // Campo necesario para "engañar" a Laravel y decirle que es un PUT
        val methodPart = createPartFromString("PUT")

        val imagePart = imageUri?.let { prepareImagePart(it, context) }

        // Enviamos el methodPart extra
        api.updateUser(user.id, namePart, emailPart, phonePart, methodPart, imagePart)
    }

    suspend fun deleteUser(id: Int) {
        val response = api.deleteUser(id)
        if (!response.isSuccessful) throw Exception("Error al eliminar usuario")
    }
}