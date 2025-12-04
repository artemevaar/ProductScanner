package com.example.productscanner.Api

import android.content.Context
import android.util.Log
import com.example.productscanner.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

suspend fun sendIngredientsExplanationToChatBot(
    context: Context,
    apiKey: String,
    ingredientsText: String
): String {
    return withContext(Dispatchers.IO) {
        val prompt = context.getString(R.string.ingredients_prompt, ingredientsText)
        val url = URL("https://bothub.chat/api/v2/openai/v1/chat/completions")
        val requestBody = JSONObject().apply {
            put("model", "gpt-4.1-nano")
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", prompt)
                })
            })
            put("max_tokens", 900)
        }
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Authorization", "Bearer $apiKey")
            doOutput = true
        }
        try {
            connection.outputStream.use { outputStream ->
                val bytes = requestBody.toString().toByteArray(Charsets.UTF_8)
                outputStream.write(bytes)
            }
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = InputStreamReader(connection.inputStream).use { reader ->
                    BufferedReader(reader).use { bufferedReader ->
                        bufferedReader.readText()
                    }
                }
                val responseObject = JSONObject(response)
                val explanation = responseObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                explanation.trim()
            } else {
                val errorMessage = InputStreamReader(connection.errorStream).use { reader ->
                    BufferedReader(reader).use { bufferedReader ->
                        bufferedReader.readText()
                    }
                }
                Log.e(
                    "ChatBotRequest",
                    "Error response code: $responseCode, Error message: $errorMessage"
                )
                "Ошибка получения ответа от чат-бота"
            }
        } catch (e: Exception) {
            Log.e("ChatBotRequest", "Exception during request: ${e.message}", e)
            "Ошибка получения ответа от чат-бота"
        } finally {
            connection.disconnect()
        }
    }
}
