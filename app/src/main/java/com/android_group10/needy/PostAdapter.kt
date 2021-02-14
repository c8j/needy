package com.android_group10.needy

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android_group10.needy.ui.InNeed.OpenPostRecordFragment
import java.security.AccessController.getContext
import java.util.*

class PostAdapter(
    val detailList: List<Post>,
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
            Log.d("PostAdapter", "$position")
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
               // listener.onItemClick(position, detailList[position])
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        //fun onItemClick(position: Int,  currentPositionedPost: Post)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.post_details,
            parent,
            false
        )

        return PostViewHolder(itemView)
    }

    override fun getItemCount() = detailList.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = detailList[position]
        holder.lineView1.text = currentItem.description
        holder.photoView1.setImageResource(currentItem.user.image)
        //add stars here
     /*   holder.itemView.setOnClickListener {
            fun onClick(v: View) {
                val activity: AppCompatActivity = v.context as AppCompatActivity
                val newFragment = OpenPostRecordFragment()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_in_need_fragment, newFragment).addToBackStack(null)
                    .commit()
            }
        }*/
    }

}