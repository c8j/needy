package com.android_group10.needy.messaging.data.message

import androidx.recyclerview.widget.DiffUtil

open class ChatMessageQueryItemDiffCallback : DiffUtil.ItemCallback<ChatMessageQueryItem>() {
    override fun areItemsTheSame(oldItem: ChatMessageQueryItem, newItem: ChatMessageQueryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatMessageQueryItem, newItem: ChatMessageQueryItem): Boolean {
        return oldItem.item == newItem.item
    }
}