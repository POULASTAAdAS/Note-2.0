package com.example.note.presentation.screen.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import com.example.note.presentation.common.StartActivityForResult
import com.example.note.presentation.common.singIn

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel
) {
    val googleButtonLoadingState by loginViewModel.googleButtonLoadingState
    val basicLoginLoadingState by loginViewModel.basicLoginLoadingState

    val emailFiled by loginViewModel.emailFiled
    val passwordFiled by loginViewModel.passwordFiled

    val emailNotValid by loginViewModel.emailNotValid
    val passwordToShort by loginViewModel.passwordToShort

    val emailFieldEmpty by loginViewModel.emailFieldEmpty
    val passwordFieldEmpty by loginViewModel.passwordFieldEmpty

    val userExists by loginViewModel.userExists
    val userExistsCount by loginViewModel.userExistsCount

    val unableToLogin by loginViewModel.unableToLogin

    val credential by loginViewModel.signedInPasswordCredential.collectAsState()

    val focusManager = LocalFocusManager.current
    val activity = LocalContext.current as Activity

    LoginScreenContent(
        loadingState = googleButtonLoadingState,
        emailFiled = emailFiled,
        emailNotValid = emailNotValid,
        emailFieldEmpty = emailFieldEmpty,
        userExists = userExists,
        userExistsCount = userExistsCount,
        passwordToShort = passwordToShort,
        passwordFieldEmpty = passwordFieldEmpty,
        basicLoginLoadingState = basicLoginLoadingState,
        passwordFiled = passwordFiled,
        changeEmailFiled = {
            loginViewModel.checkUserExistsStatus()
            loginViewModel.changeEmailFiled(it)
        },
        changePasswordFiled = {
            if (!it.contains(" "))
                loginViewModel.changePasswordFiled(it)
        },
        enableLoginState = {
            loginViewModel.changeGoogleButtonLoadingState(true)
        },
        emailFieldEmptyCheck = {
            loginViewModel.emailEmptyCheck()
            if (!emailFieldEmpty) focusManager.moveFocus(FocusDirection.Down)
        },
        passwordFieldEmptyValidityCheck = {
            loginViewModel.passwordEmptyCheck()
            loginViewModel.passwordToShortCheck()

            if (!passwordToShort && !passwordFieldEmpty) {
                loginViewModel.emailValidationCheck()
                if (emailNotValid) focusManager.moveFocus(FocusDirection.Up)
                else {
                    focusManager.clearFocus()

                    loginViewModel.basicLoginSignUp(
                        activity = activity,
                        credential = credential
                    )
                }
            }
        },
        basicLoginClicked = {
            loginViewModel.emailValidationCheck()
            loginViewModel.passwordToShortCheck()

            if (!emailNotValid && !passwordToShort) {
                focusManager.clearFocus()

                loginViewModel.basicLoginSignUp(
                    activity = activity,
                    credential = credential
                )
            }
        }
    )

    StartGoogleLoginProcess(
        loginViewModel = loginViewModel,
        loadingState = googleButtonLoadingState,
        loginDismissed = {
            loginViewModel.changeGoogleButtonLoadingState(false)
            loginViewModel.changeUnableToLogin(false)
        }
    )

    LaunchedEffect(key1 = unableToLogin) {
        if (unableToLogin) {
            Toast.makeText(
                activity,
                "server is down",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(key1 = Unit) {// logging in with old jwt auth account if saved at google password manager by the user
        loginViewModel.signInWithSavedCredential(activity)
    }
}


@Composable
fun StartGoogleLoginProcess(
    loginViewModel: LoginViewModel,
    loadingState: Boolean,
    loginDismissed: () -> Unit
) {
    val activity = LocalContext.current as Activity


    StartActivityForResult(
        key = loadingState,
        onResultReceived = {
            loginViewModel.googleLoginSignUp(tokenId = it)
            loginDismissed()
        },
        onDialogDismissed = loginDismissed
    ) { activityLauncher ->
        if (loadingState)
            if (loginViewModel.checkInternetConnection()) {
                singIn(
                    activity = activity,
                    launchActivityResult = { intentSenderRequest ->
                        loginDismissed()
                        activityLauncher.launch(intentSenderRequest)
                    },
                    accountNotFound = loginDismissed
                )
            } else {
                Toast.makeText(
                    activity,
                    "please check your internet connection",
                    Toast.LENGTH_SHORT
                ).show()

                loginDismissed()
            }
    }
}