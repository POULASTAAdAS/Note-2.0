package com.example.note.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.note.presentation.screen.home.HomeScreen
import com.example.note.presentation.screen.home.HomeViewModel
import com.example.note.presentation.screen.login.LoginScreen
import com.example.note.presentation.screen.login.LoginViewModel
import com.example.note.presentation.screen.neww.NewScreen

@Composable
fun SetUpNavGraph(
    homeViewModel: HomeViewModel,
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
            HomeScreen(
                homeViewModel = homeViewModel,
                navigateToSelectedScreen = {
                    navHostController.navigate("selected_screen/$it")
                },
                navigateToNew = {
                    navHostController.navigate("new_screen/${-1}")
                }
            )
        }

        composable(route = Screens.New.path) {
            NewScreen()
        }
    }
}