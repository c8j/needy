package com.android_group10.needy.ui.messaging.data

import com.android_group10.needy.Post
import com.android_group10.needy.User

data class Conversation(
    //TODO: implement message preview
    val associatedPost: Post,
    val communicationPartner: User
)
