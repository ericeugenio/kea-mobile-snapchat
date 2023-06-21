package com.example.snapchat.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.theme.SnapchatTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (
    navigateToChat: () -> Unit,
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    Scaffold { contentPadding ->
        SplashBody(
            modifier = modifier.padding(contentPadding)
        )
    }

    LaunchedEffect(true) {
        delay(SplashViewModel.SPLASH_DELAY)
        viewModel.onAppStart(navigateToChat, navigateToSignIn)
    }
}

@Composable
fun SplashBody(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview()
{
    SnapchatTheme {
        SplashBody()
    }
}