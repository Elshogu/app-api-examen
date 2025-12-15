package com.cursokotlin.com.examen_api.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor // <-- NUEVA IMPORTACIÓN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://192.168.1.67:8000/api/"

    // 1. Crear el Interceptor de Logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // Nivel BODY: Muestra los encabezados, la URL y el cuerpo completo (incluyendo el multipart)
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. Crear el cliente OkHttp con el Interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // 3. Usar el cliente personalizado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}