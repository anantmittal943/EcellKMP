package com.anantmittal.ecellkmp.presentation.login_screen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anantmittal.ecellkmp.utility.presentation.ColorAccent
import com.anantmittal.ecellkmp.utility.presentation.White

@Composable
fun LoginFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onUnfocus: (() -> Unit)? = null,
    modifier: Modifier,
    error: String? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None

) {
    var wasFocused by remember { mutableStateOf(false) }

    CompositionLocalProvider(
        LocalTextSelectionColors.provides(
            TextSelectionColors(
                handleColor = ColorAccent,
                backgroundColor = ColorAccent
            )
        )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            singleLine = true,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = modifier.fillMaxWidth()
                .onFocusChanged { state ->
                    if (wasFocused && !state.isFocused) {
                        onUnfocus?.invoke()
                    }
                    wasFocused = state.isFocused
                },
            isError = error != null,
            supportingText = {
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = White,
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedLabelColor = White,
                unfocusedLabelColor = White,
                focusedBorderColor = ColorAccent,
                unfocusedBorderColor = White,
                focusedLeadingIconColor = ColorAccent,
                unfocusedLeadingIconColor = White,
                focusedTrailingIconColor = White,
                unfocusedTrailingIconColor = ColorAccent,
                errorTextColor = White,
                errorLeadingIconColor = Color.Red
            )
        )
    }
}