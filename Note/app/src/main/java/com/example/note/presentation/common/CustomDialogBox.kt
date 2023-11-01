package com.example.note.presentation.common

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.note.R
import com.example.note.ui.theme.google_login_button
import com.example.note.ui.theme.place_holder

@Composable
fun CustomDialogBox(
    hideDialog: (Boolean) -> Unit,
    returnUrl: (String) -> Unit,
    clearFocus: () -> Unit
) {
    val url = remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }

    Dialog(
        onDismissRequest = {
            hideDialog(false)
        }
    ) {
        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            TextField(
                value = url.value,
                onValueChange = { url.value = it },
                placeholder = {
                    Text(text = "https://google.com")
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_link),
                        contentDescription = null
                    )
                },
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.inversePrimary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
                    unfocusedPlaceholderColor = place_holder,
                    focusedPlaceholderColor = place_holder,
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (url.value.isEmpty()) {
                            Toast.makeText(
                                context,
                                "url is empty",
                                Toast.LENGTH_SHORT
                            ).show()

                            hideDialog(false)
                        } else
                            if (url.value.endsWith(".com")) {
                                clearFocus()
                                returnUrl(url.value)

                                hideDialog(false)
                            } else
                                Toast.makeText(
                                    context,
                                    "not a url",
                                    Toast.LENGTH_SHORT
                                ).show()
                    }
                )
            )

            Button(
                onClick = {
                    if (url.value.isEmpty()) {
                        Toast.makeText(
                            context,
                            "url is empty",
                            Toast.LENGTH_SHORT
                        ).show()

                        hideDialog(false)
                    } else
                        if (url.value.endsWith(".com")) {
                            clearFocus()
                            returnUrl(url.value)

                            hideDialog(false)
                        } else
                            Toast.makeText(
                                context,
                                "not a url",
                                Toast.LENGTH_SHORT
                            ).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary,
                    contentColor = google_login_button
                )
            ) {
                Text(text = "save")
            }
        }
    }
}