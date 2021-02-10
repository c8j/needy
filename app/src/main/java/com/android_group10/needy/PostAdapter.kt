package com.android_group10.needy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(private  val detailList: List<Post>) : RecyclerView.Adapter<PostAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val lineView1 : TextView = itemView.findViewById(R.id.text_view_1)
        val photoView1 : ImageView = itemView.findViewById(R.id.image_view)
        //this functionality is work-in-progress. Pushed to show how it will look like

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_details, parent, false)

        return DetailsViewHolder(itemView)
    }

    override fun getItemCount() = detailList.size

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val currentItem = detailList[position]
        holder.lineView1.text = currentItem.description
        holder.photoView1.setImageResource(currentItem.user.image)

    }
}