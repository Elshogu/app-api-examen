package com.cursokotlin.com.examen_api.ui

object ImageUtils {
    // Si usas emulador, usa 10.0.2.2. Si es dispositivo físico, usa tu IP local (ej. 192.168.1.50)
    private const val BASE_URL = "http://10.0.10.62:8000"

    fun getFullImageUrl(relativePath: String?): String? {
        if (relativePath.isNullOrEmpty()) return null
        if (relativePath.startsWith("http")) return relativePath // Ya viene completa

        // Limpiamos barras duplicadas por si acaso
        val cleanPath = if (relativePath.startsWith("/")) relativePath else "/$relativePath"
        return "$BASE_URL$cleanPath"
    }
}