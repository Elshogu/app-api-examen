package com.cursokotlin.com.examen_api

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cursokotlin.com.examen_api.ui.screen.UserDetailScreen
import com.cursokotlin.com.examen_api.ui.screen.UserFormScreen
import com.cursokotlin.com.examen_api.ui.screen.UserListScreen
import com.cursokotlin.com.examen_api.ui.theme.ExamenapiTheme
import com.cursokotlin.com.examen_api.viewmodel.UserViewModel
import com.cursokotlin.com.examen_api.data.model.user.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamenapiTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "users"
    ) {

        // LISTADO DE USUARIOS
        composable("users") {
            UserListScreen(
                viewModel = userViewModel,
                onUserClick = { user: User ->
                    userViewModel.selectUser(user)
                    navController.navigate("user_detail")
                },
                onAddClick = {
                    userViewModel.clearSelectedUser()
                    navController.navigate("user_form")
                }
            )
        }

        // DETALLE DE USUARIO
        composable("user_detail") {
            UserDetailScreen(
                viewModel = userViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { /* No hace nada */ },
                onDeleted = { navController.popBackStack(route = "users", inclusive = false) }
            )
        }

        // NUEVO CONTACTO
        composable("user_form") {
            UserFormScreen(
                viewModel = userViewModel,
                onCancel = {
                    userViewModel.clearSelectedUser()
                    navController.popBackStack()
                },
                onSave = {
                    userViewModel.clearSelectedUser()
                    navController.popBackStack(route = "users", inclusive = false)
                }
            )
        }
    }
}
