package com.android_group10.needy.messaging.data.conversation

data class Conversation(
    val associatedPostUID: String,
    val associatedPostDescription: String,
    val userUIDs: List<String>,
    val userNameMap: MutableMap<String, String>,
    val latestMessage: String
) {
    constructor() : this("", "", listOf(), mutableMapOf(), "")
}
