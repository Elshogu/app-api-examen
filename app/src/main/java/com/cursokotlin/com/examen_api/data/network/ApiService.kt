package com.cursokotlin.com.examen_api.data.network

import com.cursokotlin.com.examen_api.data.model.user.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @Multipart
    @POST("users")
    suspend fun createUser(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): User

    // CORRECCIÓN: Laravel no lee archivos en PUT directos.
    // Usamos POST e inyectamos "_method" = "PUT"
    @Multipart
    @POST("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("_method") method: RequestBody, // <-- Campo mágico para Laravel
        @Part image: MultipartBody.Part? = null
    ): User

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): retrofit2.Response<Unit>
}