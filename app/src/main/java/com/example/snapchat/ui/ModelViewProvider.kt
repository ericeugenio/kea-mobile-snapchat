package com.example.snapchat.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.snapchat.SnapchatApplication
import com.example.snapchat.ui.screens.chat.ChatViewModel
import com.example.snapchat.ui.screens.auth.SignInViewModel
import com.example.snapchat.ui.screens.auth.SignUpViewModel
import com.example.snapchat.ui.screens.chat.snap.SnapTakeViewModel
import com.example.snapchat.ui.screens.chat.snap.SnapSendViewModel
import com.example.snapchat.ui.screens.auth.ProfileViewModel
import com.example.snapchat.ui.screens.chat.snap.SnapPreviewViewModel
import com.example.snapchat.ui.screens.splash.SplashViewModel

object ModelViewProvider {
    val Factory = viewModelFactory {
        initializer {
            SplashViewModel(userRepository = application().container.userRepository)
        }

        initializer {
            SignInViewModel(userRepository = application().container.userRepository)
        }

        initializer {
            SignUpViewModel(userRepository = application().container.userRepository)
        }

        initializer {
            ChatViewModel(snapRepository = application().container.snapRepository)
        }

        initializer {
            SnapTakeViewModel(snapRepository = application().container.snapRepository)
        }

        initializer {
            SnapSendViewModel(snapRepository = application().container.snapRepository)
        }

        initializer {
            SnapPreviewViewModel( snapRepository = application().container.snapRepository)
        }

        initializer {
            ProfileViewModel(userRepository = application().container.userRepository)
        }
    }
}

fun CreationExtras.application(): SnapchatApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as SnapchatApplication)