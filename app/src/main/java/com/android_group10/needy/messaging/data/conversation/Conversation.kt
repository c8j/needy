package com.android_group10.needy.messaging.data.conversation

import com.android_group10.needy.messaging.data.message.ChatMessage

data class Conversation(
    val associatedPostUID: String,
    val associatedPostDescription: String,
    val userUIDs: List<String>,
    val userNameMap: MutableMap<String, String>,
    val latestMessage: ChatMessage,
    val unread: Boolean,
    val concluded: Boolean
) {
    constructor() :
            this(
                "",
                "",
                listOf(),
                mutableMapOf(),
                ChatMessage(),
                false,
                false
            )
}
