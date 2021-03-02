package com.android_group10.needy.ui.messaging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.databinding.ItemMessagingConversationBinding
import com.android_group10.needy.messaging.data.Conversation

class MessagesFragmentAdapter(private val conversationList: List<Conversation>) :
    RecyclerView.Adapter<MessagesFragmentAdapter.ConversationItemViewHolder>() {

    inner class ConversationItemViewHolder(private val binding: ItemMessagingConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(conversation: Conversation) {
            binding.apply {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItemViewHolder {
        val binding = ItemMessagingConversationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ConversationItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationItemViewHolder, position: Int) {
        holder.bind(conversationList[position])
    }

    override fun getItemCount(): Int = conversationList.size
}