package com.example.snapchat.ui.screens.chat.snap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapchat.data.model.Snap
import com.example.snapchat.data.repository.SnapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SnapPreviewViewModel(
    private val snapRepository: SnapRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SnapPreviewUiState())
    val uiState: StateFlow<SnapPreviewUiState> = _uiState.asStateFlow()

    fun onInit(snapId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(snap = snapRepository.getSnap(snapId) as Snap.Detailed) }
        }
    }
}

data class SnapPreviewUiState(
    val snap: Snap.Detailed? = null
)