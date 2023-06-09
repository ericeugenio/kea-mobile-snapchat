package com.example.snapchat.ui.screens.chat.snap

import android.Manifest
import android.net.Uri
import androidx.camera.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snapchat.R
import com.example.snapchat.ui.ModelViewProvider
import com.example.snapchat.ui.common.*
import com.example.snapchat.ui.theme.SnapchatTheme
import com.example.snapchat.utilities.*
import com.example.snapchat.utilities.camera.PhotoListener
import com.example.snapchat.utilities.camera.PhotoManager
import com.example.snapchat.utilities.camera.PhotoUiState

@Composable
fun SnapTakeScreen(
    navigateBack: () -> Unit,
    navigateToSnapSend: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SnapTakeViewModel = viewModel(factory = ModelViewProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val isPermissionGranted by remember {
        mutableStateOf(
            context.checkPermission(Manifest.permission.CAMERA)
        )
    }

    Scaffold { contentPadding ->
        if (isPermissionGranted) {
            SnapTakeWithPermissionBody(
                photoUiSate = uiState,
                navigateBack = navigateBack,
                navigateToSnapSend = navigateToSnapSend,
                onCameraInit = viewModel::onCameraInit,
                onFlashTapped = viewModel::onFlashTapped,
                onLensFlipTapped = viewModel::onLensFlipTapped,
                onImageCaptureSuccess = viewModel::onImageCapture,
                modifier = modifier.padding(contentPadding)
            )
        }
        else {
            CameraWithoutPermissionBody(
                navigateBack = navigateBack,
                navigateToSettingsApp = { context.getActivity().navigateToSettings() },
                modifier = modifier.padding(contentPadding)
            )
        }
    }
}

@Composable
fun SnapTakeWithPermissionBody(
    photoUiSate: PhotoUiState,
    navigateBack: () -> Unit,
    navigateToSnapSend: () -> Unit,
    onCameraInit: (MutableMap<Int, CameraInfo>) -> Unit,
    onFlashTapped: () -> Unit,
    onLensFlipTapped: () -> Unit,
    onImageCaptureSuccess: (Uri?, () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val photoManager = remember {
        PhotoManager.Builder(context, lifecycleOwner)
            .addListener(object : PhotoListener {
                override fun onInit(lensInfo: MutableMap<Int, CameraInfo>) {
                    onCameraInit(lensInfo)
                }

                override fun onSuccess(imageUri: Uri?) {
                    onImageCaptureSuccess(imageUri, navigateToSnapSend)
                }

                override fun onError() {
                    TODO("Handle")
                }
            })
            .build()
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        AndroidView(
            factory = { photoManager.getPreview(photoUiSate) },
            update = { view -> photoManager.getPreview(photoUiSate, view) },
            modifier = Modifier
                .fillMaxSize(),
        )

        BasicIconButton(
            icon = R.drawable.ic_close,
            onClick = navigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        if (photoUiSate.hasFlash()) {
            BasicFilledIconButton(
                icon = if (photoUiSate.isFlashOn()) R.drawable.ic_flash_on
                    else R.drawable.ic_flash_off,
                onClick = onFlashTapped,
                containerAlpha = 0.6F,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp, 24.dp, 164.dp, 24.dp)
            )
        }
        CameraLensIconButton(
            onClick = { photoManager.captureImage() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .size(64.dp)
        )
        if (photoUiSate.hasDualCamera()) {
            val rotationAngle by animateFloatAsState(
                targetValue = if (photoUiSate.isBackCamera()) 0F else 180F,
                animationSpec = tween(1000)
            )

            BasicFilledIconButton(
                icon = R.drawable.ic_flip_camera,
                onClick = onLensFlipTapped,
                containerAlpha = 0.6F,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(164.dp, 24.dp, 16.dp, 24.dp),
                iconModifier = Modifier
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
fun CameraWithoutPermissionBody(
    navigateBack: () -> Unit,
    navigateToSettingsApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.permission_camera_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.permission_camera_permanently_denied_text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        BasicTonalButton(
            text = R.string.permission_go_to_settings,
            onClick = navigateToSettingsApp
        )
        BasicButton(
            text = R.string.go_home,
            onClick = navigateBack
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SnapTakeWithPermissionScreenPreview()
{
    SnapchatTheme {
        SnapTakeWithPermissionBody(
            photoUiSate = PhotoUiState(),
            navigateBack = {},
            navigateToSnapSend = {},
            onCameraInit = {},
            onFlashTapped = {},
            onLensFlipTapped = {},
            onImageCaptureSuccess = { _: Uri?, _: () -> Unit ->

            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SnapTakeWithoutPermissionScreenPreview()
{
    SnapchatTheme {
        CameraWithoutPermissionBody(
            navigateBack = {},
            navigateToSettingsApp = {}
        )
    }
}