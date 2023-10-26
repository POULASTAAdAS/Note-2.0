package com.example.note.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.note.presentation.screen.home.HomeScreen
import com.example.note.presentation.screen.home.HomeViewModel
import com.example.note.presentation.screen.login.LoginScreen
import com.example.note.presentation.screen.login.LoginViewModel
import kotlinx.coroutines.delay

@Composable
fun SetUpNavGraph(
    startDestination: String,
    navHostController: NavHostController,
    keepSplashOpened: () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = Screens.Login.path) {
            val loginViewModel: LoginViewModel = hiltViewModel()

            LoginScreen(loginViewModel = loginViewModel,
                navigateToHome = { userExists ->
                    navHostController.popBackStack()
                    navHostController.navigate("home_screen/$userExists")
                    keepSplashOpened()
                }
            )

            LaunchedEffect(key1 = Unit) {
                delay(400)
                keepSplashOpened()
            }
        }

        composable(route = Screens.Home.path,
            arguments = listOf(
                navArgument("userExists") {
                    type = NavType.BoolType
                }
            )
        ) {
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                userExists = it.arguments!!.getBoolean("userExists"), // TODO try adding false if not called from login screen
                homeViewModel = homeViewModel,
                navigateToSelectedScreen = {
                    navHostController.popBackStack()
                    // TODO navigation
                }
            )
        }
    }
}