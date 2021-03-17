package com.android_group10.needy.ui.messaging.conversations

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConversationsFragmentAdapter(
    private val progressBarVisibility: (isVisible: Boolean) -> Unit,
    private val pronoun: String
) :
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
            itemView.setOnCreateContextMenuListener { menu, view, _ ->
                val inflater = MenuInflater(view.context)
                inflater.inflate(R.menu.messaging_conversation, menu)
                menu.getItem(0).setOnMenuItemClickListener {
                    //Archive option
                    MaterialAlertDialogBuilder(
                        view.context,
                        R.style.ThemeOverlay_Needy_MaterialAlertDialog
                    )
                        .setMessage(R.string.messaging_dialog_archive)
                        .setPositiveButton(R.string.messaging_dialog_confirm) { _, _ ->
                            progressBarVisibility(true)
                            FirestoreUtil.updateConversationStatus(
                                conversationQueryItem.id,
                                true
                            ) { wasSuccessful ->
                                progressBarVisibility(false)
                                if (wasSuccessful) {
                                    Toast.makeText(
                                        view.context,
                                        "Conversation archived.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        view.context,
                                        "Error occurred when trying to archive conversation.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        .setNeutralButton(R.string.messaging_dialog_cancel) { _, _ ->
                            //Do nothing
                        }
                        .show()
                    true
                }
                menu.getItem(1).setOnMenuItemClickListener {
                    //Block option
                    MaterialAlertDialogBuilder(
                        view.context,
                        R.style.ThemeOverlay_Needy_MaterialAlertDialog
                    )
                        .setMessage(R.string.messaging_dialog_block_conversations)
                        .setPositiveButton(R.string.messaging_dialog_confirm) { _, _ ->
                            progressBarVisibility(true)
                            FirestoreUtil.addToBlockList(
                                partnerUID
                            ) { _, message ->
                                progressBarVisibility(false)
                                Toast.makeText(
                                    view.context,
                                    message,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        .setNeutralButton(R.string.messaging_dialog_cancel) { _, _ ->
                            //Do nothing
                        }
                        .show()
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
                var latestMessageText = conversationQueryItem.item.latestMessage.text
                conversationQueryItem.item.apply {
                    this.latestMessage.senderUid.let { uid ->
                        if (uid != "") {
                            if (unread && uid != currentUserUID) {
                                tvMessagePreview.apply {
                                    setTypeface(typeface, Typeface.BOLD)
                                }
                            } else {
                                if (uid == currentUserUID) {
                                    latestMessageText = "$pronoun: $latestMessageText"
                                }
                                tvMessagePreview.apply {
                                    setTypeface(typeface, Typeface.NORMAL)
                                }
                            }

                        }
                    }
                }
                tvMessagePreview.text = latestMessageText

                FirebaseUtil.getUserPictureURI(partnerUID) { uri ->
                    if (uri != null) {
                        Glide.with(itemView.context).load(uri).apply(
                            RequestOptions()
                                .placeholder(R.drawable.anonymous_mask)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        ).into(ivConversationAvatar)
                    } else {
                        ivConversationAvatar.setImageResource(R.drawable.anonymous_mask)
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConversationsFragmentAdapter.ConversationItemViewHolder {
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