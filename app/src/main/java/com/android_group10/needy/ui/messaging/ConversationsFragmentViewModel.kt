package com.android_group10.needy.ui.messaging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android_group10.needy.Post
import com.android_group10.needy.User

class ConversationsFragmentViewModel : ViewModel() {
    //TODO: implement LiveData instead
    val conversationList: List<Conversation> = mutableListOf()

    fun initTestData() {
        //TODO: implement proper data grabbing
        repeat(5){
            (conversationList as MutableList).add(
                Conversation(
                    Post(
                        "test@email.com",
                        "Need help with groceries",
                        1,
                        "Kristianstad",
                        "29137",
                        "money"
                    ),
                    User(
                        "test@email.com",
                        "123456",
                        "John",
                        "Doe",
                        "123857612",
                        "Kristianstad",
                        29137
                    )
                )
            )
        }
    }
}