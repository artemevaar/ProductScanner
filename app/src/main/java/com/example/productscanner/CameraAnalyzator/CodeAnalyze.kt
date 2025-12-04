package com.example.productscanner.CameraAnalyzator

import android.graphics.ImageFormat
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer

class CodeAnalyze(
    private val onCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val barcodeFormats = listOf(
        BarcodeFormat.QR_CODE,
        BarcodeFormat.UPC_A,
        BarcodeFormat.UPC_E,
        BarcodeFormat.EAN_13,
        BarcodeFormat.EAN_8,
        BarcodeFormat.CODE_128,
        BarcodeFormat.CODE_39,
        BarcodeFormat.CODE_93,
        BarcodeFormat.ITF,
        BarcodeFormat.CODABAR,
        BarcodeFormat.DATA_MATRIX,
        BarcodeFormat.PDF_417
    )

    override fun analyze(image: ImageProxy) {
        if (image.format != ImageFormat.YUV_420_888) {
            image.close()
            return
        }

        val buffer = image.planes.first().buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        val width = image.width
        val height = image.height

        val source = PlanarYUVLuminanceSource(
            bytes,
            width,
            height,
            0,
            0,
            width,
            height,
            false
        )

        val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
        val reader = MultiFormatReader().apply {
            setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to barcodeFormats))
        }
        try {
            val result = reader.decode(binaryBitmap)
            onCodeScanned(result.text)
        } catch (e: Exception) {
            Log.d("MyTagScan", "Штрих-код не найден, ошибка: ${e.message}")
        } finally {
            image.close()
        }
    }
}