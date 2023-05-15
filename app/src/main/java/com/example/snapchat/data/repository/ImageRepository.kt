package com.example.snapchat.data.repository

import android.net.Uri

interface ImageRepository {
    var imageUri: Uri?

    suspend fun uploadImage()
    suspend fun downloadImage()
}