package com.example.productscanner.CameraAnalyzator

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var permissionRequestPending by remember { mutableStateOf(false) }

    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        onPermissionGranted()
    } else if (!permissionRequestPending) {
        LaunchedEffect(Unit) {
            permissionRequestPending = true
            (context as ComponentActivity).requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                123
            )
        }
        onPermissionDenied()
    }
}