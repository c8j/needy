package com.android_group10.needy.ui.messaging.data

import com.android_group10.needy.Post
import com.android_group10.needy.User

data class Request(
    val associatedPostUID: String,
    val communicationPartnerUID: String
)
