package com.example.snapchat.data.repository.firebase

import com.example.snapchat.data.model.User
import com.example.snapchat.data.model.asExternalModel
import com.example.snapchat.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : UserRepository {

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

        db.collection(USERS_COLLECTION)
            .document(auth.currentUser!!.uid)
            .set(hashMapOf(
                USERS_FIELD_USERNAME to username
            )
        ).await()
    }

    override fun signOutUser() {
        auth.signOut()
    }

    companion object {
        // This data is duplicated in @FirebaseMessageRepository.
        // Ideally, it should be in one same place.
        private const val USERS_COLLECTION = "users"
        private const val USERS_FIELD_USERNAME = "username"
    }
}