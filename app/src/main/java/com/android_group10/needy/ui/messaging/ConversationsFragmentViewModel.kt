package com.android_group10.needy.ui.messaging

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.android_group10.needy.Post
import com.android_group10.needy.User
import com.android_group10.needy.ui.messaging.data.Conversation
import com.android_group10.needy.ui.messaging.data.Request
import java.util.*

class ConversationsFragmentViewModel : ViewModel() {
    //TODO: implement LiveData instead
    val conversationList: List<Conversation> = mutableListOf()
    val requestList: List<Request> = mutableListOf()

    private lateinit var _pagerFragments: List<Fragment>
    val pagerFragments get() = _pagerFragments

    fun init() {
        _pagerFragments = listOf(RequestsFragment(), MessagesFragment())

        //TODO: replace dummy data with actual data

    }
}