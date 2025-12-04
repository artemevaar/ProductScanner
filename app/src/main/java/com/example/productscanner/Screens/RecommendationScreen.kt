package com.example.productscanner.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.LoadingAnimation
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SearchField
import com.example.productscanner.Component.SemiBoldText
import com.example.productscanner.Firebase.FavoritesRepository
import com.example.productscanner.R
import com.example.productscanner.Recommendation.Product
import com.example.productscanner.Recommendation.fetchProductsFromFirestore
import com.example.productscanner.Recommendation.filterProductsByCategory
import com.example.productscanner.Recommendation.navigateToProductInfoWithExplanation
import com.example.productscanner.ui.theme.montsertFontFamily
import com.example.scanpp.ComponentContentView.CardImg
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun RecommendationScreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = Firebase.auth

    var selectedTab by remember { mutableStateOf("Рекомендации") }

    val categories: List<String> = listOf(
        "Все", "Молочные продукты", "Сладости", "Мясо, птица и рыба",
        "Хлеб и выпечка", "Крупы, макароны и бобовые,орехи", "Напитки", "Соусы, специи"
    )

    var selectedCategory by remember { mutableStateOf("Все") }
    var productList by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchProduct by remember { mutableStateOf("") }

    val filteredProductList by remember(productList, selectedCategory) {
        derivedStateOf {
            filterProductsByCategory(
                productList,
                selectedCategory
            ).filter { product -> product.name.contains(searchProduct, ignoreCase = true) }
        }
    }

    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            fetchProductsFromFirestore(userId, context, onDataUpdated = { updatedList ->
                productList = updatedList
                isLoading = false
            }, onError = { exception ->
                Log.e("MyTagScan", "Ошибка при загрузке продуктов: ${exception.message}")
                isLoading = false
            })
        } else {
            Log.d("MyTagScan", "Нет пользователя")
            isLoading = false
        }
    }


    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.backround))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))

            MediumText(
                text = "Вам понравится",
                fontSize = 24,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            SearchField(
                label = "Найдите продукт...",
                value = searchProduct,
                onValueChange = { searchProduct = it },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(categories) { category ->
                    val isSelected = category == selectedCategory
                    Button(
                        onClick = { selectedCategory = category },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color.Black else colorResource(id = R.color.grey_btn),
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontFamily = montsertFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (isLoading) {
                MediumText(
                    text = "Загрузка данных...",
                    fontSize = 16,
                    modifier = Modifier.padding(top = 200.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                LoadingAnimation()
            } else {
                if (productList.isEmpty()) {
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
                            text = "Оценивайте отсканированные продукты и мы подберем рекомендации по вашим предпочтениям",
                            fontSize = 12,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(323.dp)
                        )
                    }
                } else {
                    if (filteredProductList.isEmpty()) {
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
                                text = "Продуктов в данной категории нет",
                                fontSize = 12,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(323.dp)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1000.dp)
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            items(filteredProductList.chunked(2)) { rowItems ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    rowItems.forEach { product ->
                                        CardImg(
                                            label = product.name,
                                            imageUrl = product.imageUrl,
                                            isSelected = false,
                                            isFavorite = product.isFavorite,
                                            onSelectionChange = { isSelected ->
                                            },
                                            backgroundColor = Color.White,
                                            modifier = Modifier
                                                .width(166.dp)
                                                .height(200.dp),
                                            onCardClick = {

                                                navigateToProductInfoWithExplanation(
                                                    context = context,
                                                    navController = navController,
                                                    scannedCode = product.barcode,
                                                    productName = product.name,
                                                    ingredients = product.ingredients,
                                                    productImageUrl = product.imageUrl
                                                )
                                            },
                                            onFavoriteClick = {
                                                val userId = auth.currentUser?.uid ?: return@CardImg

                                                FavoritesRepository.toggleFavorite(
                                                    userId = userId,
                                                    productId = product.barcode,
                                                    productName = product.name,
                                                    ingredientsText = product.ingredients,
                                                    imageUrl = product.imageUrl,
                                                    isCurrentlyFavorite = product.isFavorite
                                                ) { isNowFavorite ->
                                                    productList = productList.map {
                                                        if (it.barcode == product.barcode) {
                                                            it.copy(isFavorite = isNowFavorite)
                                                        } else {
                                                            it
                                                        }
                                                    }
                                                }
                                            }

                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


        BottomNavigationBar(
            navController = navController,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
        )
    }
}
