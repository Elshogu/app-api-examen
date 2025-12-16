package com.cursokotlin.com.examen_api.ui.screen

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cursokotlin.com.examen_api.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val user = viewModel.selectedUser ?: return
    var showDeleteDialog by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    TextButton(onClick = onEdit) {
                        Text("Editar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(16.dp))

            // CORRECCIÓN DE URL AQUÍ
            val imageUrl = fixImageUrlForDetails(user.image?.url)

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop,
                // ESTO TE AYUDARÁ A VER EL ERROR REAL EN EL LOG SI FALLA
                onError = { error ->
                    println("FALLO_COIL: Error cargando $imageUrl")
                    println("FALLO_COIL: Causa -> ${error.result.throwable.message}")
                }
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = user.name,
                fontSize = 22.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            // Iconos decorativos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val iconBackgroundModifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0))

                listOf(
                    Icons.AutoMirrored.Filled.Message,
                    Icons.Filled.Call,
                    Icons.Filled.VideoCall,
                    Icons.Filled.Email
                ).forEach { icon ->
                    Box(modifier = iconBackgroundModifier, contentAlignment = Alignment.Center) {
                        IconButton(onClick = {}) {
                            Icon(icon, contentDescription = null)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Tarjetas de info
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("móvil", color = Color.Gray)
                    Text(user.phone)
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("correo electrónico", color = Color.Gray)
                    Text(user.email)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botón eliminar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) { showDeleteDialog = true },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEAEA)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Eliminar Contacto", color = Color.Red, fontSize = 16.sp)
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar este contacto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteUser(user.id)
                        viewModel.clearSelectedUser()
                        showDeleteDialog = false
                        onDeleted()
                    }
                ) { Text("Eliminar", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

// --- FUNCIÓN PRIVADA PARA EL DETALLE CON TU IP ---
private fun fixImageUrlForDetails(url: String?): String? {
    if (url.isNullOrEmpty()) return null
    val myIp = "http://10.0.20.62:8000"

    if (url.startsWith("http")) {
        return url.replace("localhost", "10.0.20.62")
            .replace("127.0.0.1", "10.0.20.62")
    }

    var cleanPath = if (url.startsWith("/")) url else "/$url"
    if (!cleanPath.startsWith("/storage")) {
        cleanPath = "/storage$cleanPath"
    }

    return "$myIp$cleanPath"
}