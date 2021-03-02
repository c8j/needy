package com.android_group10.needy.ui.messaging

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.R
import com.android_group10.needy.databinding.ItemMessagingRequestBinding
import com.android_group10.needy.messaging.data.RequestQueryItem
import com.android_group10.needy.messaging.util.RequestQueryItemDiffCallback

class RequestsFragmentAdapter :
    ListAdapter<RequestQueryItem, RequestsFragmentAdapter.RequestsViewHolder>(object :
        RequestQueryItemDiffCallback() {}) {

    inner class RequestsViewHolder(private val binding: ItemMessagingRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                iBtnChoiceMenu.setOnClickListener { view ->
                    ContextThemeWrapper(view.context, R.style.RequestMenuVariant).also {
                        PopupMenu(it, view).apply {
                            inflate(R.menu.messaging_request)
                            setOnMenuItemClickListener { menuItem ->
                                when (menuItem.itemId) {
                                    R.id.messaging_request_menu_accept -> {
                                        //TODO: implement accept request
                                        Toast.makeText(
                                            view.context,
                                            "Request accepted",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        true
                                    }
                                    R.id.messaging_request_menu_ignore -> {
                                        //TODO: implement ignore request
                                        Toast.makeText(
                                            view.context,
                                            "Request ignored",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
        }

        fun bind(queryItem: RequestQueryItem) {
            binding.apply {
                tvAssociatedPostTitle.text = queryItem.item.associatedPostUID
                tvContactName.text = queryItem.item.senderUID
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val binding =
            ItemMessagingRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RequestsViewHolder, position: Int) {
        val queryItem = getItem(position)
        holder.bind(queryItem)
    }

}