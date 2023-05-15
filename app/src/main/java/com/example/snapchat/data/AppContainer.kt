package com.example.snapchat.data

import com.example.snapchat.data.repository.FirebaseAuthRepository
import com.example.snapchat.data.repository.AuthRepository
import com.example.snapchat.data.repository.FirebaseImageRepository
import com.example.snapchat.data.repository.ImageRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val authRepository: AuthRepository
    val imageRepository: ImageRepository
}

class DefaultAppContainer() : AppContainer {
    override val authRepository: AuthRepository by lazy {
        FirebaseAuthRepository(Firebase.auth)
    }

    override val imageRepository: ImageRepository by lazy {
        FirebaseImageRepository()
    }
}