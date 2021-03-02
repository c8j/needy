package com.android_group10.needy.messaging.data

data class Request(
    val associatedPostUID: String,
    val senderUID: String
) {
    constructor() : this("", "")
}
