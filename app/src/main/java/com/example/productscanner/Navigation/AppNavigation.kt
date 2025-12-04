package com.example.productscanner.Navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.productscanner.DataBase.ScannedProduct
import com.example.productscanner.DataBase.getUserScannedProducts
import com.example.productscanner.Screens.AccountScreen
import com.example.productscanner.Screens.FavoriteProduct
import com.example.productscanner.Screens.LimitationsScreen
import com.example.productscanner.Screens.PasswordResetScreen
import com.example.productscanner.Screens.ProductInfoScreen
import com.example.productscanner.Screens.RecommendationScreen
import com.example.productscanner.Screens.ScanHistoryScreen
import com.example.productscanner.Screens.SignInScreen
import com.example.productscanner.Screens.SignUpScreen
import com.example.productscanner.Screens.WelcomePageScreen
import com.example.productscanner.Screens.scan_screen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "welcome")

    {
        composable("welcome") {
            WelcomePageScreen(
                onSignInClick = { navController.navigate("signIn") },
                onSignUpClick = { navController.navigate("signUp") }
            )
        }
        composable("signIn") {
            SignInScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToScanner = { navController.navigate("scanner") },
                onNavigateToResetPassword = { navController.navigate("password_reset") }
            )
        }
        composable("signUp") {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToScanner = { navController.navigate("scanner") },
                onNavigateToSignInScreen = { navController.navigate("signIn") }
            )
        }
        composable("account") {
            AccountScreen(
                authViewModel = viewModel(),
                onWelcomePageClick = { navController.navigate("welcome") },
                onResetPasswordClick = { navController.navigate("password_reset") },
                onFavoriteClick = { navController.navigate("favorite") },
                navController
            )
        }
        composable("scanner") {
            scan_screen(navController)
        }
        composable("recommendations") {
            RecommendationScreen(navController)
        }

        composable("limitations") {
            LimitationsScreen(navController)
        }


        composable("password_reset") {
            PasswordResetScreen(
                authViewModel = viewModel(),
                onWelcomePageClick = { navController.navigate("welcome") },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("favorite") {
            FavoriteProduct(navController)
        }
        composable("history") {
            val context = LocalContext.current
            var scannedProducts by remember { mutableStateOf<List<ScannedProduct>>(emptyList()) }

            LaunchedEffect(true) {
                getUserScannedProducts(context) { products ->
                    scannedProducts = products
                }
            }
            ScanHistoryScreen(
                scannedProducts = scannedProducts,
                onProductInfoScreen = { scannedCode, productName, ingredientsText, ingredientsExplanation, imageUrl ->
                    val encodedExplanation = Uri.encode(ingredientsExplanation ?: "")
                    val encodedImageUrl = Uri.encode(imageUrl ?: "")
                    navController.navigate("product_info_screen/$scannedCode/$productName/$ingredientsText?ingredientsExplanation=$encodedExplanation&imageUrl=$encodedImageUrl")
                },
                navController
            )
        }

        composable(
            "product_info_screen/{scannedCode}/{productName}/{ingredientsText}?ingredientsExplanation={ingredientsExplanation}&imageUrl={imageUrl}",

            arguments = listOf(
                navArgument("scannedCode") { type = NavType.StringType },
                navArgument("productName") { type = NavType.StringType },
                navArgument("ingredientsText") { type = NavType.StringType },
                navArgument("ingredientsExplanation") {
                    type = NavType.StringType; nullable = true
                },
                navArgument("imageUrl") { type = NavType.StringType; nullable = true }


            )
        ) { backStackEntry ->
            val scannedCode = backStackEntry.arguments?.getString("scannedCode")
            val productName = backStackEntry.arguments?.getString("productName")
            val ingredientsText = backStackEntry.arguments?.getString("ingredientsText")
            val ingredientsExplanation =
                backStackEntry.arguments?.getString("ingredientsExplanation")
            val imageUrl = backStackEntry.arguments?.getString("imageUrl")


            if (scannedCode != null && productName != null && ingredientsText != null) {
                ProductInfoScreen(
                    productId = scannedCode,
                    productName = productName,
                    ingredientsText = ingredientsText,
                    ingredientsExplanation = ingredientsExplanation,
                    imageUrl = imageUrl,
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}
