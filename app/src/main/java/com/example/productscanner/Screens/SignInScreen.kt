package com.example.productscanner.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.BtnText
import com.example.productscanner.Component.CustomTextField
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Firebase.saveUserIdToPreferences
import com.example.productscanner.Firebase.signIn
import com.example.productscanner.Login.AuthViewModel
import com.example.productscanner.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun SignInScreen(
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToScanner: () -> Unit,
    onNavigateToResetPassword: () -> Unit
) {
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

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
                text = "Вход",
                fontSize = 24,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 76.dp)
        ) {

            MediumText("Почта", fontSize = 14)

            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                label = "Введите адрес почты",
                value = authViewModel.email,
                onValueChange = { authViewModel.updateEmail(it) },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(15.dp))
            MediumText("Пароль", fontSize = 14)
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                CustomTextField(
                    label = "Введите пароль",
                    value = authViewModel.password,
                    onValueChange = { authViewModel.updatePassword(it) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Image(
                    painter = painterResource(id = if (passwordVisible) R.drawable.eye_crossed_out else R.drawable.eye),
                    contentDescription = "Toggle Password Visibility",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                        .size(17.dp)
                        .clickable { passwordVisible = !passwordVisible }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BtnText(
                text = "Забыли пароль?",
                onClick = onNavigateToResetPassword, fontSize = 14,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.weight(1f))

            Btn(
                text = "Войти",
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    signIn(
                        Firebase.auth,
                        authViewModel.email,
                        authViewModel.password,
                        context
                    ) { success, result ->
                        if (success && result != null) {
                            saveUserIdToPreferences(context, result)
                            authViewModel.getCurrentUserInfo()
                            onNavigateToScanner()
                            Log.d("MyTagScan", "User ID: $result")
                        } else {
                            errorMessage = result ?: "ошибка"
                        }
                    }
                }
            )
        }
    }
}
