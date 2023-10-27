package com.example.note.presentation.common

import android.app.Activity
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.note.utils.Constants
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes


@Composable
fun StartActivityForResult(
    key: Any,
    onResultReceived: (String) -> Unit,
    onDialogDismissed: () -> Unit,
    launcher: (ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>) -> Unit
) {
    val activity = LocalContext.current as Activity

    val activityLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                if (it.resultCode == Activity.RESULT_OK) {
                    val oneTapClient = Identity.getSignInClient(activity)
                    val credential = oneTapClient.getSignInCredentialFromIntent(it.data)

                    Log.d("it", it.data.toString())

                    val tokenId = credential.googleIdToken

                    if (tokenId != null) {
                        onResultReceived(tokenId)
                    } else {
                        Log.d("StartActivityForResult", "BLACK SCREEN CLICKED, DIALOG CLICKED")
                        onDialogDismissed()
                    }
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d("StartActivityForResult", "ONE-TAP DIALOG CANCELED")
                        onDialogDismissed()
                    }

                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d("StartActivityForResult", "ONE-TAP NETWORK ERROR")
                        onDialogDismissed()
                    }

                    else -> {
                        Log.d("StartActivityForResult", "${e.message}")
                        onDialogDismissed()
                    }
                }
            }
        }

    LaunchedEffect(key1 = key) {
        launcher(activityLauncher)
    }
}


fun singIn(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)

    val signInRequest = BeginSignInRequest
        .builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest
                .GoogleIdTokenRequestOptions
                .builder()
                .setSupported(true)
                .setServerClientId(Constants.CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient
        .beginSignIn(signInRequest)
        .addOnSuccessListener {
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        it.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                Log.d("SignIn", "Couldn't start One Tap UI: ${e.message}")
            }
        }
        .addOnFailureListener {
            Log.d("SignIn", "${it.message}")

            signUp(
                activity = activity,
                launchActivityResult = launchActivityResult,
                accountNotFound = accountNotFound
            )
        }
        .addOnCanceledListener {
            Log.d("SignIn", "sing up canceled")
        }
}

fun signUp(
    activity: Activity,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    accountNotFound: () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(Constants.CLIENT_ID)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                Log.d("SignUp", "Couldn't start One Tap UI: ${e.message}")
            }
        }
        .addOnFailureListener {
            Log.d("SignUp", "${it.message}")
            accountNotFound()
        }
}