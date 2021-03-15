package com.android_group10.needy.messaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil

class MessagingFragmentViewModel : ViewModel() {

    lateinit var conversationUID: String

    val requests by lazy {
        FirestoreUtil.getRequestsLiveData()
    }

    val requestsCounter: LiveData<Int> by lazy {
        Transformations.map(requests) {
            it.size
        }
    }

    val conversations by lazy {
        FirestoreUtil.getConversationsLiveData()
    }

    val unreadConversationsCounter: LiveData<Int> by lazy {
        Transformations.map(conversations) { conversationQueryItemList ->
            FirestoreUtil.firebaseAuthInstance.currentUser?.let { currentUser ->
                var unreadCounter = 0
                conversationQueryItemList.forEach {
                    if (it.item.unread && currentUser.uid != it.item.latestMessage.senderUid) {
                        unreadCounter++
                    }
                }
                unreadCounter
            }
        }
    }

    fun getConversation(): LiveData<Conversation> {
        return FirestoreUtil.getConversationLiveData(conversationUID)
    }

    fun retrieveMessages(): LiveData<List<ChatMessageQueryItem>> {
        return FirestoreUtil.getChatMessagesLiveData(conversationUID)
    }

    fun sendMessage(messageText: String, onComplete: (String?) -> Unit) {
        FirestoreUtil.sendMessage(conversationUID, messageText, onComplete)
    }

    fun markMessageAsRead() {
        FirestoreUtil.updateConversationReadStatus(conversationUID, false)
    }
}