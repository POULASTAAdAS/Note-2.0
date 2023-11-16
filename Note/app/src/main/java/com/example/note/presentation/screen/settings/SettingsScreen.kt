package com.example.note.presentation.screen.settings

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import com.example.note.connectivity.NetworkObserver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsScreenViewModel: SettingsScreenViewModel,
    navigateBack: () -> Unit,
    recentlyDeletedNavigationClick: () -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    val network by settingsScreenViewModel.network

    val syncState by settingsScreenViewModel.autoSync
    val sortState by settingsScreenViewModel.sortState

    val userNameCardState by settingsScreenViewModel.userNameCardState
    val logOutCardState by settingsScreenViewModel.logOutCardState
    val deleteAccountCardState by settingsScreenViewModel.deleteAccountCardState

    val userName by settingsScreenViewModel.userName
    val oldUserName = settingsScreenViewModel.oldUserName

    val isLoggedOut by settingsScreenViewModel.isLoggedOut
    val isAccountDeleted by settingsScreenViewModel.isAccountDeleted

    val navigateToLogInScreen by settingsScreenViewModel.navigateToLogInScreen

    val syncStateText = remember {
        mutableStateOf("auto sync on")
    }

    LaunchedEffect(
        key1 = network,
        key2 = syncState
    ) {
        syncStateText.value =
            if (network == NetworkObserver.STATUS.AVAILABLE && syncState) "Auto sync on"
            else if (!settingsScreenViewModel.checkInternetConnection()) "Please check your Internet connection"
            else "Turn on auto sync to save your notes to the server"
    }

    LaunchedEffect(key1 = navigateToLogInScreen) {
        if (navigateToLogInScreen) navigateToLoginScreen()
    }

    Scaffold(
        topBar = {
            SettingsTopBar(
                navigateBack = navigateBack
            )
        },
    ) { paddingValues ->
        SettingsScreenContent(
            paddingValues = paddingValues,
            // username
            context = context,
            network = settingsScreenViewModel.checkInternetConnection(),
            userName = userName,
            userNameCardState = userNameCardState,
            onValueChange = {
                settingsScreenViewModel.changeUserNameText(it)
            },
            changeUserNameCardState = {
                settingsScreenViewModel.changeUserNameCardState()
            },
            saveClicked = {
                if (userName == oldUserName)
                    Toast.makeText(context, "same username", Toast.LENGTH_SHORT).show()
                else if (userName.trim().isEmpty()) {
                    Toast.makeText(context, "username can't be empty", Toast.LENGTH_SHORT).show()
                    settingsScreenViewModel.userNameUpdateCancelClicked()
                } else {
                    settingsScreenViewModel.saveUserName()
                    Toast.makeText(context, "username updated", Toast.LENGTH_SHORT).show()
                }
            },
            userNameUpdateCancelClicked = {
                settingsScreenViewModel.userNameUpdateCancelClicked()
            },
            // auto sync
            syncState = syncState,
            syncText = syncStateText.value,
            syncCardEnabled = settingsScreenViewModel.checkInternetConnection(),
            syncCardSwitchClicked = {
                settingsScreenViewModel.toggleSyncSwitch(it)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            // sort type
            sortType = if (sortState) "Last Edited" else "Last Created",
            updateSortType = {
                settingsScreenViewModel.updateSortState(it)
            },
            // recently deleted
            recentlyDeletedNavigationClick = recentlyDeletedNavigationClick,
            // logOut
            logOutCardState = logOutCardState,
            logOutCardClicked = {
                settingsScreenViewModel.changeLogoutCardState()
            },
            logOutConformLogOut = {
                settingsScreenViewModel.logOutUser()
            },
            // delete account
            deleteAccountCardState = deleteAccountCardState,
            deleteAccountCardClicked = {
                settingsScreenViewModel.changeDeleteAccountCardState()
            },
            deleteAccountConformClicked = {
                settingsScreenViewModel.deleteUser(context)
            },
            isLoggedOut = isLoggedOut,
            isAccountDeleted = isAccountDeleted
        )
    }
}