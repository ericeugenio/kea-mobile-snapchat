package com.example.snapchat.ui.screens.camera

import android.net.Uri
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.ImageRepository
import com.example.snapchat.utilities.hardware.CameraUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CameraViewModel(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    private val flashMode
        get() = _uiState.value.flashMode
    private val lens
        get() = _uiState.value.lens
    private val lensInfo
        get() = _uiState.value.lensInfo

    fun onCameraInit(lensInfo: MutableMap<Int, CameraInfo>) {
        _uiState.update { it.copy(lensInfo = lensInfo) }
    }

    fun onFlashTapped() {
        val newCameraFlashMode =
            if (flashMode == ImageCapture.FLASH_MODE_OFF) ImageCapture.FLASH_MODE_ON
            else ImageCapture.FLASH_MODE_OFF

        _uiState.update { it.copy(flashMode = newCameraFlashMode) }
    }

    fun onLensFlipTapped() {
        val newCameraLens =
            if (lens == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
            else CameraSelector.LENS_FACING_BACK

        if (lensInfo[newCameraLens] != null) {
            val newCameraFlashMode =
                if (lensInfo[newCameraLens]!!.hasFlashUnit()) flashMode
                else ImageCapture.FLASH_MODE_OFF

            _uiState.update { it.copy(flashMode = newCameraFlashMode, lens = newCameraLens) }
        }
    }

    fun onImageCapture(
        imageUri: Uri?,
        navigateToImagePreview: () -> Unit
    ) {
        imageRepository.imageUri = imageUri
        navigateToImagePreview()
    }
}


