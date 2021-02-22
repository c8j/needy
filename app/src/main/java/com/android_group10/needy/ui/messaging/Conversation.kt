package com.android_group10.needy.ui.messaging

import com.android_group10.needy.Post
import com.android_group10.needy.User

data class Conversation(
    val associatedPost: Post,
    val communicationPartner: User
)
