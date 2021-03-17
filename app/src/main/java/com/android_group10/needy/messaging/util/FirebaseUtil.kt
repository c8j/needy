package com.android_group10.needy.messaging.util

import android.net.Uri
import android.util.Log
import com.android_group10.needy.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseUtil {
    private const val FIREBASE_DATABASE_LOG_TAG = "FIREBASE_DB"
    private const val FIREBASE_STORAGE_LOG_TAG = "FIREBASE_STORAGE"

    private val firebaseDBReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val firebaseStorageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    fun getUser(userUID: String, onComplete: (User?) -> Unit){
        firebaseDBReference.child("Users/$userUID").get().addOnSuccessListener { dataSnapshot ->
            onComplete(dataSnapshot.getValue<User>())
        }.addOnFailureListener {
            Log.e(FIREBASE_DATABASE_LOG_TAG, "Failed to retrieve user profile from database.", it)
        }
    }

    fun getUserPictureURI(userUID: String, onComplete: (Uri?) -> Unit) {
        firebaseStorageReference.child("profile_images/$userUID/profile_pic").downloadUrl.addOnSuccessListener {
            onComplete(it)
        }.addOnFailureListener {
            Log.e(FIREBASE_STORAGE_LOG_TAG, "Error when retrieving user profile image", it)
            onComplete(null)
        }
    }


}