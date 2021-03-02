package com.android_group10.needy.messaging.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.android_group10.needy.Post
import com.android_group10.needy.messaging.data.RequestQueryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.DoubleConsumer

object FirestoreUtil {
    private const val FIRESTORE_LOG_TAG = "FIRESTORE"
    private const val REQUESTS_COLLECTION = "messaging.requests"

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val currentUserMessagingDataRef: DocumentReference
        get() = firestoreInstance.document(
            "userMessagingData/${FirebaseAuth.getInstance().currentUser!!.uid}"
        )

    private fun createRequest(post: Post, onComplete: (wasSuccessful: Boolean, message: String) -> Unit) {
        lateinit var completionMessage: String

        val postAuthorMessagingDataRequestsRef = FirebaseFirestore.getInstance()
            .collection("userMessagingData/${post.authorUID}/$REQUESTS_COLLECTION")

        val requestQuery = postAuthorMessagingDataRequestsRef
            .whereEqualTo("associatedPostUID", post.postUID)
            .whereEqualTo("senderUID", FirebaseAuth.getInstance().currentUser!!)

        requestQuery.get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                postAuthorMessagingDataRequestsRef
                    .add(
                        mutableMapOf(
                            "associatedPostUID" to post.postUID,
                            "senderUID" to FirebaseAuth.getInstance().currentUser!!
                        )
                    ).addOnSuccessListener {
                        completionMessage = "Request successfully added"
                        Log.d(FIRESTORE_LOG_TAG, completionMessage)
                        onComplete(true, completionMessage)
                    }
                    .addOnFailureListener {
                        completionMessage = "Error occurred when trying to create request."
                        Log.e(
                            FIRESTORE_LOG_TAG,
                            completionMessage,
                            it
                        )
                        onComplete(false, completionMessage)
                    }
            }
        }.addOnFailureListener {
            completionMessage = "Error occurred when trying to query database."
            Log.e(FIRESTORE_LOG_TAG, completionMessage, it)
        }
    }

    @JvmStatic
    fun createRequest(post: Post, onComplete: BiConsumer<Boolean, String>) = createRequest(post, onComplete::accept)

    fun getRequestsLiveData(): LiveData<List<RequestQueryItem>> {
        val query = currentUserMessagingDataRef.collection(REQUESTS_COLLECTION)
        return FirestoreRequestQueryLiveData(query)
    }
}