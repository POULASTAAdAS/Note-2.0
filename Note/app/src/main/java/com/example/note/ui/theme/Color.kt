package com.example.note.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val dark_primary = Color(0xFF000020)
val light_primary = Color(0xFFECF7FC)

val inverse_dark_primary = Color(0xFFECF7FC)
val inverse_light_primary = Color(0xFF000020)


val light_place_holder = Color.LightGray
val dark_place_holder = Color.Gray


val forgot_text = Color(0xFF0096DA)

val google_login_button = Color(0xFF5DCCFF)

val primary
    @Composable get() = if (isSystemInDarkTheme()) dark_primary else light_primary

val inverse_primary
    @Composable get() = if (isSystemInDarkTheme()) inverse_dark_primary else inverse_light_primary

val place_holder
    @Composable get() = if (isSystemInDarkTheme()) dark_place_holder else light_place_holder