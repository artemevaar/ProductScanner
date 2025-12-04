package com.example.productscanner.Component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun DropDown(
    restrictions: List<String>,
    selectedRestrictions: MutableList<String>,
    selectedSortOrder: String,
    onSortSelected: (String) -> Unit,
    modifier: Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val selected_color = Color(ContextCompat.getColor(context, R.color.text_inp_grey))
    val grey_btn = Color(ContextCompat.getColor(context, R.color.grey_btn))
    Box(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = modifier
                .width(134.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(grey_btn),
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .width(134.dp)
                        .height(40.dp)
                        .clickable { isExpanded = !isExpanded }
                        .padding(start = 14.dp),
                ) {
                    Text(
                        text = "Сортировать",
                        fontFamily = montsertFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Image(painterResource(id = R.drawable.arrow_history), contentDescription = null)
                }

                if (isExpanded) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .padding(
                                horizontal = 9.dp,
                                vertical = 8.dp
                            )
                    ) {
                        restrictions.forEach { restriction ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSortSelected(restriction)
                                        isExpanded = false
                                    }
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = restriction,
                                    fontSize = 12.sp,
                                    fontFamily = montsertFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = if (selectedSortOrder == restriction) selected_color else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}