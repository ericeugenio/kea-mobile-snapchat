package com.example.snapchat.data.model

import com.google.firebase.auth.FirebaseUser

class User(
    val id: String,
    val username: String,
    val email: String
)

fun FirebaseUser.asExternalModel() = User(
    id = uid,
    username = displayName!!,
    email = email!!
)
