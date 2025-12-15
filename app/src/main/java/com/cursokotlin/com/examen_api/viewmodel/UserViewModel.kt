package com.cursokotlin.com.examen_api.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cursokotlin.com.examen_api.data.model.user.User
import com.cursokotlin.com.examen_api.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var selectedUser by mutableStateOf<User?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadUsers() {
        viewModelScope.launch {
            try {
                users = repository.getUsers()
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al cargar usuarios: ${e.message}"
            }
        }
    }

    fun selectUser(user: User) { selectedUser = user }
    fun clearSelectedUser() { selectedUser = null }

    fun createUser(user: User, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            try {
                repository.createUser(user, imageUri, context)
                loadUsers() // Recargar lista
                clearSelectedUser() // Cerrar formulario o limpiar selección
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al crear usuario: ${e.message}"
            }
        }
    }

    fun updateUser(user: User, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            try {
                repository.updateUser(user, imageUri, context)
                loadUsers()
                clearSelectedUser()
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al actualizar usuario: ${e.message}"
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteUser(id)
                loadUsers()
                clearSelectedUser()
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Error al eliminar usuario: ${e.message}"
            }
        }
    }
}