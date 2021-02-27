package com.android_group10.needy.ui.messaging.data

import com.google.firebase.Timestamp

data class Message(
    val senderUid: String,
    val text: String,
    val timestamp: Timestamp
) {
    constructor() : this("", "", Timestamp(0, 0))
}
