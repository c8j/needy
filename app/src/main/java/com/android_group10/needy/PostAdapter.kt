package com.android_group10.needy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private  val detailList: List<Post>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val lineView1 : TextView = itemView.findViewById(R.id.text_view_1)
        val photoView1 : ImageView = itemView.findViewById(R.id.image_view)
        //this functionality is work-in-progress. Pushed to show how it will look like
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_details, parent, false)

        return PostViewHolder(itemView)
    }

    override fun getItemCount() = detailList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = detailList[position]
        holder.lineView1.text = currentItem.description
        holder.photoView1.setImageResource(currentItem.user.image)
        //add stars here
    }
}