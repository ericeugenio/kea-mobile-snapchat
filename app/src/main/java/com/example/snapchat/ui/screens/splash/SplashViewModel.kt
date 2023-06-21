package com.example.snapchat.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.UserRepository

class SplashViewModel (
    private val userRepository: UserRepository
) : ViewModel() {

    fun onAppStart(
        navigateToChat: () -> Unit,
        navigateToSignIn: () -> Unit
    ) {
        if (userRepository.isUserLogged) navigateToChat()
        else navigateToSignIn()
    }

    companion object {
        const val SPLASH_DELAY = 1000L
    }
}