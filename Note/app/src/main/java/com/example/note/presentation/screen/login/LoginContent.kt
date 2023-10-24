package com.example.note.presentation.screen.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.note.R
import com.example.note.presentation.common.BasicLoginButton
import com.example.note.presentation.common.ErrorTextFieldIndication
import com.example.note.presentation.common.ForgotText
import com.example.note.presentation.common.GoogleLoginButton
import com.example.note.presentation.common.LoginTextField
import com.example.note.ui.theme.primary

@Composable
fun LoginScreenContent(
    emailFiled: String,
    emailNotValid: Boolean,
    passwordToShort: Boolean,
    emailFieldEmpty: Boolean,
    passwordFieldEmpty: Boolean,
    basicLoginLoadingState: Boolean,
    passwordFiled: String,
    changeEmailFiled: (String) -> Unit,
    changePasswordFiled: (String) -> Unit,
    loadingState: Boolean,
    basicLoginClicked: () -> Unit,
    emailFieldEmptyCheck: () -> Unit,
    passwordFieldEmptyValidityCheck: () -> Unit,
    enableLoginState: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = primary
            )
            .padding(
                top = 35.dp,
                start = 35.dp,
                end = 35.dp
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        val haptic = LocalHapticFeedback.current
        val passwordVisibility = remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.aspectRatio(2.5f))

        Text(
            text = "Login",
            modifier = Modifier.padding(start = 8.dp),
            style = TextStyle(
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                color = MaterialTheme.colorScheme.inversePrimary,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        LoginTextField(
            value = emailFiled,
            label = "Email",
            iconClicked = {},
            onValueChange = changeEmailFiled,
            isError = emailNotValid || emailFieldEmpty,
            keyboardActions = KeyboardActions(
                onNext = {
                    emailFieldEmptyCheck()
                }
            )
        )

        if (emailNotValid) ErrorTextFieldIndication(text = "Email is not valid")
        else if (emailFieldEmpty) ErrorTextFieldIndication(text = "Email can't be empty")
        else ErrorTextFieldIndication(text = "")

        Spacer(modifier = Modifier.height(8.dp))

        LoginTextField(
            value = passwordFiled,
            label = "Password",
            onValueChange = changePasswordFiled,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    passwordFieldEmptyValidityCheck()
                }
            ),
            isError = passwordFieldEmpty || passwordToShort,
            iconClicked = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                passwordVisibility.value = !passwordVisibility.value
            },
            icon = if (passwordVisibility.value) painterResource(id = R.drawable.ic_visibility_on)
            else painterResource(id = R.drawable.ic_visibility_off),
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None
            else PasswordVisualTransformation()
        )


        if (passwordToShort) ErrorTextFieldIndication(text = "Password must be 6+ character")
        else if (passwordFieldEmpty) ErrorTextFieldIndication(text = "Email can't be empty")
        else ErrorTextFieldIndication(text = "")


        ForgotText(text = "Forgot Password ?") {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }

        Spacer(modifier = Modifier.height(70.dp))

        BasicLoginButton(
            basicLoginLoadingState = basicLoginLoadingState,
            loginButtonClicked = {
                basicLoginClicked()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        )

        Column(
            modifier = Modifier
                .aspectRatio(1.2f),
            verticalArrangement = Arrangement.Bottom
        ) {
            GoogleLoginButton(
                basicLoginLoadingState = basicLoginLoadingState,
                text = "Continue with Google",
                loadingState = loadingState,
                googleButtonClicked = {
                    enableLoginState()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewLight() {
    LoginScreenContent(
        emailFiled = "",
        emailNotValid = false,
        passwordToShort = false,
        emailFieldEmpty = false,
        passwordFieldEmpty = false,
        basicLoginLoadingState = false,
        passwordFiled = "",
        changeEmailFiled = {},
        changePasswordFiled = {},
        loadingState = false,
        basicLoginClicked = { },
        emailFieldEmptyCheck = { },
        passwordFieldEmptyValidityCheck = { }
    ) {

    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    LoginScreenContent(
        emailFiled = "",
        emailNotValid = false,
        passwordToShort = false,
        emailFieldEmpty = false,
        passwordFieldEmpty = false,
        basicLoginLoadingState = false,
        passwordFiled = "",
        changeEmailFiled = {},
        changePasswordFiled = {},
        loadingState = false,
        basicLoginClicked = { },
        emailFieldEmptyCheck = { },
        passwordFieldEmptyValidityCheck = { }
    ) {

    }
}