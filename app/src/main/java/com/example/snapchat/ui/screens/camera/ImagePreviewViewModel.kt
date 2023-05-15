package com.example.snapchat.ui.screens.camera

import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ImagePreviewViewModel(
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImagePreviewUiState())
    val uiState: StateFlow<ImagePreviewUiState> = _uiState.asStateFlow()

    private val caption
        get() = _uiState.value.caption

    fun getImagePath(): String? {
        return imageRepository.imageUri?.encodedPath
    }

    fun onCaptionChange(newCaption: String) {
        if (caption.length <= 512) {
            _uiState.update { it.copy(caption = newCaption) }
        }
    }

    fun onImageSend() {

    }
}

data class ImagePreviewUiState(
    val caption: String = ""
)