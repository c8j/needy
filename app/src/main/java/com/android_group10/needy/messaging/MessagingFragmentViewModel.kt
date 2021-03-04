package com.android_group10.needy.messaging

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.request.RequestQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.android_group10.needy.ui.messaging.conversations.ConversationsFragment
import com.android_group10.needy.ui.messaging.requests.RequestsFragment

class MessagingFragmentViewModel : ViewModel() {

    val pagerFragments: List<Fragment> by lazy {
        listOf(RequestsFragment(), ConversationsFragment())
    }

    fun getRequests(): LiveData<List<RequestQueryItem>> {
        return FirestoreUtil.getRequestsLiveData()
    }

    fun getConversations(): LiveData<List<ConversationQueryItem>> {
        return FirestoreUtil.getConversationsLiveData()
    }
}