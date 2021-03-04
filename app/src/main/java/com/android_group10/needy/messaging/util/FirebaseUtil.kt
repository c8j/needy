package com.android_group10.needy.messaging.util

import android.net.Uri
import android.util.Log
import com.android_group10.needy.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseUtil {
    const val FIREBASE_DATABASE_LOG_TAG = "FIREBASE_DB"
    val FIREBASE_STORAGE_LOG_TAG = "FIREBASE_STORAGE"

    //private lateinit var userDatabaseListener: ChildEventListener

    private val firebaseDBReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val firebaseStorageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }

    /* Removed since this would imply that EVERY user updates the database whenever ANY user changes info
    @JvmStatic
    fun addRequestsUserNameListener() {
        userDatabaseListener = object : ChildEventListener{
            private fun updateRequests(snapshot: DataSnapshot){
                snapshot.key?.let { userUID ->
                    snapshot.getValue<User>()?.let { user ->
                        val userFullName = "${user.firstName} ${user.lastName}"
                        FirestoreUtil.updateRequests(userUID, userFullName)
                    }
                }
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                updateRequests(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                updateRequests(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                snapshot.key?.let { userUID ->
                    FirestoreUtil.removeRequestsFromUser(userUID)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(FIREBASE_DATABASE_LOG_TAG, "Users listener failed", error.toException())
            }
        }
        firebaseDBReference.child("Users").addChildEventListener(userDatabaseListener)
    }

    @JvmStatic
    fun removeRequestsUserNameListener() {
        firebaseDBReference.child("Users").removeEventListener(userDatabaseListener)
    }
    */

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