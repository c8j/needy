package com.android_group10.needy.ui.messaging.conversations

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingConversationBinding
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItemDiffCallback
import com.android_group10.needy.messaging.util.ChatConstants
import com.android_group10.needy.messaging.util.FirebaseUtil
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.android_group10.needy.ui.messaging.chat.ChatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ConversationsFragmentAdapter :
    ListAdapter<ConversationQueryItem, ConversationsFragmentAdapter.ConversationItemViewHolder>(
        object :
            ConversationQueryItemDiffCallback() {}) {

    inner class ConversationItemViewHolder(private val binding: ItemMessagingConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var conversationQueryItem: ConversationQueryItem
        private lateinit var partnerUID: String

        init {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ChatActivity::class.java)
                intent.putExtra(ChatConstants.CONVERSATION_UID, conversationQueryItem.id)
                intent.putExtra(ChatConstants.PARTNER_UID, partnerUID)
                intent.putExtra(ChatConstants.PARTNER_FULL_NAME, conversationQueryItem.item.userNameMap[partnerUID])
                itemView.context.startActivity(intent)
            }
        }

        fun bind(conversationQueryItem: ConversationQueryItem) {
            this.conversationQueryItem = conversationQueryItem
            val currentUserUID = FirestoreUtil.firebaseAuthInstance.currentUser!!.uid
            this.conversationQueryItem.item.userUIDs.forEach { uid ->
                if (uid != currentUserUID) {
                    partnerUID = uid
                }
            }

            binding.apply {
                tvAssociatedPostTitle.text = conversationQueryItem.item.associatedPostDescription
                tvContactName.text = conversationQueryItem.item.userNameMap[partnerUID]
                tvMessagePreview.text = conversationQueryItem.item.latestMessage
                FirebaseUtil.getUserPictureURI(partnerUID) { uri ->
                    uri?.let {
                        Glide.with(itemView.context).load(it).apply(
                            RequestOptions()
                                .placeholder(R.drawable.anonymous_mask)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        ).into(ivConversationAvatar)
                    }
                }
            }
        }

        fun clearImageView() {
            binding.apply {
                Glide.with(itemView.context).clear(ivConversationAvatar)
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
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: ConversationItemViewHolder) {
        holder.clearImageView()
    }
}