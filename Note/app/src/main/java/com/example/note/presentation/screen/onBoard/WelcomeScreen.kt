package com.example.note.presentation.screen.onBoard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.domain.model.OnBoardingPages
import com.example.note.ui.theme.google_login_button
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(
    letsGoButtonClicked: () -> Unit
) {
    val pages = listOf(
        OnBoardingPages.First,
        OnBoardingPages.Second,
        OnBoardingPages.Third,
        OnBoardingPages.Fourth,
        OnBoardingPages.Fifth
    )

    val pageState = rememberPagerState { 5 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(15.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.End
        ) {
            val scope = rememberCoroutineScope()

            SkipButton(
                pageState
            ) {
                scope.launch {
                    pageState.animateScrollToPage(4)
                }
            }
        }

        HorizontalPager(state = pageState) {
            DefaultWelcomeScreen(oP = pages[it])
        }

        Row(
            modifier = Modifier
                .wrapContentSize()
        ) {
            PagerIndicator(pagerState = pageState)
        }

        Row(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = pageState.currentPage == 4) {
                LetsGoButton {
                    letsGoButtonClicked()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerIndicator(
    pagerState: PagerState
) {
    repeat(pagerState.pageCount) {
        val color = if (pagerState.currentPage == it) google_login_button
        else MaterialTheme.colorScheme.inversePrimary.copy(.3f)

        Box(
            modifier = Modifier
                .padding(8.dp)
                .clip(CircleShape)
                .background(color = color)
                .size(10.dp)
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SkipButton(
    pagerState: PagerState,
    text: String = if (pagerState.currentPage == 4) "" else "Skip",
    skipButtonClicked: () -> Unit
) {
    Text(
        text = text,
        fontWeight = FontWeight.Light,
        color = MaterialTheme.colorScheme.inversePrimary,
        modifier = Modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = skipButtonClicked,
            )
            .padding(end = 8.dp)
    )
}

@Composable
fun LetsGoButton(
    letsGoButtonClicked: () -> Unit
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        onClick = letsGoButtonClicked,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.inversePrimary
        ),
        border = BorderStroke(
            width = 1.3.dp,
            color = MaterialTheme.colorScheme.inversePrimary
        )
    ) {
        Text(text = "Continue")
    }
}


@Preview
@Composable
private fun Preview() {
    WelcomeScreen {

    }
}