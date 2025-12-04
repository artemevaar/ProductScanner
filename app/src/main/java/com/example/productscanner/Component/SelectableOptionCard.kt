package com.example.productscanner.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun SelectableOptionCard(
    label: String,
    imageRes: Int,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
) {
    Box(
        modifier = modifier
            .width(345.dp)
            .height(74.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onSelectionChange(!isSelected) },

        contentAlignment = Alignment.Center
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 24.dp)
        ) {

            Box(
                modifier = Modifier.width(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                )
            }

            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight(),
                color = colorResource(id = R.color.grey_btn)
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = textColor,
                fontFamily = montsertFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = modifier.padding(start = 26.dp)
            )

            Image(
                painter = painterResource(if (isSelected) (R.drawable.selection_button_true) else (R.drawable.selection_button_false)),
                contentDescription = null
            )
        }
    }
}
