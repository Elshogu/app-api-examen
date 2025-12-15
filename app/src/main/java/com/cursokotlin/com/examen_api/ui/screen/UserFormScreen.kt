package com.cursokotlin.com.examen_api.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cursokotlin.com.examen_api.data.model.user.User
import com.cursokotlin.com.examen_api.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormScreen(
    viewModel: UserViewModel,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> selectedImageUri = uri }

    Column {
        TopAppBar(
            title = { Text("Nuevo contacto") },
            navigationIcon = {
                TextButton(onClick = { onCancel() }) { Text("Cancelar") }
            },
            actions = {
                TextButton(onClick = { /* Botón de editar NO hace nada */ }) {
                    Text("Editar")
                }
            }
        )

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de perfil
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = LocalIndication.current
                    ) { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Campos de texto
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = phoneError,
                modifier = Modifier.fillMaxWidth()
            )
            if (phoneError) {
                Text(
                    "El teléfono debe tener 10 dígitos",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text(
                    "Correo inválido",
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(24.dp))

            // Botón guardar
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    phoneError = phone.length != 10

                    if (!emailError && !phoneError) {
                        val user = User(
                            id = 0,
                            name = name,
                            phone = phone,
                            email = email,
                            image = null
                        )
                        viewModel.createUser(user, selectedImageUri, context)
                        onSave()
                    }
                }
            ) {
                Text("Guardar contacto")
            }
        }
    }
}
