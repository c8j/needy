package com.android_group10.needy.messaging.data.conversation

import androidx.recyclerview.widget.DiffUtil

open class ConversationQueryItemDiffCallback : DiffUtil.ItemCallback<ConversationQueryItem>() {
    override fun areItemsTheSame(oldItem: ConversationQueryItem, newItem: ConversationQueryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ConversationQueryItem, newItem: ConversationQueryItem): Boolean {
        return oldItem.item == newItem.item
    }
}