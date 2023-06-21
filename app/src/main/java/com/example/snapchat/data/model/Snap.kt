package com.example.snapchat.data.model

import com.example.snapchat.utilities.toLocalDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude

sealed class Snap {
    abstract val id: String
    abstract val sentAt: Timestamp

    @Exclude fun getKey() = sentAt.toLocalDate()

    data class New(
        @DocumentId override val id: String = "",
        val caption: String = "",
        val senderId: String = "",
        override val sentAt: Timestamp = Timestamp.now()
    ) : Snap()

    data class Detailed(
        @DocumentId override val id: String = "",
        val caption: String = "",
        val image: Image,
        override val sentAt: Timestamp
    ) : Snap()

    data class Received(
        @DocumentId override val id: String,
        val sender: User,
        val isViewed: Boolean,
        override val sentAt: Timestamp
    ) : Snap()

    data class Sent(
        @DocumentId override val id: String,
        override val sentAt: Timestamp
    ) : Snap()
}




