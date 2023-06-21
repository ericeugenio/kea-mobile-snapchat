package com.example.snapchat.data.repository

import com.example.snapchat.data.model.User

interface UserRepository {
    val currentUser: User?
    val isUserLogged: Boolean

    suspend fun signInUser(email: String, password: String)
    suspend fun signUpUser(username: String, email: String, password: String)
    fun signOutUser()
}