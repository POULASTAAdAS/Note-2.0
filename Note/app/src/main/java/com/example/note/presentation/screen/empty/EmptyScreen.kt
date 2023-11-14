package com.example.note.presentation.screen.empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.note.R
import com.example.note.ui.theme.url_color

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier,
    lottie: Int
) {
    val composition
            by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(lottie))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            restartOnPlay = false,
            modifier = Modifier
                .aspectRatio(5f / 10f)
                .padding(bottom = 50.dp)
                .then(modifier)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    EmptyScreen(lottie = R.raw.empty_home_screen)
}