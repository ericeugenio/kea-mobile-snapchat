package com.example.snapchat.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.AuthRepository

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun onSignOut(navigateToSignIn: () -> Unit) {
        authRepository.signOutUser()
        navigateToSignIn()
    }
}