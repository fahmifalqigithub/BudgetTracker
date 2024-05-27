package com.example.simplebudgettracker.ui.theme
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SettingsScreen(navController: NavController) {
    var backgroundColor by remember { mutableStateOf(Color.White) }
    var backgroundImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        backgroundImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge, // Gunakan gaya teks yang sesuai
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = { backgroundColor = Color.White },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("White Background")
        }
        Button(
            onClick = { backgroundColor = Color.Black },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Black Background")
        }
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Custom Background")
        }

        backgroundImageUri?.let { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Back")
        }
    }
}

