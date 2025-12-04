package com.example.productscanner.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.LoadingAnimation
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SearchField
import com.example.productscanner.Component.SemiBoldText
import com.example.productscanner.DataBase.deleteRestrictions
import com.example.productscanner.DataBase.deleteScannedProductToDatabase
import com.example.productscanner.Firebase.FavProduct
import com.example.productscanner.Firebase.FavoritesRepository
import com.example.productscanner.Firebase.fetchProductsFromFavorites
import com.example.productscanner.R
import com.example.productscanner.Recommendation.Product
import com.example.productscanner.Recommendation.fetchProductsFromFirestore
import com.example.productscanner.Recommendation.filterProductsByCategory
import com.example.productscanner.Recommendation.navigateToProductInfoWithExplanation
import com.example.productscanner.ui.theme.montsertFontFamily
import com.example.scanpp.ComponentContentView.CardImg
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun FavoriteProduct(navController: NavHostController) {
    val context = LocalContext.current
    val auth = Firebase.auth

    var selectedTab by remember { mutableStateOf("Аккаунт") }
    var productList by remember { mutableStateOf<List<FavProduct>>(emptyList()) }
    var searchProduct by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        val userId = auth.currentUser?.uid ?: ""
        if (userId.isNotEmpty()) {
            fetchProductsFromFavorites(
                userId = userId,
                onSuccess = { updatedList ->
                    productList = updatedList
                    Log.d("MyTagScan", "LIST ${productList}")
                    isLoading = false
                },
                onFailure = { exception ->
                    isLoading = false
                    Log.e("MyTagScan", "Ошибка при загрузке избранного: ${exception.message}")
                }
            )
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
                text = "Избранное",
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
            Box(modifier = Modifier.align(Alignment.Start)) {
                Btn(
                    text = "Очистить все",
                    onClick = {
                        userId?.let { uid ->
                            FavoritesRepository.clearAllFavorites(uid) {
                                productList = productList.map { it.copy(isFavorite = false) }
                            }
                        }
                    },
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .width(166.dp)
                        .height(40.dp),
                    fontSize = 12,
                    color = colorResource(id = R.color.grey_btn)
                )
                Image(
                    painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 14.dp)
                        .width(16.dp)
                        .height(18.dp)
                )
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
                            text = "Сохраняйте продукты, чтобы ничего не забыть",
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
                        items(productList.chunked(2)) { chunk ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                chunk.forEach { product ->
                                    CardImg(
                                        label = product.productName,
                                        imageUrl = product.imageUrl,
                                        isSelected = false,
                                        isFavorite = product.isFavorite,
                                        onSelectionChange = { isSelected -> },
                                        backgroundColor = Color.White,
                                        modifier = Modifier
                                            .width(166.dp)
                                            .height(200.dp)
                                        //.align(Alignment.CenterHorizontally)
                                        ,
                                        onCardClick = {
                                            navigateToProductInfoWithExplanation(
                                                context = context,
                                                navController = navController,
                                                scannedCode = product.productId,
                                                productName = product.productName,
                                                ingredients = product.ingredientsText,
                                                productImageUrl = product.imageUrl
                                            )
                                        },
                                        onFavoriteClick = {
                                            val userId = auth.currentUser?.uid ?: return@CardImg

                                            FavoritesRepository.toggleFavorite(
                                                userId = userId,
                                                productId = product.productId,
                                                productName = product.productName,
                                                ingredientsText = product.ingredientsText,
                                                imageUrl = product.imageUrl,
                                                isCurrentlyFavorite = product.isFavorite
                                            ) { isNowFavorite ->
                                                productList = productList.map { p ->
                                                    if (p.productId == product.productId) {
                                                        p.copy(isFavorite = isNowFavorite)
                                                    } else {
                                                        p
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

        BottomNavigationBar(
            navController = navController,
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            modifier = Modifier
        )
    }
}
