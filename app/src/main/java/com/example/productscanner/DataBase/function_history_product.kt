package com.example.productscanner.DataBase

import android.content.Context
import com.example.productscanner.Firebase.getUserIdFromPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun deleteScannedProductToDatabase(context: Context) {

    val userId = getUserIdFromPreferences(context) ?: return
    val database = DatabaseProvider.getDatabase(context)

    CoroutineScope(Dispatchers.IO).launch {
        database.scannedProductDao().deleteProductsByUserId(userId)
    }
}

fun getUserScannedProducts(
    context: Context,
    onResult: (List<ScannedProduct>) -> Unit
) {
    val userId = getUserIdFromPreferences(context) ?: return
    val database = DatabaseProvider.getDatabase(context)

    CoroutineScope(Dispatchers.IO).launch {
        val products = database.scannedProductDao().getProductsByUserId(userId)
        onResult(products)
    }
}

fun saveScannedProductToDatabase(
    context: Context,
    productName: String,
    ingredients: String,
    ingredientsExplanation: String,
    scannedCode: String,
    productImageUrl: String
) {
    val userId = getUserIdFromPreferences(context) ?: return
    val database = DatabaseProvider.getDatabase(context)
    val currentTime = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())

    val product = ScannedProduct(
        userId = userId,
        productName = productName,
        ingredients = ingredients,
        ingredientsExplanation = ingredientsExplanation ?: "",
        scannedCode = scannedCode,
        scannedTime = currentTime,
        productImageUrl = productImageUrl

    )
    CoroutineScope(Dispatchers.IO).launch {
        database.scannedProductDao().insertProduct(product)
    }
}
