package com.android_group10.needy.messaging

import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.Message

class ChatActivityViewModel : ViewModel() {
    private lateinit var _messageList: List<Message>
    val messageList get() = _messageList

    fun retrieveMessages(){
        //Dummy data
    }
}