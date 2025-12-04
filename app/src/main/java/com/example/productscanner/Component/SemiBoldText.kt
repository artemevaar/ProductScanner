package com.example.productscanner.Component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun SemiBoldText(
    text: String,
    fontSize: Int = 14,
    color: Color = Color.Black,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        fontFamily = montsertFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = fontSize.sp,
        color = color,
        modifier = modifier,
        textAlign = textAlign
    )
}