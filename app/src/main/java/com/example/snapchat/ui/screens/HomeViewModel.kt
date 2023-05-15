package com.example.snapchat.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel(

) : ViewModel() {

    private val _permissionsQueueState = MutableStateFlow(mutableListOf<String>())
    val permissionsQueueState: StateFlow<MutableList<String>> = _permissionsQueueState.asStateFlow()

    private val permissionsQueue
        get() = _permissionsQueueState.value

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
                    it.toMutableList().apply {
                        this.add(permission)
                    }
                }
            }
        }
    }

    fun onPermissionDialogDismiss() {
        _permissionsQueueState.update {
            it.toMutableList().apply {
                this.removeFirst()
            }
        }
    }
}