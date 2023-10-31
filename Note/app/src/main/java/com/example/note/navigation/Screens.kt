package com.example.note.navigation

sealed class Screens(val path : String){
    data object Login : Screens("login_screen")

    data object Home : Screens("home_screen")

    data object New: Screens("new_screen/{noteID}")
    data object Selected: Screens("selected_screen/{noteID}")
}