package com.example.snapchat.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.snapchat.data.repository.AuthRepository

class SplashViewModel (
    private val authRepository: AuthRepository
) : ViewModel() {

    fun onAppStart(
        navigateToHome: () -> Unit,
        navigateToSignIn: () -> Unit
    ) {
        /* TODO: add necessary configurations */

        if (authRepository.isUserLogged) navigateToHome()
        else navigateToSignIn()
    }

    companion object {
        const val SPLASH_DELAY = 1000L
    }
}