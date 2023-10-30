package com.example.note.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

            LoginScreen(
                loginViewModel = loginViewModel,
                navigateToHome = {
                    navHostController.popBackStack()
                    navHostController.navigate(Screens.Home.path)
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