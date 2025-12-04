package com.example.productscanner.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.CustomTextField
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Component.SemiBoldText
import com.example.productscanner.Firebase.resetPassword
import com.example.productscanner.Login.AuthViewModel
import com.example.productscanner.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun PasswordResetScreen(
    authViewModel: AuthViewModel = viewModel(),
    onWelcomePageClick: () -> Unit,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val auth = Firebase.auth

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.backround))
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(54.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_svg),
                modifier = Modifier
                    .clickable { onBackClick() }
                    .align(Alignment.CenterVertically),
                contentDescription = "Стрелка назад"
            )
            Spacer(modifier = Modifier.weight(1f))
            MediumText(
                text = "Сброс пароля",
                fontSize = 24,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 70.dp)
        ) {
            SemiBoldText("Почта", fontSize = 14)
            Spacer(modifier = Modifier.height(10.dp))
            CustomTextField(
                label = "Введите адрес почты",
                value = authViewModel.email,

                onValueChange = { authViewModel.updateEmail(it) },
                modifier = Modifier
            )
            Spacer(modifier = Modifier.weight(1f))

            Btn(
                text = "Продолжить",
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    val email = authViewModel.email
                    resetPassword(auth, email, context) { success, message ->
                        Toast.makeText(
                            context,
                            "Инструкция по сбросу пароля отправлена на $email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onWelcomePageClick()
                }
            )

        }
    }
}
