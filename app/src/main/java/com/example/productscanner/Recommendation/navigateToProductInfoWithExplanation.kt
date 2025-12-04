package com.example.productscanner.Recommendation

import android.content.Context
import android.net.Uri
import androidx.navigation.NavController
import com.example.productscanner.Api.sendIngredientsExplanationToChatBot
import com.example.productscanner.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun navigateToProductInfoWithExplanation(
    navController: NavController,
    scannedCode: String,
    productName: String,
    ingredients: String,
    productImageUrl: String,
    context: Context,
) {
    kotlinx.coroutines.GlobalScope.launch(Dispatchers.Main) {
        val apiKey = context.getString(R.string.bot_api_key)
        val explanation = withContext(Dispatchers.IO) {
            sendIngredientsExplanationToChatBot(
                context = context,
                apiKey = apiKey,
                ingredientsText = ingredients
            )
        }

        val encodedProductName = Uri.encode(productName)
        val encodedIngredients = Uri.encode(ingredients)
        val encodedExplanation = Uri.encode(explanation)

        navController.navigate(
            "product_info_screen/${scannedCode}/${encodedProductName}/${encodedIngredients}?ingredientsExplanation=${encodedExplanation}&imageUrl=${
                Uri.encode(
                    productImageUrl
                )
            }"
        )
    }
}
