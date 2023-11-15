package com.example.note.domain.model

import androidx.annotation.RawRes
import com.example.note.R

sealed class OnBoardingPages(
    @RawRes
    val lottie: Int,
    val title: String,
    val content: String
) {
    data object First : OnBoardingPages(
        lottie = R.raw.logo,
        title = "Welcome",
        content = "we provide the best notes taking application on the playstore"
    )

    data object Second : OnBoardingPages(
        lottie = R.raw.account_managemnt,
        title = "Account Management",
        content = "create one account and access all your notes from any android device"
    )


    data object Third : OnBoardingPages(
        lottie = R.raw.auto_sync,
        title = "Auto Sync",
        content = "we save your notes to the server to make sure you don't have to worry about loosing them"
    )

    data object Fourth : OnBoardingPages(
        lottie = R.raw.pinned,
        title = "Pined Notes",
        content = "pin your most important notes to the top"
    )

    data object Fifth : OnBoardingPages(
        lottie = R.raw.lest_go,
        title = "Lets GO",
        content = "create an account or login to write down your amazing ideas"
    )
}
