package com.example.scanpp.ComponentContentView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun CardImg(
    label: String,
    imageRes: Int? = null,
    imageUrl: String? = null,
    isSelected: Boolean,
    isFavorite: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(166.dp)
            .height(152.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = if (isSelected) colorResource(id = R.color.black) else Color.Transparent,
                spotColor = if (isSelected) colorResource(id = R.color.black) else Color.Transparent
            )

            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) colorResource(id = R.color.text_inp_grey).copy(alpha = 0.3f) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onSelectionChange(!isSelected)
                onCardClick()
            }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = textColor,
                fontFamily = montsertFontFamily,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 10.dp, top = 10.dp)
                    .width(120.dp)
            )
            Image(
                painter = painterResource(id = if (isFavorite) R.drawable.star_select else R.drawable.star),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 11.dp, end = 15.dp)
                    .clickable { onFavoriteClick() }

            )
        }

        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        } else if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.Center)
            )
        }
    }
}