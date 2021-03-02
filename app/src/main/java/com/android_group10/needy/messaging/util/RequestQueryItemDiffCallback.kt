package com.android_group10.needy.messaging.util

import androidx.recyclerview.widget.DiffUtil
import com.android_group10.needy.messaging.data.RequestQueryItem

open class RequestQueryItemDiffCallback : DiffUtil.ItemCallback<RequestQueryItem>() {
    override fun areItemsTheSame(oldItem: RequestQueryItem, newItem: RequestQueryItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RequestQueryItem, newItem: RequestQueryItem): Boolean {
        return oldItem.item == newItem.item
    }
}