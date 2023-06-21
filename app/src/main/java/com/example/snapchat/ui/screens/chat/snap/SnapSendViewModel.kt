package com.example.snapchat.ui.screens.chat.snap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapchat.data.repository.SnapRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnapSendViewModel(
    private val snapRepository: SnapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnapSendUiState())
    val uiState: StateFlow<SnapSendUiState> = _uiState.asStateFlow()

    val imageUri = snapRepository.image.uri

    private val caption
        get() = _uiState.value.caption

    fun onCaptionChange(newCaption: String) {
        if (caption.length <= 512) {
            _uiState.update { it.copy(caption = newCaption) }
        }
    }

    fun onImageSend(navigateToChat: () -> Unit) {
        viewModelScope.launch {
            try {
                snapRepository.sendSnap(caption = caption)
                navigateToChat()
            }
            /* TODO: catch exceptions in messageRepository */
            catch (e: FirebaseFirestoreException) {
                Log.e("SEND_MESSAGE", "${e.code }: ${e.message}")
            }
        }
    }
}

data class SnapSendUiState(
    val caption: String = ""
)