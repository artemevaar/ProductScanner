package com.example.productscanner.Screens

import  androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.productscanner.Component.BottomNavigationBar
import com.example.productscanner.Component.Btn
import com.example.productscanner.Component.MediumText
import com.example.productscanner.Firebase.deleteAccount
import com.example.productscanner.Firebase.signOut
import com.example.productscanner.Login.AuthViewModel
import com.example.productscanner.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AccountScreen(
    authViewModel: AuthViewModel,
    onWelcomePageClick: () -> Unit,
    onResetPasswordClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    navController: NavHostController
) {
    val auth = Firebase.auth
    val email = authViewModel.email
    val password = authViewModel.password

    var selectedTab by remember { mutableStateOf("Аккаунт") }

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.backround))
            .fillMaxSize(),

        ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(54.dp))
            MediumText(
                text = "Аккаунт",
                fontSize = 24,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            MediumText(text = email, fontSize = 15, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Image(
                    painterResource(id = R.drawable.vector_limitions),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )

                Btn(
                    text = "Мои ограничения",
                    onClick = { navController.navigate("limitations") },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp),

                    )

                Image(
                    painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Image(
                    painterResource(id = R.drawable.vector_star),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )
                Btn(
                    text = "Избранные",
                    onClick = { navController.navigate("favorite") },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp)
                )
                Image(
                    painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
            }
            Spacer(modifier = Modifier.height(42.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

                Image(
                    painterResource(id = R.drawable.vector_password),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )

                Btn(
                    text = "Изменить пароль",
                    onClick = { onResetPasswordClick() },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp)
                )
                Image(
                    painterResource(id = R.drawable.arrow), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))


            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

                Image(
                    painterResource(id = R.drawable.vector_exit),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )

                Btn(
                    text = "Выйти",
                    onClick = {
                        signOut(auth)
                        onWelcomePageClick()
                    },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp)
                )
                Image(
                    painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Image(
                    painterResource(id = R.drawable.vector_delete_account),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )
                Btn(
                    text = "Удалить аккаунт",
                    onClick = {
                        deleteAccount(auth, email, password)
                        onWelcomePageClick()
                    },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp)
                )
                Image(
                    painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
            }

            Spacer(modifier = Modifier.height(42.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                Image(
                    painterResource(id = R.drawable.vector_question),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 22.dp)
                        .zIndex(1f)
                )
                Btn(
                    text = "О приложении",
                    onClick = {
                        deleteAccount(auth, email, password)
                        onWelcomePageClick()
                    },
                    textAlign = TextAlign.Start,
                    contentPadding = PaddingValues(start = 59.dp)
                )
                Image(
                    painterResource(id = R.drawable.arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 30.dp)
                )
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
