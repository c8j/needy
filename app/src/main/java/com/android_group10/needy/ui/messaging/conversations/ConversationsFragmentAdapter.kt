package com.android_group10.needy.ui.messaging.conversations

import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import android.widget.Toast
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingConversationBinding
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItemDiffCallback
import com.android_group10.needy.messaging.util.FirebaseUtil
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.android_group10.needy.ui.messaging.MessagingFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

class ConversationsFragmentAdapter(private val pronoun: String) :
    ListAdapter<ConversationQueryItem, ConversationsFragmentAdapter.ConversationItemViewHolder>(
        object :
            ConversationQueryItemDiffCallback() {}) {

    inner class ConversationItemViewHolder(private val binding: ItemMessagingConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var conversationQueryItem: ConversationQueryItem
        private lateinit var partnerUID: String

        init {

            //Set-up listener to open the chat window when clicking on a conversation
            itemView.setOnClickListener {
                val action =
                    conversationQueryItem.item.userNameMap[partnerUID]?.let { partnerFullName ->
                        MessagingFragmentDirections.actionMessagingToChat(
                            conversationQueryItem.id,
                            partnerFullName
                        )
                    }
                if (action != null) {
                    it.findNavController().navigate(action)
                }
            }

            //Set-up listener to open the popup menu
            itemView.setOnCreateContextMenuListener { menu, v, _ ->
                val inflater = MenuInflater(v.context)
                inflater.inflate(R.menu.messaging_conversation, menu)
                menu.getItem(0).setOnMenuItemClickListener {
                    //Archive option
                    FirestoreUtil.updateConversationStatus(
                        conversationQueryItem.id,
                        true
                    ) { wasSuccessful ->
                        if (wasSuccessful) {
                            Toast.makeText(
                                v.context,
                                "Conversation archived.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                v.context,
                                "Error occurred when trying to archive conversation.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    true
                }
                menu.getItem(1).setOnMenuItemClickListener {
                    //Block option
                    FirestoreUtil.addToBlockList(
                        conversationQueryItem.item.associatedPostUID,
                        partnerUID
                    ) { _, message ->
                        Toast.makeText(
                            v.context,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    true
                }
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
                var latestMessageText = conversationQueryItem.item.latestMessage
                conversationQueryItem.item.latestMessageSenderUID.let { uid ->
                    if (uid != "" && uid == currentUserUID) {
                        latestMessageText = "$pronoun: $latestMessageText"
                    }
                }
                tvMessagePreview.text = latestMessageText
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