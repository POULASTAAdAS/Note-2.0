package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.note.navigation.SetUpNavGraph
import com.example.note.presentation.screen.home.HomeViewModel
import com.example.note.ui.theme.NoteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            NoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navHostController = rememberNavController()
                    val startDestination by homeViewModel.startDestination.collectAsState()

                    startDestination?.let {
                        SetUpNavGraph(
                            homeViewModel = homeViewModel,
                            startDestination = it,
                            navHostController = navHostController
                        )
                    }
                }
            }
        }
    }

    private fun setUpSplashScreen() {
        var keepSplashOpen = true

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.isLoading.collect {
                    keepSplashOpen = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpen
        }
    }
}