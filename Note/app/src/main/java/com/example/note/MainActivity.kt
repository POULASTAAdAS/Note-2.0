package com.example.note

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.note.navigation.Screens
import com.example.note.navigation.SetUpNavGraph
import com.example.note.presentation.screen.home.HomeViewModel
import com.example.note.ui.theme.NoteTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            NoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    val navHostController = rememberNavController()
                    val homeViewModel: HomeViewModel = hiltViewModel()

                    SetUpNavGraph(
                        homeViewModel = homeViewModel,
                        startDestination = Screens.Login.path, // getStartDestination()
                        navHostController = navHostController,
                        keepSplashOpened = {
//                            keepSplashOpened = false
                        }
                    )
                }
            }
        }
    }
}