package com.android_group10.needy.messaging.util.liveData

import android.util.Log
import androidx.lifecycle.LiveData
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class FirestoreConversationLiveData(private val documentRef: DocumentReference) : LiveData<Conversation>(),
    EventListener<DocumentSnapshot> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = documentRef.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
        error?.let {
            Log.e(FirestoreUtil.FIRESTORE_LOG_TAG, "Error occurred on conversation listener.", error)
        }

        value?.let { documentSnapshot ->
            documentSnapshot.toObject<Conversation>()?.let {
                setValue(it)
            }
        }
    }
}