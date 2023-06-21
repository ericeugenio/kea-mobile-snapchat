package com.example.snapchat.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun onSignOut(navigateToSignIn: () -> Unit) {
        userRepository.signOutUser()
        navigateToSignIn()
    }
}