package com.android_group10.needy.messaging

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.messaging.data.RequestQueryItem
import com.android_group10.needy.messaging.util.FirestoreUtil
import com.android_group10.needy.ui.messaging.MessagesFragment
import com.android_group10.needy.ui.messaging.RequestsFragment

class ConversationsFragmentViewModel : ViewModel() {



    val pagerFragments: List<Fragment> by lazy {
        listOf(RequestsFragment(), MessagesFragment())
    }

    fun getRequests(): LiveData<List<RequestQueryItem>> {
        return FirestoreUtil.getRequestsLiveData()
    }

    fun initConversationsData() {

    }
}