package com.example.snapchat.ui.screens.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.snapchat.R
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.BasicIconButton
import com.example.snapchat.ui.common.MessageField
import com.example.snapchat.ui.theme.SnapchatTheme

@Composable
fun ImagePreviewScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ImagePreviewViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { contentPadding ->
        ImagePreviewBody(
            imagePath = viewModel.getImagePath(),
            imageCaption = uiState.caption,
            navigateBack = navigateBack,
            onImageCaptionChange = viewModel::onCaptionChange,
            onImageSend = navigateToHome,
            modifier = modifier.padding(contentPadding)
        )
    }
}

@Composable
fun ImagePreviewBody(
    imagePath: String?,
    imageCaption: String,
    navigateBack: () -> Unit,
    onImageCaptionChange: (String) -> Unit,
    onImageSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        AsyncImage(
            model = imagePath,
            modifier = Modifier
                .fillMaxSize(),
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )

        BasicIconButton(
            icon = R.drawable.ic_close,
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        MessageField(
            value = imageCaption,
            onValueChange = onImageCaptionChange,
            onSend = onImageSend,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ImagePreviewScreenPreview()
{
    SnapchatTheme {
        ImagePreviewBody(
            imagePath = "",
            imageCaption = "",
            navigateBack = {},
            onImageCaptionChange = {},
            onImageSend = {},
        )
    }
}
