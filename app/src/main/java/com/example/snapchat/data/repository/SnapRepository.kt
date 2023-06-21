package com.example.snapchat.data.repository

import com.example.snapchat.data.model.Image
import com.example.snapchat.data.model.Snap
import com.example.snapchat.data.model.User
import kotlinx.coroutines.flow.Flow

interface SnapRepository {
    var image: Image

    fun getSnaps(): Flow<List<Snap>>
    suspend fun sendSnap(caption: String)
    suspend fun getSnap(id: String): Snap
    suspend fun getViewersFromSnap(id: String): List<User>
}