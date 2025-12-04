package com.example.productscanner.Component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.productscanner.ui.theme.manropeFontFamily
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun BtnText(
    text: String,
    onClick: () -> Unit,
    fontSize: Int = 20,
    color: Color = Color.Black,
    modifier: Modifier = Modifier

) {
    Text(
        text = text,
        fontFamily = montsertFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = fontSize.sp,
        color = color,
        modifier = modifier.clickable(onClick = onClick)
    )
}