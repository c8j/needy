package com.android_group10.needy.messaging.data.message

import com.google.firebase.Timestamp

data class ChatMessage(
    val senderUid: String,
    val text: String,
    val timestamp: Timestamp
) {
    constructor() : this("", "", Timestamp.now())
}
