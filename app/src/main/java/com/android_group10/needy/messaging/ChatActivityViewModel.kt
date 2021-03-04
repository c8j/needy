package com.android_group10.needy.messaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil

class ChatActivityViewModel() : ViewModel() {

    lateinit var conversationUID: String
    lateinit var partnerUID: String

    fun retrieveMessages(): LiveData<List<ChatMessageQueryItem>>{
        return FirestoreUtil.getChatMessagesLiveData(conversationUID)
    }

    fun sendMessage(messageText: String, onComplete: (String?) -> Unit){
        FirestoreUtil.sendMessage(conversationUID, messageText, onComplete)
    }
}