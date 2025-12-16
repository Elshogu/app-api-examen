package com.cursokotlin.com.examen_api.ui.screen.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cursokotlin.com.examen_api.data.model.user.User

@Composable
fun UserCard(
    user: User,
    onClick: () -> Unit
) {
    // LLAMAMOS A LA FUNCIÓN QUE ARREGLA LA URL CON TU IP 10.0.20.62
    val imageUrl = fixImageUrl(user.image?.url)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.email ?: "",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

// --- FUNCIÓN DE CORRECCIÓN DE URL (EXCLUSIVA PARA TU IP) ---
private fun fixImageUrl(url: String?): String? {
    if (url.isNullOrEmpty()) return null

    // TU IP EXACTA
    val myIp = "http://10.0.20.62:8000"

    // 1. Si Laravel devuelve URL completa (ej. http://localhost...), cambiamos a tu IP
    if (url.startsWith("http")) {
        return url.replace("localhost", "10.0.20.62")
            .replace("127.0.0.1", "10.0.20.62")
    }

    // 2. Si Laravel devuelve ruta relativa (ej. "users/foto.jpg")
    var cleanPath = if (url.startsWith("/")) url else "/$url"

    // Laravel suele guardar en 'storage/app/public', pero se accede por '/storage'
    // Si la ruta no tiene "/storage", se lo pegamos
    if (!cleanPath.startsWith("/storage")) {
        cleanPath = "/storage$cleanPath"
    }

    return "$myIp$cleanPath"
}