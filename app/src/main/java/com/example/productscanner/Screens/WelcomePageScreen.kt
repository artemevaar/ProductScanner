package com.example.productscanner.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.BtnText
import com.example.productscanner.Component.MediumText
import com.example.productscanner.R

@Composable
fun WelcomePageScreen(onSignInClick: () -> Unit, onSignUpClick: () -> Unit) {
    val context = LocalContext.current
    val logoText = context.getString(R.string.logo)

    Box(
        modifier = Modifier
            .background(colorResource(id = R.color.backround))
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))

            MediumText(text = "Добро пожаловать", fontSize = 24)
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.weight(1f))

            Btn(text = "Войти", onClick = onSignInClick)
            Spacer(modifier = Modifier.height(14.dp))
            BtnText(
                text = "Еще нет аккаунта?",
                onClick = onSignUpClick,
                fontSize = 13
            )

            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

