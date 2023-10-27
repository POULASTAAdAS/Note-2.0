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

            LaunchedEffect(key1 = Unit) {
                delay(400)
                keepSplashOpened()
            }
            LoginScreen(loginViewModel = loginViewModel,
                navigateToHome = {
                    navHostController.popBackStack()
                    navHostController.navigate("home_screen")
                    keepSplashOpened()
                }
            )
        }

        composable(route = Screens.Home.path) {
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                homeViewModel = homeViewModel,
                navigateToSelectedScreen = {
                    navHostController.popBackStack()
                    // TODO navigation
                }
            )
        }
    }
}