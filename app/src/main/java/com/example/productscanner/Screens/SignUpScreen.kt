package com.example.productscanner.Screens

import android.util.Log
import android.widget.Toast
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
import com.example.productscanner.Firebase.signUp
import com.example.productscanner.Login.AuthViewModel
import com.example.productscanner.Login.isValidPassword
import com.example.productscanner.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
    onNavigateToScanner: () -> Unit,
    onNavigateToSignInScreen: () -> Unit
) {
    val context = LocalContext.current
    var passwordRepeat by remember { mutableStateOf("") }
    var passwordRepeatd_Visible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.backround))
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Регистрация",
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

            Spacer(modifier = Modifier.height(15.dp))

            MediumText("Пароль", fontSize = 14)
            Spacer(modifier = Modifier.height(10.dp))


            Box(modifier = Modifier.fillMaxWidth()) {
                CustomTextField(
                    label = "Повторите пароль",
                    value = passwordRepeat,
                    onValueChange = { passwordRepeat = it },
                    visualTransformation = if (passwordRepeatd_Visible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Image(
                    painter = painterResource(id = if (passwordRepeatd_Visible) R.drawable.eye_crossed_out else R.drawable.eye),
                    contentDescription = "Toggle Password Visibility",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 24.dp)
                        .size(17.dp)
                        .clickable { passwordRepeatd_Visible = !passwordRepeatd_Visible }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            BtnText(
                text = "Уже есть аккаунт?",
                onClick = onNavigateToSignInScreen, fontSize = 14,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.weight(1f))
            Btn(
                text = "Зарегистрироваться",
                modifier = Modifier.padding(bottom = 100.dp),
                onClick = {
                    if (!isValidPassword(authViewModel.password)) {
                        Toast.makeText(
                            context,
                            "Пароль должен быть не менее 8 символов, содержать цифру, заглавную букву и спецсимвол",
                            Toast.LENGTH_LONG
                        ).show()
                        return@Btn
                    }
                    if (authViewModel.password == passwordRepeat) {
                        signUp(
                            Firebase.auth,
                            authViewModel.email,
                            authViewModel.password,
                            context
                        ) { isSuccessful, result ->
                            if (isSuccessful && result != null) {
                                saveUserIdToPreferences(context, result)
                                onNavigateToScanner()
                                Log.d("MyTagScan", "User ID: $result")
                            } else {
                                Toast.makeText(
                                    context,
                                    result ?: "Неизвестная ошибка",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }
    }
}
