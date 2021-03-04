package com.android_group10.needy.messaging.data.message

import com.android_group10.needy.messaging.data.QueryItem

data class ChatMessageQueryItem(private val _item: ChatMessage, private val _id: String) :
    QueryItem<ChatMessage> {
    override val item: ChatMessage
        get() = _item
    override val id: String
        get() = _id
}
