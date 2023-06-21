package com.example.snapchat.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.theme.SnapchatTheme
import com.example.snapchat.R
import com.example.snapchat.ui.common.*

@Composable
fun SignUpScreen(
    navigateBack: () -> Unit,
    navigateToChat: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { contentPadding ->
        SignUpBody(
            signUpUiState = uiState,
            onUsernameChange = viewModel::onUsernameChange,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
            onSubmit = { viewModel.onSignUp(navigateToChat) },
            navigateToSignIn = navigateBack,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun SignUpBody(
    signUpUiState: SignUpUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignUpForm(
            username = signUpUiState.username,
            email = signUpUiState.email,
            password = signUpUiState.password,
            repeatPassword = signUpUiState.repeatPassword,
            onUsernameChange = onUsernameChange,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onRepeatPasswordChange = onRepeatPasswordChange,
            onSubmit = onSubmit,
            modifier = Modifier.weight(1f)
        )
        SignUpFooter (
            navigateToSignIn = navigateToSignIn
        )
    }
}

@Composable
fun SignUpForm(
    username: String,
    email: String,
    password: String,
    repeatPassword: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        UsernameField(
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            value = username,
            onValueChange = onUsernameChange
        )
        EmailField(
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            value = email,
            onValueChange = onEmailChange
        )
        PasswordField(
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            value = password,
            onValueChange = onPasswordChange
        )
        RepeatPasswordField(
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            value = repeatPassword,
            onValueChange = onRepeatPasswordChange
        )
        BasicButton(
            text = R.string.sign_up,
            onClick = onSubmit
        )
    }
}

@Composable
fun SignUpFooter(
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextButton(
        text = R.string.sign_in,
        onClick = navigateToSignIn,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview()
{
    SnapchatTheme {
        SignUpBody(
            signUpUiState = SignUpUiState(
                username = "example",
                email = "example@mail.com",
                password = "Example123",
                repeatPassword = "Example123",
            ),
            onUsernameChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onRepeatPasswordChange = {},
            onSubmit = {},
            navigateToSignIn = {},
        )
    }
}