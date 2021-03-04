package com.android_group10.needy.messaging.data.conversation

import com.android_group10.needy.messaging.data.QueryItem

data class ConversationQueryItem(private val _item: Conversation, private val _id: String) :
    QueryItem<Conversation> {
    override val item: Conversation
        get() = _item
    override val id: String
        get() = _id
}
