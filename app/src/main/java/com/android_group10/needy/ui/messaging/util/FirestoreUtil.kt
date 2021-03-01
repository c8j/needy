package com.android_group10.needy.ui.messaging.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android_group10.needy.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val currentUserMessagingDataRef: DocumentReference
        get() = firestoreInstance.document(
            "userMessagingData/${FirebaseAuth.getInstance().currentUser!!.uid}"
        )

    private const val FIRESTORE_LOG_TAG = "FIRESTORE"

    private fun isUserNull(context: Context): Boolean {
        return if (FirebaseAuth.getInstance().currentUser != null) {
            true
        } else {
            Toast.makeText(
                context,
                "ERROR: Unable to access messaging service. No user logged in.",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }

    /*private fun createConversation(context: Context) {
        if (!isNullReference(context)) {

        }
    }*/

    fun createRequest(context: Context, post: Post) {
        if (!isUserNull(context)) {
            val requestQuery = currentUserMessagingDataRef.collection("messaging.requests")
                .whereEqualTo("associatedPostUID", post.postUID)
                .whereEqualTo("senderUID", FirebaseAuth.getInstance().currentUser!!)

            requestQuery.get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    FirebaseFirestore.getInstance()
                        .collection("userMessagingData/${post.authorUID}/messaging.requests").add(
                            mutableMapOf(
                                "associatedPostUID" to post.postUID,
                                "senderUID" to FirebaseAuth.getInstance().currentUser!!
                            )
                        ).addOnSuccessListener {
                            Log.d(FIRESTORE_LOG_TAG, "Request successfully added")
                            Toast.makeText(context, "Request sent!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Log.e(
                                FIRESTORE_LOG_TAG,
                                "Error occurred when trying to create request.",
                                it
                            )
                        }
                }
            }.addOnFailureListener {
                Log.e(FIRESTORE_LOG_TAG, "Error occurred when trying to query database.", it)
            }
        }
    }
}