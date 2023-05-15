package com.example.snapchat.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.snapchat.SnapchatApplication
import com.example.snapchat.ui.screens.HomeViewModel
import com.example.snapchat.ui.screens.auth.SignInViewModel
import com.example.snapchat.ui.screens.auth.SignUpViewModel
import com.example.snapchat.ui.screens.camera.CameraViewModel
import com.example.snapchat.ui.screens.camera.ImagePreviewViewModel
import com.example.snapchat.ui.screens.profile.ProfileViewModel
import com.example.snapchat.ui.screens.splash.SplashViewModel

object ModelViewProvider {
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(
                authRepository = application().container.authRepository
            )
        }

        initializer {
            SignInViewModel(
                authRepository = application().container.authRepository
            )

        }

        initializer {
            SignUpViewModel(
                authRepository = application().container.authRepository
            )
        }

        initializer {
            HomeViewModel()
        }

        initializer {
            CameraViewModel(
                imageRepository = application().container.imageRepository
            )
        }

        initializer {
            ImagePreviewViewModel(
                imageRepository = application().container.imageRepository
            )
        }

        initializer {
            ProfileViewModel(
                authRepository = application().container.authRepository
            )
        }
    }
}

fun CreationExtras.application(): SnapchatApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SnapchatApplication)