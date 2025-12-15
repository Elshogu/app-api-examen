package com.cursokotlin.com.examen_api.data.model.user

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val image: Image?
)

data class Image(
    val url: String
)

