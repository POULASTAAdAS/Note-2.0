package com.example.note.presentation.screen.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.note.ui.theme.url_color
import com.example.note.utils.UrlVisualTransformation


@Composable
fun DataContent(
    paddingValues: PaddingValues,
    focusManager: FocusManager,
    updateDate: String = "",
    updateTime: String = "",
    newNote: Boolean,
    heading: String,
    content: String,
    onHeadingChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        if (newNote) focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding(),
                start = 15.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!newNote)
            Text(
                text = "Last updated on $updateDate at $updateTime",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.inversePrimary.copy(.6f),
                fontWeight = FontWeight.Light,
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )

        DataScreenTextField(
            text = heading,
            onTextChange = onHeadingChange,
            placeHolder = "heading..",
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        DataScreenTextField(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            text = content,
            onTextChange = onContentChange,
            placeHolder = "write something...",
            singleLine = false,
            textStyle = LocalTextStyle.current,
            visualTransformation = UrlVisualTransformation(url_color)
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewDark() {
    val focusManager = LocalFocusManager.current

    DataContent(
        focusManager = focusManager,
        paddingValues = PaddingValues(),
        heading = "",
        newNote = true,
        onHeadingChange = {},
        content = "",
        onContentChange = {}
    )
}