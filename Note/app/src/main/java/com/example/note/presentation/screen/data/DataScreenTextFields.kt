package com.example.note.presentation.screen.data

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.note.ui.theme.place_holder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreenTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit,
    singleLine: Boolean = true,
    placeHolder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Transparent,
        cursorColor = MaterialTheme.colorScheme.inversePrimary,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        focusedTrailingIconColor = MaterialTheme.colorScheme.inversePrimary,
        placeholderColor = place_holder,
    ),
    textStyle: TextStyle = TextStyle(
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = FontWeight.SemiBold
    ),
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        modifier = Modifier.then(modifier),
        value = text,
        textStyle = textStyle,
        onValueChange = onTextChange,
        placeholder = {
            Text(text = placeHolder)
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors,
        visualTransformation = visualTransformation
    )
}


@Preview
@Composable
private fun Preview() {
    Column {
        DataScreenTextField(
            text = "",
            onTextChange = {},
            placeHolder = "Heading..",
            keyboardActions = KeyboardActions()
        )
    }
}