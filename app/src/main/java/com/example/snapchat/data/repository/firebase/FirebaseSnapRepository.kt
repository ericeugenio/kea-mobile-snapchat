package com.example.snapchat.data.repository.firebase

import android.net.Uri
import android.util.Log
import com.example.snapchat.data.model.Image
import com.example.snapchat.data.model.Snap
import com.example.snapchat.data.model.User
import com.example.snapchat.data.repository.UserRepository
import com.example.snapchat.data.repository.SnapRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class FirebaseSnapRepository(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val userRepository: UserRepository
) : SnapRepository {

    override lateinit var image: Image

    override fun getSnaps(): Flow<List<Snap>> {
        val currentUserId = userRepository.currentUser!!.id

        return db.collection(SNAPS_COLLECTION)
            .orderBy(SNAPS_FIELD_TIMESTAMP)
            .snapshots().map { snapshots ->
                snapshots.map { snapshot ->
                    if (snapshot.data[SNAPS_FIELD_SENDER_ID] == currentUserId) {
                        Snap.Sent(
                            id = snapshot.id,
                            sentAt = snapshot.data[SNAPS_FIELD_TIMESTAMP] as Timestamp
                        )
                    }
                    else {
                        val viewerSnapshot = db.collection(SNAPS_COLLECTION)
                            .document(snapshot.id)
                            .collection(VIEWERS_COLLECTION)
                            .document(currentUserId)
                            .get().await()

                        val senderSnapshot = db.collection(USERS_COLLECTION)
                            .document(snapshot.data[SNAPS_FIELD_SENDER_ID] as String)
                            .get().await()

                        Snap.Received(
                            id = snapshot.id,
                            isViewed = viewerSnapshot.exists(),
                            sender = User(
                                id = senderSnapshot.id,
                                username = senderSnapshot.data?.get(USERS_FIELD_USERNAME) as String? ?: "guest_user"
                            ),
                            sentAt = snapshot.data[SNAPS_FIELD_TIMESTAMP] as Timestamp
                        )
                    }
                }
            }
    }

    override suspend fun sendSnap(caption: String) {
        val snap = Snap.New(
            caption = caption,
            senderId = userRepository.currentUser!!.id
        )

        val snapId = db.collection(SNAPS_COLLECTION).add(snap).await().id
        val snapImageRef = storage.reference.child("${SNAPS_IMAGES_PATH}${snapId}.jpg")
        snapImageRef.putFile(image.uri!!).await()
    }

    override suspend fun getSnap(id: String): Snap {
        val snapshot = db.collection(SNAPS_COLLECTION)
            .document(id)
            .get().await()

        val snapImageRef = storage.reference.child("${SNAPS_IMAGES_PATH}${id}.jpg")
        val file = withContext(Dispatchers.IO) {
            File.createTempFile("temp", ".jpg")
        }
        snapImageRef.getFile(file).await()


        // Notify db message has been seen by the current user.
        // ----------------------------------------------------------------------------------
        // It should be done in the backend with Cloud Functions, but is out of the scope
        // of this course.
        db.collection(SNAPS_COLLECTION)
            .document(id)
            .collection(VIEWERS_COLLECTION)
            .document(userRepository.currentUser!!.id)
            .set(hashMapOf(
                VIEWERS_FIELD_TIMESTAMP to Timestamp(Date())
            ))

        return Snap.Detailed(
            id = id,
            caption = snapshot.data?.get(SNAPS_FIELD_CAPTION) as String? ?: "",
            image = Image(uri = Uri.fromFile(file)),
            sentAt = snapshot.data?.get(SNAPS_FIELD_TIMESTAMP) as Timestamp
        )
    }

    override suspend fun getViewersFromSnap(id: String): List<User> {
        val viewersSnapshot = db.collection(SNAPS_COLLECTION)
            .document(id)
            .collection(VIEWERS_COLLECTION)
            .get().await()

        return mutableListOf<User>().apply {
            for (snapshot in viewersSnapshot) {
                this.add(User(
                    id = snapshot.id,
                    username = snapshot.data[USERS_FIELD_USERNAME] as String
                ))
            }
        }
    }

    companion object {
        private const val SNAPS_COLLECTION = "snaps"
        private const val SNAPS_FIELD_SENDER_ID = "senderId"
        private const val SNAPS_FIELD_TIMESTAMP = "sentAt"
        private const val SNAPS_FIELD_CAPTION = "caption"
        private const val SNAPS_IMAGES_PATH = "images/snaps/"

        private const val VIEWERS_COLLECTION = "viewers"
        private const val VIEWERS_FIELD_TIMESTAMP = "viewedAt"

        private const val USERS_COLLECTION = "users"
        private const val USERS_FIELD_USERNAME = "username"
    }
}