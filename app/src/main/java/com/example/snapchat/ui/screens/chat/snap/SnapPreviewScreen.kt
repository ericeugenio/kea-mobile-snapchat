package com.example.snapchat.ui.screens.chat.snap

import android.net.Uri
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.ImageView

@Composable
fun SnapPreviewScreen(
    snapId: String?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SnapPreviewViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { contentPadding ->
        SnapPreviewBody(
            image = uiState.snap?.image?.uri,
            imageCaption = uiState.snap?.caption,
            navigateBack = navigateBack,
            modifier = modifier.padding(contentPadding)
        )
    }

    LaunchedEffect(snapId) {
        viewModel.onInit(snapId!!)
    }
}

@Composable
fun SnapPreviewBody(
    image: Uri?,
    imageCaption: String?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ImageView(
        image = image,
        onClose = navigateBack,
        modifier = modifier
    ) {
        if (imageCaption != null) {
            Text(text = imageCaption, modifier = Modifier.align(Alignment.Center) )
        }
    }
}