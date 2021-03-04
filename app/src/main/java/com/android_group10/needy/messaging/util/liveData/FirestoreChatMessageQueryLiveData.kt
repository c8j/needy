package com.android_group10.needy.messaging.util.liveData

import android.util.Log
import androidx.lifecycle.LiveData
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.message.ChatMessage
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class FirestoreChatMessageQueryLiveData(private val query: Query) : LiveData<List<ChatMessageQueryItem>>(),
    EventListener<QuerySnapshot> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        listenerRegistration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        listenerRegistration?.remove()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        error?.let {
            Log.e(FirestoreUtil.FIRESTORE_LOG_TAG, "Error occurred on chat messages listener", error)
        }

        value?.documents?.let { documentList: MutableList<DocumentSnapshot> ->
            val chatMessageQueryItemList = mutableListOf<ChatMessageQueryItem>()
            documentList.forEach {
                it.toObject<ChatMessage>()?.apply {
                    chatMessageQueryItemList.add(ChatMessageQueryItem(this, it.id))
                }
            }
            setValue(chatMessageQueryItemList)
        }
    }
}