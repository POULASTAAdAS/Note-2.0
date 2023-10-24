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
import com.example.note.domain.repository.DataStoreOperation
import com.example.note.domain.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val application: Application,
    private val networkRepository: NetworkRepository,
    private val dataStoreOperation: DataStoreOperation
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

    private var loginResponse: MutableState<DataOrException<LoginResponse, Boolean, Exception>> =
        mutableStateOf(DataOrException())

    init {
        viewModelScope.launch {
            dataStoreOperation.readSignedInState().collect {
                loggedInState.value = it
            }
        }
    }

    private suspend fun saveJWTToken(jwtToken: String) {
        dataStoreOperation.saveUpdateJWTToken(jwtToken = jwtToken)
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
                loggedInState.value = true

                Log.d("CredentialTest", "saving successful")
            } catch (e: CreateCredentialCancellationException) {
                Log.d("CredentialTest", "user canceled save")
                loggedInState.value = true
            } catch (e: CreateCredentialException) {
                Log.d("CredentialTest", " ${e.message}")
            } catch (e: Exception) {
                Log.d("unknown error", e.message.toString())
            }
        }
    }

    private suspend fun getCredential(activity: Activity): PasswordCredential? {
        try {
            val getCredRequest = GetCredentialRequest(
                listOf(GetPasswordOption())
            )

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

    // one time call
    fun signInWithSavedCredential(activity: Activity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credential = getCredential(activity) ?: return@launch
                signedInPasswordCredential.value = credential
                dataStoreOperation.saveUpdateSignedInState(true)
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
            loginResponse.value = networkRepository.loginSignUp(
                request = LoginRequest(
                    email = emailFiled.value,
                    password = passwordFiled.value
                )
            )


            if (loginResponse.value.data?.token != null) {
                val jwtToken = loginResponse.value.data!!.token!!
                saveJWTToken(jwtToken)

                startBasicLoginProcess(
                    activity = activity,
                    credential = credential
                )
            } else {
                unableToLogin.value = true
                emptyTextAndPasswordField()
            }
            changeBasicLoginLoadingState(false)
            loginResponse.value.loading = false
        }
    }


    fun googleLoginSignUp(
        tokenId: String
    ) {
        loginResponse.value.loading = true

        Log.d("token" , tokenId)

        viewModelScope.launch(Dispatchers.IO) {

            loginResponse.value = networkRepository.loginSignUp(
                request = LoginRequest(
                    googleToken = tokenId
                )
            )

            if (loginResponse.value.data?.message != null && loginResponse.value.data?.message == "authorized") {
                loginResponse.value.data!!.googleLogIn = true
                dataStoreOperation.saveUpdateSignedInState(true)
                loggedInState.value = true
            } else {
                unableToLogin.value = true
            }

            loginResponse.value.loading = false
        }
    }
}