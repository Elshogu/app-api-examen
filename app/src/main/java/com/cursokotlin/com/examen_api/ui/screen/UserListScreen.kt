package com.cursokotlin.com.examen_api.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cursokotlin.com.examen_api.ui.screen.components.UserCard
import com.cursokotlin.com.examen_api.data.model.user.User
import com.cursokotlin.com.examen_api.viewmodel.UserViewModel

@Composable
fun UserListScreen(
    viewModel: UserViewModel,
    onUserClick: (User) -> Unit,  // <-- Cambiado
    onAddClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
    }

    // Ordenar alfabéticamente y agrupar por la primera letra
    val groupedUsers = viewModel.users
        .sortedBy { it.name.lowercase() }
        .groupBy { it.name.first().uppercaseChar() }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->

        LazyColumn(modifier = Modifier.padding(padding)) {

            groupedUsers.forEach { (letter, usersForLetter) ->

                item {
                    Text(
                        text = letter.toString(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(usersForLetter) { user ->
                    UserCard(user = user) {
                        viewModel.selectUser(user)
                        onUserClick(user) // <-- pasamos el usuario
                    }
                }
            }
        }
    }
}
