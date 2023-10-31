package com.example.note.presentation.screen.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialCancellationException
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.note.data.remote.DataOrException
import com.example.note.domain.model.LoginRequest
import com.example.note.domain.model.LoginResponse
import com.example.note.domain.model.UserExists
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.net.CookieManager
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation,
    private val cookieManager: CookieManager
) : ViewModel() {
    val googleButtonLoadingState = mutableStateOf(false)
    val basicLoginLoadingState = mutableStateOf(false)

    val emailFiled = mutableStateOf("")
    val passwordFiled = mutableStateOf("")

    val emailNotValid = mutableStateOf(false)
    val passwordToShort = mutableStateOf(false)

    val emailFieldEmpty = mutableStateOf(false)
    val passwordFieldEmpty = mutableStateOf(false)

    val unableToLogin = mutableStateOf(false)

    private val credentialManager by lazy { CredentialManager.create(application) }
    val signedInPasswordCredential = MutableStateFlow<PasswordCredential?>(null)

    val loggedInState = mutableStateOf(false)

    private val loginResponse: MutableState<DataOrException<LoginResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())


    init {
        viewModelScope.launch {
            dataStoreOperation.readSignedInState().collect {
                loggedInState.value = it
                Log.d("loginState.value login", it.toString())
            }
        }
    }

    private suspend fun saveJWTTokenOrCookie(jwtToken: String) {
        dataStoreOperation.saveUpdateJWTTokenOrSession(jwtToken = jwtToken)
    }

    fun changeGoogleButtonLoadingState(value: Boolean) {
        googleButtonLoadingState.value = value
    }

    private fun changeBasicLoginLoadingState(value: Boolean) {
        basicLoginLoadingState.value = value
    }

    fun changeEmailFiled(value: String) {
        emailFiled.value = value
    }

    fun changePasswordFiled(value: String) {
        passwordFiled.value = value
    }


    fun changeUnableToLogin(value: Boolean) {
        unableToLogin.value = value
    }


    fun emailValidationCheck() {
        emailNotValid.value = !emailFiled.value.endsWith("@gmail.com")
    }

    fun passwordToShortCheck() {
        passwordToShort.value = passwordFiled.value.length < 6
    }


    fun emailEmptyCheck() {
        emailFieldEmpty.value = emailFiled.value.isEmpty()
    }

    fun passwordEmptyCheck() {
        passwordFieldEmpty.value = passwordFiled.value.isEmpty()
    }

    private fun emptyTextAndPasswordField() {
        changeEmailFiled("")
        changePasswordFiled("")
    }

    private fun signInOrSignUpWithCredential(activity: Activity) {
        signedInPasswordCredential.value = PasswordCredential(emailFiled.value, passwordFiled.value)
        saveCredential(activity, emailFiled.value, passwordFiled.value)
        emptyTextAndPasswordField()
    }

    private fun saveCredential(activity: Activity, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                credentialManager.createCredential(
                    context = activity, request = CreatePasswordRequest(email, password)
                )
                dataStoreOperation.saveUpdateSignedInState(true)
                dataStoreOperation.saveFirstTimeLoginState(true)
                dataStoreOperation.saveAuthenticationType(false)
                loggedInState.value = true

                Log.d("CredentialTest", "saving successful")
            } catch (e: CreateCredentialCancellationException) {
                loggedInState.value = true
                dataStoreOperation.saveUpdateSignedInState(true)
                dataStoreOperation.saveFirstTimeLoginState(true)
                dataStoreOperation.saveAuthenticationType(false)
                Log.d("CredentialTest", "user canceled save")
            } catch (e: CreateCredentialException) {
                Log.d("CredentialTest", " ${e.message}")
            } catch (e: Exception) {
                Log.d("unknown error", e.message.toString())
            }
        }
    }

    private suspend fun getCredential(activity: Activity): PasswordCredential? {
        try {
            val getCredRequest = GetCredentialRequest(listOf(GetPasswordOption()))

            //Show the user a dialog allowing them to pick a saved credential
            val credentialResponse = credentialManager.getCredential(
                request = getCredRequest,
                context = activity,
            )
            //Return the selected credential (as long as it's a username/password)
            return credentialResponse.credential as? PasswordCredential
        } catch (e: GetCredentialCancellationException) {
            return null
        } catch (e: NoCredentialException) {
            //We don't have a matching credential
            return null
        } catch (e: GetCredentialException) {
            Log.e("CredentialTest", "Error getting credential", e)
            throw e
        }
    }

    private fun getSignInWithSavedCredentialJWTToken(credential: PasswordCredential) {
        viewModelScope.launch {
            loginResponse.value =
                networkRepository.loginSignUp( // TODO signInWithSavedCredential server req to get jwt token
                    request = LoginRequest(
                        email = credential.id,
                        password = credential.password,
                        initial = true
                    )
                )

            if (loginResponse.value.data?.token != null) {
                val token = loginResponse.value.data!!.token!!

                saveJWTTokenOrCookie(token)
            }
        }
    }

    // one time call
    fun signInWithSavedCredential(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = getCredential(activity) ?: return@launch

                getSignInWithSavedCredentialJWTToken(credential = credential)
                signedInPasswordCredential.value = credential
                dataStoreOperation.saveUpdateSignedInState(true)
                dataStoreOperation.saveFirstTimeLoginState(true)
                dataStoreOperation.saveAuthenticationType(false)
                loggedInState.value = true
            } catch (e: Exception) {
                Log.d("CredentialTest", "error getting credential" + e.message.toString())
            }
        }
    }


    private fun startBasicLoginProcess(
        activity: Activity,
        credential: PasswordCredential?
    ) {
        credential.run {
            signInOrSignUpWithCredential(activity)
        }
    }

    fun basicLoginSignUp(
        activity: Activity,
        credential: PasswordCredential?,
    ) {
        changeUnableToLogin(false)
        changeBasicLoginLoadingState(true)
        loginResponse.value.loading = true

        viewModelScope.launch(Dispatchers.IO) {
            loginResponse.value = networkRepository.loginSignUp( //TODO first time login using jwt
                request = LoginRequest(
                    email = emailFiled.value,
                    password = passwordFiled.value,
                    initial = true
                )
            )

            if (loginResponse.value.data?.token != null) {
                val jwtToken = loginResponse.value.data!!.token!!

                if (
                    loginResponse.value.data!!.userExists!! == UserExists.YES_SAME_PASSWORD.name ||
                    loginResponse.value.data!!.userExists!! == UserExists.NO.name
                ) {
                    saveJWTTokenOrCookie(jwtToken)
                    startBasicLoginProcess(
                        activity = activity,
                        credential = credential
                    )
                } else {
                    // TODO UserExists.YES_DIFF_PASSWORD
                }
            } else {
                unableToLogin.value = true
                emptyTextAndPasswordField()
            }
            changeBasicLoginLoadingState(false)
            loginResponse.value.loading = false
        }
    }


    private suspend fun saveCookie() {
        try {
            val cookie = cookieManager.cookieStore.cookies[0].toString()
            saveJWTTokenOrCookie(cookie)
        } catch (e: Exception) {
            Log.d("error saving session data", e.message.toString())
        }
    }

    fun googleLoginSignUp(
        tokenId: String
    ) {
        loginResponse.value.loading = true
        Log.d("token", tokenId) // TODO comment time of production

        viewModelScope.launch(Dispatchers.IO) {
            loginResponse.value = networkRepository.loginSignUp( // TODO login using google
                request = LoginRequest(
                    googleToken = tokenId,
                    initial = true
                )
            )

            if (loginResponse.value.data?.googleLogIn != null && loginResponse.value.data?.googleLogIn == true) {
                dataStoreOperation.saveAuthenticationType(true)
                dataStoreOperation.saveUpdateSignedInState(true)
                dataStoreOperation.saveFirstTimeLoginState(true)
                saveCookie()
                loggedInState.value = true
            } else {
                unableToLogin.value = true
            }

            loginResponse.value.loading = false
        }
    }
}