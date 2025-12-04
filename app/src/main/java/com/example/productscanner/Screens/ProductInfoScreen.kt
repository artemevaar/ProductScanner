package com.example.productscanner.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SemiBoldText
import com.example.productscanner.DataBase.getRestrictions
import com.example.productscanner.DataBase.saveProductRating
import com.example.productscanner.Firebase.FavoritesRepository
import com.example.productscanner.Firebase.saveProductRating_firestore
import com.example.productscanner.R
import com.example.productscanner.Restrictions.findRestrictions
import com.example.productscanner.ui.theme.montsertFontFamily
import com.example.scanpp.ComponentContentView.CardText
import com.example.scanpp.ComponentContentView.MultiSelectDropDown
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProductInfoScreen(
    productId: String,
    productName: String,
    ingredientsText: String,
    ingredientsExplanation: String?,
    imageUrl: String?,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val restrictions = listOf("Сахар", "Глютен", "Лактоза", "Орехи", "Яйца", "Рыба/морепродукты")
    val selectedRestrictions = remember { mutableStateListOf<String>() }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val savedRestrictions = getRestrictions(context) ?: ""
        selectedRestrictions.clear()
        if (savedRestrictions.isNotEmpty()) {
            selectedRestrictions.addAll(savedRestrictions.split(",").map { it.trim() })
        }
    }

    LaunchedEffect(userId, productId) {
        if (userId != null) {
            FavoritesRepository.checkIfFavorite(userId, productId) {
                isFavorite = it
            }
        }
    }


    val categories = listOf("Состав", "Разбор состава", "Личные ограничения")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(28.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Закрыть",
                    modifier = Modifier.clickable { onBackClick() }
                )

                Image(
                    painter = painterResource(id = if (isFavorite) R.drawable.star_select else R.drawable.star),
                    contentDescription = "Избранное",
                    modifier = Modifier
                        .clickable {
                            if (userId != null) {
                                FavoritesRepository.toggleFavorite(
                                    userId = userId,
                                    productId = productId,
                                    productName = productName,
                                    ingredientsText = ingredientsText,
                                    imageUrl = imageUrl,
                                    isCurrentlyFavorite = isFavorite
                                ) {
                                    isFavorite = it
                                }
                            }
                        }

                        .size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(19.dp))
            SemiBoldText(text = productName, fontSize = 24)

            Spacer(modifier = Modifier.weight(1f))
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 40.dp),
                color = Color.Transparent
            ) {
                AsyncImage(model = imageUrl, contentDescription = "Изображение продукта")
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
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

        when (selectedCategory) {
            "Состав" -> userId?.let { CompositionSection(it, productId, ingredientsText) }
            "Разбор состава" -> DetailsSection(ingredientsExplanation)
            "Личные ограничения" -> RestrictionsSection(
                selectedRestrictions,
                restrictions,
                ingredientsExplanation ?: ""
            )
        }
    }
}

@Composable
fun CompositionSection(userId: String, productId: String, ingredientsText: String) {
    val context = LocalContext.current
    val sharedPreferences =
        remember { context.getSharedPreferences("product_prefs", Context.MODE_PRIVATE) }

    var selectedButton by remember {
        mutableStateOf(
            sharedPreferences.getInt("selected_button_$productId", -1).takeIf { it != -1 })
    }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        CardText(text = ingredientsText, containerColor = Color.White)

        Spacer(modifier = Modifier.height(40.dp))
        MediumText(text = "Пожалуйста, оцените, насколько продукт с данным составом вам подходит")
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(11.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..5).forEach { i ->
                Btn(
                    text = i.toString(),
                    onClick = {
                        saveProductRating(
                            productId = productId,
                            ingredients = ingredientsText,
                            rating = i,
                            context = context
                        )

                        saveProductRating_firestore(
                            userId = userId,
                            productId = productId,
                            ingredients = ingredientsText,
                            rating = i
                        )

                        selectedButton = i
                        sharedPreferences.edit().putInt("selected_button_$productId", i).apply()
                    },
                    color = if (selectedButton == i) Color.Black else Color.White,
                    textColor = if (selectedButton == i) Color.White else Color.Black,
                    modifier = Modifier
                        .width(57.dp)
                        .height(50.dp)
                )
            }
        }
    }
}

@Composable
fun DetailsSection(ingredientsExplanation: String?) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        if (!ingredientsExplanation.isNullOrEmpty()) {
            CardText(text = ingredientsExplanation, containerColor = Color.White)
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun RestrictionsSection(
    selectedRestrictions: MutableList<String>,
    restrictions: List<String>,
    ingredientsExplanation: String?
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        MultiSelectDropDown(
            restrictions = restrictions,
            selectedRestrictions = selectedRestrictions,

            )
        Spacer(modifier = Modifier.height(14.dp))

        val foundRestrictions = findRestrictions(ingredientsExplanation, selectedRestrictions)

        val restrictionsText = if (!foundRestrictions.isNullOrEmpty()) {
            "Продукт содержит: " + foundRestrictions.joinToString(", ")
        } else {
            "Продукт не содержит выбранных ограничений"
        }

        CardText(text = restrictionsText, containerColor = Color.White)
        Spacer(modifier = Modifier.height(40.dp))
    }
}
