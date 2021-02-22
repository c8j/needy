package com.android_group10.needy.ui.messaging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.databinding.ItemConversationBinding

class ConversationsFragmentAdapter(private var conversationList: List<Conversation>) : RecyclerView.Adapter<ConversationsFragmentAdapter.ConversationItemViewHolder>() {
    inner class ConversationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItemViewHolder {
        context = parent.context
        val view = ItemConversationBinding.inflate(LayoutInflater.from(context), parent, false).root
        return ConversationItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationItemViewHolder, position: Int) {
        ItemConversationBinding.bind(holder.itemView).apply {
            tvAssociatedPostTitle.text = conversationList[position].associatedPost.description
            val contactName = conversationList[position].communicationPartner.firstName + ' ' + conversationList[position].communicationPartner.lastName
            tvContactName.text = contactName
        }
    }

    override fun getItemCount(): Int {
        return conversationList.size
    }
}