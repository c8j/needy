package com.android_group10.needy.messaging.data.request

data class Request(
    val associatedPostUID: String,
    val senderUID: String,
    val associatedPostDescription: String,
    val senderFullName: String
) {
    constructor() : this("", "", "", "")
}
