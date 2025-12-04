package com.example.productscanner.Screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.productscanner.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.productscanner.CameraAnalyzator.CameraPreview
import com.example.productscanner.CameraAnalyzator.DecodeQRCodeFromImage
import com.example.productscanner.CameraAnalyzator.PermissionHandler
import com.example.productscanner.Api.getProductInfo
import com.example.productscanner.Api.sendIngredientsExplanationToChatBot
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.FlashlightButton
import com.example.productscanner.Component.LoadingAnimation
import com.example.productscanner.Component.MediumText
import com.example.productscanner.DataBase.saveScannedProductToDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun scan_screen(navController: NavHostController) {

    var selectedTab by remember { mutableStateOf("Сканер") }
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    var cameraPermissionGranted by remember { mutableStateOf(false) }

    var isImageSelected by remember { mutableStateOf(false) }

    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    var scannedCode by remember { mutableStateOf<String?>(null) }
    var productName by remember { mutableStateOf<String?>(null) }
    var ingredients by remember { mutableStateOf<String?>(null) }
    var ingredientsExplanation by remember { mutableStateOf<String?>(null) }
    var productImageUrl by remember { mutableStateOf<String?>(null) }

    val processingTimeout = 60_000L
    var processingStarted by remember { mutableStateOf(false) }

    var isSelectedScan by remember { mutableStateOf(false) }

    val currentTime = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(
            Date()
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            capturedImageUri = it
            isImageSelected = true
            processingStarted = true
        }
    }

    LaunchedEffect(Unit) {
        isSelectedScan = true
    }

    LaunchedEffect(isImageSelected) {
        if (isImageSelected) {
            processingStarted = true
            delay(processingTimeout)

            if (scannedCode.isNullOrEmpty()) {
                Toast.makeText(context, "Ошибка обработки, попробуйте еще раз", Toast.LENGTH_SHORT)
                    .show()
                capturedImageUri = null
                isImageSelected = false
                processingStarted = false
            } else {
            }
        }
    }

    PermissionHandler(
        onPermissionGranted = { cameraPermissionGranted = true },
        onPermissionDenied = { cameraPermissionGranted = false }
    )


    if (capturedImageUri != null && scannedCode.isNullOrEmpty()) {
        DecodeQRCodeFromImage(uri = capturedImageUri!!) { qrCode ->
            scannedCode = qrCode

            processingStarted = false
            if (!scannedCode.isNullOrEmpty()) {
                getProductInfo(
                    context,
                    scannedCode!!
                ) { product_name, ingredients_text, imageUrl ->
                    productName = product_name
                    ingredients = ingredients_text
                    productImageUrl = imageUrl

                    if (!ingredients.isNullOrEmpty()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val explanation = sendIngredientsExplanationToChatBot(
                                context,
                                apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjgyYjhhOGYxLTMyOTYtNDczNi1iMzhiLTBmNGNiZWUzNzBhYSIsImlzRGV2ZWxvcGVyIjp0cnVlLCJpYXQiOjE3MzUwNTU1NzQsImV4cCI6MjA1MDYzMTU3NH0.3lCg6dx3r_AtzBIBBLmL5TfNgVbSN92x3YVup0-sfdw",
                                ingredientsText = ingredients!!
                            )
                            withContext(Dispatchers.Main) {
                                ingredientsExplanation = explanation
                                Log.i("MyTagScan", "Объяснение ингредиентов: $explanation")

                                val encodedProductName = Uri.encode(productName ?: "")
                                val encodedIngredients = Uri.encode(ingredients ?: "")
                                val encodedExplanation = Uri.encode(explanation)

                                navController.navigate(
                                    "product_info_screen/${scannedCode}/${encodedProductName}/${encodedIngredients}?ingredientsExplanation=${encodedExplanation}&scannedTime=${
                                        Uri.encode(currentTime)
                                    }&imageUrl=${Uri.encode(productImageUrl ?: "")}"
                                )

                                saveScannedProductToDatabase(
                                    context = context,
                                    productName = productName ?: "",
                                    ingredients = ingredients ?: "",
                                    scannedCode = scannedCode ?: "",
                                    ingredientsExplanation = explanation ?: "",
                                    productImageUrl = productImageUrl ?: ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            if (capturedImageUri != null) {
                isImageSelected = true
            } else {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onQrCodeScanned = { qrCode ->
                        scannedCode = qrCode
                        navController.navigate("product_info_screen/$qrCode")
                    }
                )
            }

            if (isImageSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.backround))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),

                    ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val overlayColor = Color.Black.copy(alpha = 0.8f)
                        val holeWidth = 344.dp.toPx()
                        val holeHeight = 180.dp.toPx()
                        val centerX = size.width / 2
                        val centerY = size.height / 2 - 95

                        drawRect(overlayColor)

                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = Offset(centerX - holeWidth / 2, centerY - holeHeight / 2),
                            size = Size(holeWidth, holeHeight),
                            blendMode = BlendMode.Clear,
                            cornerRadius = CornerRadius(x = 10.dp.toPx(), y = 10.dp.toPx())
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 54.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    MediumText(
                        text = "Сканер",
                        fontSize = 24,
                        color = if (isImageSelected) Color.Black else Color.White
                    )
                    FlashlightButton(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                    )
                }

                if (!isImageSelected) {
                    Spacer(modifier = Modifier.height(330.dp))
                    MediumText(
                        text = "Наведите камеру на код",
                        fontSize = 16,
                        modifier = Modifier.width(303.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    MediumText(
                        text = "Сканирование начнется автоматически",
                        fontSize = 14,
                        modifier = Modifier.width(303.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                } else {
                    Spacer(modifier = Modifier.height(125.dp))
                    Image(
                        painter = rememberImagePainter(capturedImageUri),
                        contentDescription = "Выбранное изображение",
                        modifier = Modifier.size(344.dp, 180.dp),
                        contentScale = ContentScale.Crop
                    )

                    MediumText(
                        text = "Обработка изображения",
                        fontSize = 16,
                        modifier = Modifier.width(303.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    LoadingAnimation()
                }
                Spacer(modifier = Modifier.weight(1f))
                Box(modifier = Modifier.padding(bottom = 40.dp)) {
                    Box(
                        modifier = Modifier
                            .height(60.dp)
                            .padding(horizontal = 24.dp)
                            .background(
                                colorResource(id = R.color.grey_btn),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row() {
                            Btn(
                                onClick = { isSelectedScan = true },
                                text = "Сканер",
                                textColor = if (isSelectedScan) Color.White else Color.Black,
                                color = if (isSelectedScan) Color.Black else Color.Transparent,
                                modifier = Modifier
                                    .width(164.dp)
                                    .height(48.dp)
                                    .padding(start = 8.dp)
                            )
                            Btn(
                                onClick = {
                                    isSelectedScan = false
                                    galleryLauncher.launch("image/*")
                                },
                                text = "Изображение",
                                textColor = if (isSelectedScan) Color.Black else Color.White,
                                color = if (isSelectedScan) Color.Transparent else Color.Black,
                                modifier = Modifier
                                    .width(164.dp)
                                    .height(48.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                    }
                }
            }
        }


        BottomNavigationBar(
            navController = navController,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
        )
    }
}

