package com.example.snapchat.data

import com.example.snapchat.data.repository.firebase.FirebaseUserRepository
import com.example.snapchat.data.repository.UserRepository
import com.example.snapchat.data.repository.firebase.FirebaseSnapRepository
import com.example.snapchat.data.repository.SnapRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val userRepository: UserRepository
    val snapRepository: SnapRepository
}

class DefaultAppContainer : AppContainer {
    override val userRepository: UserRepository by lazy {
        FirebaseUserRepository(
            Firebase.auth,
            Firebase.firestore
        )
    }

    override val snapRepository: SnapRepository by lazy {
        FirebaseSnapRepository(
            Firebase.firestore,
            Firebase.storage,
            userRepository
        )
    }
}