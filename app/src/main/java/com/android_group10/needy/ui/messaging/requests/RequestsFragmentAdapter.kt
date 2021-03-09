package com.android_group10.needy.ui.messaging.requests

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingRequestBinding
import com.android_group10.needy.messaging.data.request.RequestQueryItem
import com.android_group10.needy.messaging.data.request.RequestQueryItemDiffCallback
import com.android_group10.needy.messaging.util.FirebaseUtil
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar

class RequestsFragmentAdapter :
    ListAdapter<RequestQueryItem, RequestsFragmentAdapter.RequestsViewHolder>(object :
        RequestQueryItemDiffCallback() {}) {

    inner class RequestsViewHolder(private val binding: ItemMessagingRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var requestQueryItem: RequestQueryItem

        init {
            binding.apply {

                //Set-up the listener to open the popup menu for a request item
                iBtnChoiceMenu.setOnClickListener { view ->
                    PopupMenu(view.context, view).apply {
                        inflate(R.menu.messaging_request)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.messaging_request_menu_accept -> {
                                    FirestoreUtil.acceptRequest(requestQueryItem) { _, message ->
                                        Toast.makeText(
                                            view.context,
                                            message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    true
                                }
                                R.id.messaging_request_menu_ignore -> {
                                    FirestoreUtil.removeRequest(requestQueryItem.id) { _, message ->
                                        Toast.makeText(
                                            view.context,
                                            message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    true
                                }
                                R.id.messaging_request_menu_block -> {
                                    FirestoreUtil.addToBlockList(
                                        requestQueryItem.item.associatedPostUID,
                                        requestQueryItem.item.senderUID
                                    ) { _, message ->
                                        Toast.makeText(
                                            view.context,
                                            message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    true
                                }
                                else -> false
                            }
                        }
                        show()
                    }
                }
            }
        }

        fun bind(requestQueryItem: RequestQueryItem) {
            this.requestQueryItem = requestQueryItem
            binding.apply {
                tvAssociatedPostTitle.text = requestQueryItem.item.associatedPostDescription
                tvContactName.text = requestQueryItem.item.senderFullName
                FirebaseUtil.getUserPictureURI(requestQueryItem.item.senderUID) { uri ->
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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestsFragmentAdapter.RequestsViewHolder {
        val binding =
            ItemMessagingRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewRecycled(holder: RequestsViewHolder) {
        holder.clearImageView()
    }
}