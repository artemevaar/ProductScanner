package com.example.productscanner.Component

import android.content.Context
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.TorchState
import androidx.camera.core.UseCase
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.productscanner.R

@Composable
fun FlashlightButton(context: Context, lifecycleOwner: LifecycleOwner, modifier: Modifier) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }

    LaunchedEffect(Unit) {
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder().build()

            cameraProvider.unbindAll()

            val camera = cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
            cameraControl = camera.cameraControl
        }, ContextCompat.getMainExecutor(context))
    }
    Image(
        painterResource(id = R.drawable.flesh_vector),
        contentDescription = null,
        modifier = modifier
    )
}
