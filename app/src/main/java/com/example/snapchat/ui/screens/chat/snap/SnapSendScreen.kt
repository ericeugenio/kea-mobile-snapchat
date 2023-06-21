package com.example.snapchat.ui.screens.chat.snap

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.ImageView
import com.example.snapchat.ui.common.MessageField
import com.example.snapchat.ui.theme.SnapchatTheme

@Composable
fun SnapSendScreen(
    navigateBack: () -> Unit,
    navigateToChat: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SnapSendViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { contentPadding ->
        SnapSendBody(
            image = viewModel.imageUri,
            imageCaption = uiState.caption,
            onImageCaptionChange = viewModel::onCaptionChange,
            onImageSend = { viewModel.onImageSend(navigateToChat) },
            navigateBack = navigateBack,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun SnapSendBody(
    image: Uri?,
    imageCaption: String,
    onImageCaptionChange: (String) -> Unit,
    onImageSend: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    ImageView(
        image = image,
        onClose = navigateBack,
        modifier = modifier
    ) {
        MessageField(
            value = imageCaption,
            onValueChange = onImageCaptionChange,
            onSend = onImageSend,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SnapSendScreenPreview()
{
    SnapchatTheme {
        SnapSendBody(
            image = null,
            imageCaption = "",
            onImageCaptionChange = {},
            onImageSend = {},
            navigateBack = {},
        )
    }
}
