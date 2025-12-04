package com.example.productscanner
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.productscanner.Screens.SignUpScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun signUpScreen_elementsDisplayed_andButtonClickable() {
        composeTestRule.setContent {
            SignUpScreen(
                onBackClick = {},
                onNavigateToScanner = {},
                onNavigateToSignInScreen = {}
            )
        }
        composeTestRule.onNodeWithText("Регистрация")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Введите адрес почты")
            .assertIsDisplayed()
            .performTextInput("user@example.com")

        composeTestRule.onNodeWithText("Введите пароль")
            .assertIsDisplayed()
            .performTextInput("Password123!")

        composeTestRule.onNodeWithText("Повторите пароль")
            .assertIsDisplayed()
            .performTextInput("WrongPassword")

        composeTestRule.onNodeWithText("Зарегистрироваться")
            .assertIsDisplayed()
            .performClick() }
}
