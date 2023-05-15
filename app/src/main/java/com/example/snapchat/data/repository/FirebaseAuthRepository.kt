package com.example.snapchat.data.repository

import com.example.snapchat.data.model.User
import com.example.snapchat.data.model.asExternalModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(private val auth: FirebaseAuth) : AuthRepository {

    override val currentUser: User?
        get() = auth.currentUser?.asExternalModel()

    override val isUserLogged: Boolean
        get() = auth.currentUser != null

    override suspend fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUpUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)?.await()
    }

    override fun signOutUser() {
        auth.signOut()
    }
}