package com.android_group10.needy.messaging

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.data.request.RequestQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.android_group10.needy.ui.messaging.conversations.ConversationsFragment
import com.android_group10.needy.ui.messaging.requests.RequestsFragment

class MessagingFragmentViewModel : ViewModel() {

    lateinit var conversationUID: String
    private lateinit var tabCallback: (tabIndex: Int) -> Unit

    val pagerFragments: List<Fragment> by lazy {
        listOf(RequestsFragment(tabCallback), ConversationsFragment())
    }

    fun setupTabCallback(tabCallback: (tabIndex: Int) -> Unit){
        this.tabCallback = tabCallback
    }

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