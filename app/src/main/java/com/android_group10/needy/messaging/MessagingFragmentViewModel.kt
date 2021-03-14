package com.android_group10.needy.messaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.data.request.RequestQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil

class MessagingFragmentViewModel : ViewModel() {

    lateinit var conversationUID: String
    var requestsCounter = MutableLiveData<Int>()

    fun getRequests(): LiveData<List<RequestQueryItem>> {
        return FirestoreUtil.getRequestsLiveData()
    }

    fun getConversation(): LiveData<Conversation>{
        return FirestoreUtil.getConversationLiveData(conversationUID)
    }

    fun getConversations(): LiveData<List<ConversationQueryItem>> {
        return FirestoreUtil.getConversationsLiveData()
    }

    fun retrieveMessages(): LiveData<List<ChatMessageQueryItem>> {
        return FirestoreUtil.getChatMessagesLiveData(conversationUID)
    }

    fun sendMessage(messageText: String, onComplete: (String?) -> Unit) {
        FirestoreUtil.sendMessage(conversationUID, messageText, onComplete)
    }
}