package com.cursokotlin.com.examen_api.ui.screen

sealed class Screen {
    object List : Screen()
    object Detail : Screen()
    object Form : Screen()
    object Edit : Screen()
}
