package com.android_group10.needy.messaging.data.request

import com.android_group10.needy.messaging.data.QueryItem

data class RequestQueryItem(private val _item: Request, private val _id: String) :
    QueryItem<Request> {
    override val item: Request
        get() = _item
    override val id: String
        get() = _id
}
