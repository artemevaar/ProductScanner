package com.example.scanpp.ComponentContentView

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productscanner.R
import com.example.productscanner.ui.theme.montsertFontFamily

@Composable
fun MultiSelectDropDown(
    restrictions: List<String>,
    selectedRestrictions: MutableList<String>
) {
    var isExpanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip( RoundedCornerShape(12.dp))
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Ограничения",
                        fontFamily = montsertFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.Black,
                    )
                }

                if (isExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 24.dp,
                                vertical = 16.dp
                            )
                    ) {
                        restrictions.forEach { restriction ->
                            val isSelected = selectedRestrictions.contains(restriction)


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (isSelected) {
                                            selectedRestrictions.remove(restriction)
                                        } else {
                                            selectedRestrictions.add(restriction)
                                        }
                                    }
                                    .padding(vertical = 8.dp)
                            )
                            {
                                Text(
                                    text = restriction,
                                    fontSize = 14.sp,
                                    fontFamily = montsertFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                Image(
                                    painter = painterResource(
                                        id = if (isSelected) R.drawable.selection_button_true else R.drawable.selection_button_false
                                    ),
                                    contentDescription = "selected item"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


