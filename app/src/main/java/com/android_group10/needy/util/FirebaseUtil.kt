package com.android_group10.needy.util

import android.net.Uri
import android.util.Log
import com.android_group10.needy.Post
import com.android_group10.needy.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseUtil {
    const val FIREBASE_DATABASE_LOG_TAG = "FIREBASE_DB"
    const val FIREBASE_STORAGE_LOG_TAG = "FIREBASE_STORAGE"

    private val firebaseDBReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val firebaseStorageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    private fun getPost(postUID: String, onRetrieved: (post: Post) -> Unit) {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.getValue<Post>()?.let(onRetrieved)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    FIREBASE_DATABASE_LOG_TAG,
                    "Error when retrieving request post information",
                    error.toException()
                )
            }
        }
        firebaseDBReference.child("Posts/$postUID").addListenerForSingleValueEvent(objectListener)
    }

    private fun getUser(userUID: String, onRetrieved: (user: User) -> Unit) {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.getValue<User>()?.let(onRetrieved)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(
                    FIREBASE_DATABASE_LOG_TAG,
                    "Error when retrieving request user information",
                    error.toException()
                )
            }
        }
        firebaseDBReference.child("Users/$userUID").addListenerForSingleValueEvent(objectListener)
    }

    fun getUserPictureURI(userUID: String, onComplete: (Uri?, Exception?) -> Unit) {
        firebaseStorageReference.child("profile_images/$userUID/profile_pic").downloadUrl.addOnSuccessListener {
            onComplete(it, null)
        }.addOnFailureListener {
            Log.e(FIREBASE_STORAGE_LOG_TAG, "Error when retrieving user profile image", it)
            onComplete(null, it)
        }
    }

    fun getPostAndUser(
        postUID: String,
        userUID: String,
        onRetrieved: (postUserPair: Pair<Post, User>) -> Unit
    ) {
        getPost(postUID) { post ->
            getUser(userUID) { user ->
                onRetrieved(Pair(post, user))
            }
        }

    }
}