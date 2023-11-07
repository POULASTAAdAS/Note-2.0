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
import com.example.note.presentation.screen.selected.SelectedScreen

@Composable
fun SetUpNavGraph(
    homeViewModel: HomeViewModel,
    startDestination: String,
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(route = Screens.Login.path) {
            val loginViewModel: LoginViewModel = hiltViewModel()
            LoginScreen(loginViewModel = loginViewModel)
        }

        composable(route = Screens.Home.path) {
            HomeScreen(
                homeViewModel = homeViewModel,
                navigateToDetailsScreen = {
                    homeViewModel.setNoteID(it)
                    navHostController.navigate(Screens.Selected.path)
                },
                navigateToNew = {
                    navHostController.navigate(Screens.New.path)
                }
            )
        }

        composable(route = Screens.New.path) {
            NewScreen(
                homeViewModel = homeViewModel,
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(route = Screens.Selected.path) {
            SelectedScreen(
                homeViewModel = homeViewModel,
                navigateBack = {
                    navHostController.popBackStack()
                }
            )
        }
    }
}