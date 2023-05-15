package com.example.snapchat.utilities

import android.util.Patterns
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.focus.FocusManager
import java.util.regex.Pattern

private const val PASS_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{6,}$"

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() &&
            Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidPassword(): Boolean {
    return this.isNotBlank() &&
            Pattern.compile(PASS_PATTERN).matcher(this).matches()
}
