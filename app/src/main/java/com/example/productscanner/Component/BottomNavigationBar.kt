package com.example.productscanner.Component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.productscanner.R

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    onTabSelected: (String) -> Unit,
    selectedTab: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                15.dp, shape =
                RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
            )
            .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
    )
    {
        NavigationBar(
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            val items = listOf(
                NavigationItem(
                    "Рекомендации", R.drawable.icon_recommendation,
                    "recommendations"
                ),
                NavigationItem(
                    "Сканер", R.drawable.icon_scaner,
                    "scanner"
                ),
                NavigationItem(
                    "История", R.drawable.icon_history,
                    "history"
                ),
                NavigationItem(
                    "Аккаунт", R.drawable.icon_account,
                    "account"
                )
            )

            items.forEach { item ->
                val isSelected = selectedTab == item.title
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        onTabSelected(item.title)
                        navController.navigate(item.route)
                    },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = null,
                                tint = colorResource(id = R.color.black)
                            )
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .padding(top = 2.dp)
                                        .size(3.dp)
                                        .clip(CircleShape)
                                        .background(colorResource(id = R.color.black))
                                )
                            }
                        }
                    },


                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = colorResource(id = R.color.black),
                        unselectedIconColor = colorResource(id = R.color.black),
                        indicatorColor = Color.Transparent
                    ),
                    alwaysShowLabel = false
                )
            }
        }
    }
}

data class NavigationItem(val title: String, val iconResId: Int, val route: String)

