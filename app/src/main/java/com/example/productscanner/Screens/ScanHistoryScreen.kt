package com.example.productscanner.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.CardHistoryText
import com.example.productscanner.Component.DropDown
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SearchField
import com.example.productscanner.Component.SemiBoldText
import com.example.productscanner.DataBase.ScannedProduct
import com.example.productscanner.DataBase.deleteScannedProductToDatabase
import com.example.productscanner.R

@Composable
fun ScanHistoryScreen(
    scannedProducts: List<ScannedProduct>,
    onProductInfoScreen: (String, String, String, String, String?) -> Unit,
    navController: NavHostController
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("История") }

    val restrictions = listOf("Сначала старые", "Сначала новые")
    val selectedRestrictions = remember { mutableStateListOf<String>() }
    var selectedSortOrder by remember { mutableStateOf("") }

    var searchProduct by remember { mutableStateOf("") }

    val sortedProducts = when (selectedSortOrder) {
        "Сначала старые" -> scannedProducts.sortedBy { it.scannedTime }
        "Сначала новые" -> scannedProducts.sortedByDescending { it.scannedTime }
        else -> scannedProducts
    }


    val filteredProducts = sortedProducts.filter {
        it.productName.contains(searchProduct, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.backround)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))

            MediumText(
                text = "История",
                fontSize = 24,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))
            SearchField(
                label = "Найдите продукт...",
                value = searchProduct, //отображает текущее значение переменной searchProduct
                onValueChange = {
                    searchProduct = it
                },//при вводе нового текста в поле он сохраняется в переменной searchProduct
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                DropDown(
                    restrictions = restrictions,
                    selectedRestrictions = selectedRestrictions,
                    selectedSortOrder = selectedSortOrder,
                    onSortSelected = { selectedSortOrder = it },
                    modifier = Modifier.width(134.dp),
                )
                Spacer(modifier = Modifier.width(10.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                    Btn(
                        text = "Очистить историю",
                        onClick = { deleteScannedProductToDatabase(context) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        fontSize = 12,
                        textAlign = TextAlign.Start,
                        color = colorResource(id = R.color.grey_btn)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                            .size(16.dp)
                    )
                }
            }

            if (scannedProducts.isEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SemiBoldText(
                        text = "Список пуст",
                        fontSize = 16,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MediumText(
                        text = "Сканируйте продукты и они отобразятся в истории",
                        fontSize = 12,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(top = 16.dp, bottom = 30.dp)
                ) {
                    if (filteredProducts.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier.padding(top = 200.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                SemiBoldText(
                                    text = "Продукт не найден",
                                    fontSize = 16,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                MediumText(
                                    text = "Попробуйте изменить запрос",
                                    fontSize = 12,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        items(filteredProducts) { product ->
                            CardHistoryText(
                                productName = product.productName,
                                scannedTime = product.scannedTime,
                                containerColor = Color.White,
                                modifier = Modifier.padding(bottom = 14.dp),
                                onClick = {
                                    onProductInfoScreen(
                                        product.scannedCode,
                                        product.productName,
                                        product.ingredients,
                                        product.ingredientsExplanation,
                                        product.productImageUrl
                                    )

                                }
                            )
                        }
                    }
                }
            }
        }

        BottomNavigationBar(
            navController = navController,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

