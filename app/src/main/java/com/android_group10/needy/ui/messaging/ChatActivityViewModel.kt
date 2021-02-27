package com.android_group10.needy.ui.messaging

import androidx.lifecycle.ViewModel
import com.android_group10.needy.ui.messaging.data.Message
import java.time.ZonedDateTime

class ChatActivityViewModel : ViewModel() {
    private lateinit var _messageList: List<Message>
    val messageList get() = _messageList

    fun retrieveMessages(){
        //Dummy data
    }
}