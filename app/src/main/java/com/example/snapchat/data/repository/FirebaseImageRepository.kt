package com.example.snapchat.data.repository

import android.net.Uri

class FirebaseImageRepository : ImageRepository {
    override var imageUri: Uri? = null

    override suspend fun uploadImage() {
        TODO("Not yet implemented")
    }

    override suspend fun downloadImage() {
        TODO("Not yet implemented")
    }
}