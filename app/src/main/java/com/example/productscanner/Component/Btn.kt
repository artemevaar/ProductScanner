package com.example.productscanner.Component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun Btn(
    text: String, onClick: () -> Unit,
    color: Color = colorResource(id = R.color.white),
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    fontSize: Int = 14,
    textAlign: TextAlign = TextAlign.Center,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(14.dp), contentPadding = contentPadding
    ) {
        Text(
            text = text, textAlign = textAlign,
            color = textColor, fontSize = fontSize.sp,
            fontFamily = montsertFontFamily,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}