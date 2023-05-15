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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.BasicButton
import com.example.snapchat.ui.common.EmailField
import com.example.snapchat.ui.common.PasswordField
import com.example.snapchat.ui.theme.SnapchatTheme
import com.example.snapchat.R
import com.example.snapchat.ui.common.BasicTextButton

@Composable
fun SignInScreen(
    navigateToHome: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { contentPadding ->
        SignInBody(
            signInUiState = uiState,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onSubmit = { viewModel.signIn(navigateToHome) },
            navigateToSignUp = navigateToSignUp,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun SignInBody(
    signInUiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SignInForm(
            email = signInUiState.email,
            password = signInUiState.password,
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange,
            onSubmit = onSubmit,
            modifier = Modifier.weight(1f)
        )
        SignInFooter (
            navigateToSignUp = navigateToSignUp
        )
    }
}

@Composable
fun SignInForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
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
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            value = password,
            onValueChange = onPasswordChange
        )
        BasicButton(
            text = R.string.sign_in,
            onClick = onSubmit
        )
    }
}

@Composable
fun SignInFooter(
    navigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextButton(
        text = R.string.sign_up,
        onClick = navigateToSignUp,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview()
{
    SnapchatTheme {
        SignInBody(
            signInUiState = SignInUiState(
                email = "example@mail.com",
                password = "Example123",
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            navigateToSignUp = {}
        )
    }
}