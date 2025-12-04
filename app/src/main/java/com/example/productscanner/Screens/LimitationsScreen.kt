package com.example.productscanner.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SelectableOptionCard
import com.example.productscanner.DataBase.deleteRestrictions
import com.example.productscanner.DataBase.saveRestrictions
import com.example.productscanner.R

@Composable
fun LimitationsScreen(navController: NavHostController) {

    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("prefs", Context.MODE_PRIVATE) }

    val buttonData = listOf(
        "Сахар" to R.drawable.image_124,
        "Лактоза" to R.drawable.limit_lactose,
        "Рыба/морепродукты" to R.drawable.group_356,
        "Глютен" to R.drawable.image,
        "Яйца" to R.drawable.two_white_eggs,
        "Орехи" to R.drawable.image__1_,
    )

    var selectedStates by remember {
        mutableStateOf(
            buttonData.map { (label, _) -> sharedPreferences.getBoolean(label, false) }
        )
    }

    fun saveSelection(index: Int, isSelected: Boolean) {
        selectedStates = selectedStates.toMutableList().apply { set(index, isSelected) }
        sharedPreferences.edit().putBoolean(buttonData[index].first, isSelected).apply()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.backround))
    ) {
        Column(
            modifier = Modifier

                .padding(horizontal = 24.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))

            MediumText(
                text = "Мои ограничения",
                fontSize = 24,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            MediumText(
                text = "Выберите ограничения, которые будут применяться по умолчанию",
                fontSize = 14,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.align(Alignment.Start)) {
                Btn(
                    text = "Отменить выбор",
                    onClick = {
                        selectedStates = List(buttonData.size) { false }
                        sharedPreferences.edit().clear().apply()
                        deleteRestrictions(context)
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(top = 14.dp, bottom = 30.dp)
            ) {
                itemsIndexed(buttonData) { index, (label, imageRes) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(13.dp)
                    ) {

                        val globalIndex = index

                        SelectableOptionCard(
                            label = label,
                            imageRes = imageRes,
                            isSelected = selectedStates[globalIndex],
                            onSelectionChange = {
                                saveSelection(globalIndex, it)

                                val selectedRestrictions = buttonData
                                    .mapIndexedNotNull { i, (lbl, _) -> if (selectedStates[i]) lbl else null }

                                saveRestrictions(context, selectedRestrictions)

                            },
                            modifier = Modifier.weight(1f),
                            backgroundColor = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }


        BottomNavigationBar(
            navController = navController,
            selectedTab = "",
            onTabSelected = { },
            modifier = Modifier
        )
    }
}

