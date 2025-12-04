package com.example.productscanner.CameraAnalyzator

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

@Composable
fun DecodeQRCodeFromImage(uri: Uri, onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.let {
                    val bitmap = BitmapFactory.decodeStream(it)
                    val intArray = IntArray(bitmap.width * bitmap.height)
                    bitmap.getPixels(
                        intArray,
                        0,
                        bitmap.width,
                        0,
                        0,
                        bitmap.width,
                        bitmap.height
                    )

                    val source: LuminanceSource = RGBLuminanceSource(
                        bitmap.width,
                        bitmap.height,
                        intArray
                    )
                    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

                    val reader = MultiFormatReader()

                    val hints = mapOf(
                        DecodeHintType.POSSIBLE_FORMATS to listOf(
                            BarcodeFormat.QR_CODE,
                            BarcodeFormat.UPC_A,
                            BarcodeFormat.UPC_E,
                            BarcodeFormat.EAN_13,
                            BarcodeFormat.EAN_8,
                            BarcodeFormat.CODE_128,
                            BarcodeFormat.CODE_39
                        )
                    )
                    reader.setHints(hints)

                    val result = reader.decode(binaryBitmap)
                    withContext(Dispatchers.Main) {
                        onQrCodeScanned(result.text)
                    }
                } ?: Log.e("MyTagScan", "Ошибка: inputStream is null")

            } catch (e: Resources.NotFoundException) {
                Log.e("MyTagScan", "QR-код не найден на изображении", e)
            } catch (e: Exception) {
                Log.e("MyTagScan", "Ошибка сканирования QR-кода с фото", e)
            }
        }
    }
}