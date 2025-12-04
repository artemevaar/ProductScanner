package com.example.productscanner.Component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun CardHistoryText(
    productName: String,
    scannedTime: String,
    containerColor: Color = Color.Gray,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val data_text_color = Color(ContextCompat.getColor(context, R.color.text_inp_grey))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                text = productName,
                fontFamily = montsertFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 24.dp,
                    bottom = 24.dp
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = scannedTime,
                fontFamily = montsertFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = data_text_color,
                modifier = Modifier.padding(
                    top = 24.dp,
                    bottom = 24.dp,
                    end = 24.dp
                )
            )
        }
    }
}
