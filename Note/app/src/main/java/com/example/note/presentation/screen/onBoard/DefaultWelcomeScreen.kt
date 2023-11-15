package com.example.note.presentation.screen.onBoard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.note.domain.model.OnBoardingPages

@Composable
fun DefaultWelcomeScreen(
    oP: OnBoardingPages
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(oP.lottie)
        )

        LottieAnimation(
            composition = composition,
            alignment = Alignment.BottomCenter,
            iterations = 20,
            modifier = Modifier.aspectRatio(.9f)
        )

        Text(
            text = oP.title,
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.inversePrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = oP.content,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.inversePrimary.copy(.6f),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f),
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            lineHeight = 30.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    DefaultWelcomeScreen(
        oP = OnBoardingPages.First
    )
}