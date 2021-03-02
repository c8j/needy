package com.android_group10.needy.messaging.util

import androidx.lifecycle.LiveData
import com.android_group10.needy.messaging.data.Request
import com.android_group10.needy.messaging.data.RequestQueryItem
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject

class FirestoreRequestQueryLiveData(private val query: Query) : LiveData<List<RequestQueryItem>>(),
    EventListener<QuerySnapshot> {

    private var listenerRegistration: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        listenerRegistration = query.addSnapshotListener(this)
    }

    override fun onInactive() {
        super.onInactive()
        listenerRegistration?.remove()
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        value?.documents?.let { documents ->
            val requestQueryItemList = mutableListOf<RequestQueryItem>()
            documents.forEach {
                it.toObject<Request>()?.apply {
                    requestQueryItemList.add(RequestQueryItem(this, it.id))
                }
            }
            setValue(requestQueryItemList)
        }
    }
}