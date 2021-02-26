package com.android_group10.needy.ui.messaging.data

import java.time.ZonedDateTime

data class Message(
    val senderUid: String,
    val text: String,
    val timestamp: ZonedDateTime
)
