package com.android_group10.needy

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.messaging.util.FirebaseUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PostAdapter(
    private val context: Context,
    private var detailList: List<Post>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val lineView1 : TextView = itemView.findViewById(R.id.text_view_1)
        val photoView1 : ImageView = itemView.findViewById(R.id.image_view)
        val postContainerLayout : RelativeLayout = itemView.findViewById(R.id.postItemRelativeLayout)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            Log.d("PostAdapter", "$position")
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.post_details,
            parent,
            false
        )
        return PostViewHolder(itemView)
    }

    override fun getItemCount() = detailList.size

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = detailList[position]
        holder.lineView1.text = currentItem.description

        //Setting the background of author's post:
        val currentUID = Firebase.auth.currentUser?.uid
        if(currentItem.authorUID == currentUID){
            holder.postContainerLayout.setBackgroundColor(0xFFf5fae0.toInt())
        }
        
        FirebaseUtil.getUserPictureURI(currentItem.authorUID) { uri ->
            uri?.let {
                Glide.with(context).load(it).apply(
                    RequestOptions()
                        .placeholder(R.drawable.anonymous_mask)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                ).into(holder.photoView1)
            }
        }
    }

    fun updateData(newList: List<Post>){
        detailList = newList;

        /*This could be further optimized to only update items that have changed
        instead of the whole list, take a look at DiffUtil
         */
        notifyDataSetChanged();
    }

}