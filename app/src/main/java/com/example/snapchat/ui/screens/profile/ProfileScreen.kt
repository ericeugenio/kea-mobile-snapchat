package com.example.snapchat.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.R
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.BasicButton
import com.example.snapchat.ui.common.NavigationTopAppBar
import com.example.snapchat.ui.theme.SnapchatTheme

@Composable
fun ProfileScreen (
    navigateBack: () -> Unit,
    navigateToSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    Scaffold (
        topBar = {
            NavigationTopAppBar(
                title = R.string.profile,
                navigationIcon = R.drawable.ic_arrow_back,
                onNavigation = navigateBack
            )
        }
    ) { contentPadding ->
        ProfileBody(
            onSignOut = { viewModel.onSignOut(navigateToSignIn) },
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun ProfileBody(
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicButton(
            text = R.string.sign_out,
            onClick = onSignOut
        )
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview()
{
    SnapchatTheme {
        ProfileBody(
            onSignOut = {}
        )
    }
}