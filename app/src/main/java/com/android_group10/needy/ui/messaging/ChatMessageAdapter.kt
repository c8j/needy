package com.android_group10.needy.ui.messaging

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingMessageBinding
import com.android_group10.needy.ui.messaging.data.Message
import com.google.android.material.color.MaterialColors
import com.google.firebase.auth.FirebaseAuth
import java.time.ZoneId
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.*

class ChatMessageAdapter(private val messageList: List<Message>) :
    RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder>() {

    inner class ChatMessageViewHolder(private val binding: ItemMessagingMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.apply {
                tvMessage.text = message.text
                tvTimestamp.text = getLocalTimestamp(message)
                setMessageSide(message)
            }
        }

        private fun getLocalTimestamp(message: Message): String {
            val locale = Locale.ENGLISH
            var shortPattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                FormatStyle.SHORT,
                null,
                IsoChronology.INSTANCE,
                locale
            )

            if (shortPattern.contains("yy") && !shortPattern.contains("yyy")) {
                shortPattern = shortPattern.replace("yy", "yyyy")
            }

            val localZonedDateTime = message.timestamp.withZoneSameInstant(ZoneId.systemDefault())
            return localZonedDateTime.format(
                DateTimeFormatter.ofPattern(shortPattern, locale)
            )
        }

        private fun setMessageSide(message: Message){
            if (message.senderUid == FirebaseAuth.getInstance().currentUser?.uid){
                binding.messageLayout.apply {
                    background.setTint(MaterialColors.getColor(binding.root, R.attr.colorPrimary))
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.END)
                }
                binding.tvMessage.setTextColor(MaterialColors.getColor(binding.root, R.attr.colorOnPrimary))
                binding.tvTimestamp.apply {
                    setTextColor(context.resources.getColorStateList(R.color.selector_conversations_timestamp_user, context.theme))
                }
            } else {
                binding.messageLayout.apply {
                    background.setTint(MaterialColors.getColor(binding.root, R.attr.colorSurface))
                    layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.START)
                }
                binding.tvMessage.setTextColor(MaterialColors.getColor(binding.root, R.attr.colorOnSurface))
                binding.tvTimestamp.apply {
                    setTextColor(context.resources.getColorStateList(R.color.selector_conversations_timestamp_partner, context.theme))
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
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size
}