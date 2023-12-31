package com.example.note.navigation

sealed class Screens(val path: String) {
    data object OnBoard : Screens("on_board")
    data object Login : Screens("login_screen")

    data object Home : Screens("home_screen")

    data object New : Screens("new_screen")
    data object Selected : Screens("selected_screen")

    data object RecentlyDeleted : Screens("recently_deleted")
    data object Settings : Screens("settings")
}