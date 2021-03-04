package com.android_group10.needy.ui.messaging.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingMessageBinding
import com.android_group10.needy.messaging.data.message.ChatMessage
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItemDiffCallback
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.FirebaseAuth
import java.time.Instant
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

class ChatMessageAdapter :
    ListAdapter<ChatMessageQueryItem, ChatMessageAdapter.ChatMessageViewHolder>(object :
        ChatMessageQueryItemDiffCallback() {}) {

    inner class ChatMessageViewHolder(private val binding: ItemMessagingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatMessageQueryItem: ChatMessageQueryItem) {
            binding.apply {
                tvMessage.text = chatMessageQueryItem.item.text
                tvTimestamp.text = getLocalTimestamp(chatMessageQueryItem.item)
                setMessageSide(chatMessageQueryItem.item)
            }
        }

        private fun getLocalTimestamp(chatMessage: ChatMessage): String {
            var shortPattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                FormatStyle.SHORT,
                FormatStyle.SHORT,
                IsoChronology.INSTANCE,
                Locale.getDefault(Locale.Category.FORMAT)
            )

            if (shortPattern.contains("yy") && !shortPattern.contains("yyy")) {
                shortPattern = shortPattern.replace("yy", "yyyy")
            }

            val timestamp = chatMessage.timestamp
            val localZonedDateTime =
                Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong()).atZone(
                    ZoneId.systemDefault()
                )
            return localZonedDateTime.format(
                DateTimeFormatter.ofPattern(shortPattern)
            )
        }

        private fun setMessageSide(chatMessage: ChatMessage) {
            FirestoreUtil.firebaseAuthInstance.currentUser?.let {
                val currentUserUID = it.uid
                if (chatMessage.senderUid == currentUserUID) {
                    binding.messageLayout.apply {
                        background.setTint(
                            MaterialColors.getColor(
                                binding.root,
                                R.attr.colorSurface
                            )
                        )
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.END
                        )
                    }
                    binding.tvMessage.setTextColor(
                        MaterialColors.getColor(
                            binding.root,
                            R.attr.colorOnSurface
                        )
                    )
                    binding.tvTimestamp.apply {
                        setTextColor(
                            context.resources.getColorStateList(
                                R.color.selector_conversations_timestamp_user,
                                context.theme
                            )
                        )
                    }
                } else {
                    binding.messageLayout.apply {
                        background.setTint(
                            MaterialColors.getColor(
                                binding.root,
                                R.attr.colorPrimary
                            )
                        )
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            FrameLayout.LayoutParams.WRAP_CONTENT,
                            Gravity.START
                        )
                    }
                    binding.tvMessage.setTextColor(
                        MaterialColors.getColor(
                            binding.root,
                            R.attr.colorOnPrimary
                        )
                    )
                    binding.tvTimestamp.apply {
                        setTextColor(
                            context.resources.getColorStateList(
                                R.color.selector_conversations_timestamp_partner,
                                context.theme
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val binding =
            ItemMessagingMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}