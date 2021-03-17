package com.android_group10.needy

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.messaging.util.FirebaseUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class PostAdapter(
    private val context: Context,
    private var detailList: List<Post>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val lineView1 : TextView = itemView.findViewById(R.id.text_view_1)
        val photoView1 : ImageView = itemView.findViewById(R.id.image_view)
        val starsView : RatingBar = itemView.findViewById(R.id.rating_star)
        val postContainerLayout : RelativeLayout = itemView.findViewById(R.id.postItemRelativeLayout)
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
        } else{
            holder.postContainerLayout.setBackgroundColor(0xFFffffff.toInt())
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
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentItem.authorUID).get().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("firebase", "Error getting data", task.exception)
            } else {
                @Suppress("UNCHECKED_CAST") val authorObject =
                    (task.result?.value as HashMap<*, *>)
                if (authorObject["authorRating"] != null) {
                    val authRating = String.format(
                        Locale.getDefault(), "%s",
                        authorObject["authorRating"])
                    holder.starsView.rating = authRating.toFloat()
                }
            }
        }
    }

    fun updateData(newList: List<Post>){
        detailList = newList;
        notifyDataSetChanged();
    }

}