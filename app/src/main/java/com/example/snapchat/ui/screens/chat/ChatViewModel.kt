package com.example.snapchat.ui.screens.chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.snapchat.data.model.Image
import com.example.snapchat.data.model.Snap
import com.example.snapchat.data.repository.SnapRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.util.SortedMap

class ChatViewModel(
    private val snapRepository: SnapRepository
) : ViewModel() {

    val uiState: StateFlow<ChatUiState> = snapRepository.getSnaps()
        .map { messages ->
            ChatUiState(snaps = messages.groupBy { it.getKey() }.toSortedMap())
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ChatUiState(),
            started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT)
        )

    private val _permissionsQueueState = MutableStateFlow(PermissionsUiState())
    val permissionsQueueState: StateFlow<PermissionsUiState> = _permissionsQueueState.asStateFlow()

    private val permissionsQueue
        get() = _permissionsQueueState.value.permissionsQueue

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        onGranted: () -> Unit
    ) {
        if (isGranted) {
            onGranted()
        }
        else {
            if (!permissionsQueue.contains(permission)) {
                _permissionsQueueState.update {
                    it.copy(permissionsQueue = permissionsQueue.toMutableList().apply {
                        this.add(permission)
                    })
                }
            }
        }
    }

    fun onPermissionDialogDismiss() {
        _permissionsQueueState.update {
            it.copy(permissionsQueue = permissionsQueue.toMutableList().apply {
                this.removeFirst()
            })
        }
    }

    fun onImageSelected(
        imageUri: Uri?,
        navigateToSnapSend: () -> Unit
    ) {
        snapRepository.image = Image(uri = imageUri)
        navigateToSnapSend()
    }

    companion object {
        private const val DEFAULT_TIMEOUT = 5000L
    }
}

data class ChatUiState(
    val snaps: SortedMap<LocalDate, List<Snap>> = sortedMapOf()
)

data class PermissionsUiState(
    val permissionsQueue: MutableList<String> = mutableListOf()
)