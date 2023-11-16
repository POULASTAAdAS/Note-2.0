package com.example.model

sealed class EndPoint(val path: String) {
    data object Root : EndPoint("/")

    data object LoginSignUp : EndPoint("/login_signup")

    data object GetAll : EndPoint("/get_all")

    data object AddOne : EndPoint("/add_one")
    data object AddMultiple : EndPoint("/add_multiple")

    data object UpdateOne : EndPoint("/update_one")
    data object UpdateMultiple : EndPoint("/update_multiple")

    data object DeleteOne : EndPoint("/delete_one")
    data object DeleteMultiple : EndPoint("/delete_multiple")

    data object UpdateUserName : EndPoint("/update_username")

    data object DeleteUser : EndPoint("/delete_user")

    data object Authorized : EndPoint("/authorized")
    data object UnAuthorized : EndPoint("/un_authorized")
}