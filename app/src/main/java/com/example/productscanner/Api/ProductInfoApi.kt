package com.example.productscanner.Api

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun getProductInfo(
    context: Context,
    barcode: String,
    onProductInfoReceived: (String, String, String) -> Unit
) {
    val url = "https://world.openfoodfacts.org/api/v0/product/$barcode.json?lang=ru"
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val apiUrl = URL(url)
            val connection = apiUrl.openConnection() as HttpURLConnection
            val inputStream: InputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            inputStream.close()
            connection.disconnect()

            val jsonObject = JSONObject(response.toString())
            if (jsonObject.getString("status") == "1") {
                val product = jsonObject.getJSONObject("product")
                val productName = product.optString("product_name", "Название не найдено")
                val ingredients = product.optString("ingredients_text", "Состав не указан")
                val imageUrl = product.optString("image_url", null)

                withContext(Dispatchers.Main) {
                    onProductInfoReceived(productName, ingredients, imageUrl)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}