package com.android_group10.needy.messaging.util

import android.util.Log
import androidx.lifecycle.LiveData
import com.android_group10.needy.Post
import com.android_group10.needy.messaging.data.conversation.Conversation
import com.android_group10.needy.messaging.data.conversation.ConversationQueryItem
import com.android_group10.needy.messaging.data.message.ChatMessage
import com.android_group10.needy.messaging.data.message.ChatMessageQueryItem
import com.android_group10.needy.messaging.data.request.Request
import com.android_group10.needy.messaging.data.request.RequestQueryItem
import com.android_group10.needy.messaging.util.liveData.FirestoreChatMessageQueryLiveData
import com.android_group10.needy.messaging.util.liveData.FirestoreConversationLiveData
import com.android_group10.needy.messaging.util.liveData.FirestoreConversationQueryLiveData
import com.android_group10.needy.messaging.util.liveData.FirestoreRequestQueryLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.function.BiConsumer

object FirestoreUtil {
    const val FIRESTORE_LOG_TAG = "FIRESTORE"
    private const val ROOT_COLLECTION = "user_messaging_data"
    private const val REQUESTS_COLLECTION = "messaging_requests"
    private const val BLOCK_LIST_COLLECTION = "messaging_blocklist"
    private const val CONVERSATIONS_COLLECTION = "user_conversations"
    private const val MESSAGES_COLLECTION = "conversation_messages"

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val firebaseAuthInstance: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val currentUserMessagingDataRef: DocumentReference
        get() = firestoreInstance.document(
            "$ROOT_COLLECTION/${firebaseAuthInstance.currentUser!!.uid}"
        )

    private val conversationsCollectionRef = firestoreInstance.collection(CONVERSATIONS_COLLECTION)

    /*
    Requests
     */

    private fun isOnBlockList(
        userUID: String,
        receiverUID: String,
        onComplete: (Boolean?, Exception?) -> Unit
    ) {
        val receiverBlockListRef = firestoreInstance
            .collection("$ROOT_COLLECTION/$receiverUID/$BLOCK_LIST_COLLECTION")

        val query = receiverBlockListRef.whereEqualTo("userUID", userUID)

        query.get().addOnSuccessListener {
            if (it.isEmpty) {
                onComplete(false, null)
            } else {
                onComplete(true, null)
            }
        }.addOnFailureListener {
            onComplete(null, it)
        }
    }

    private fun isRequestRegistered(
        associatedPostUID: String,
        receiverUID: String,
        senderUID: String,
        onComplete: (alreadyRegistered: Boolean?) -> Unit
    ) {
        firestoreInstance
            .collection("$ROOT_COLLECTION/$receiverUID/$REQUESTS_COLLECTION")
            .whereEqualTo("associatedPostUID", associatedPostUID)
            .whereEqualTo("senderUID", senderUID)
            .get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    onComplete(false)
                } else {
                    onComplete(true)
                }
            }.addOnFailureListener {
                Log.e(FIRESTORE_LOG_TAG, "Failed to query database for requests.", it)
                onComplete(null)
            }
    }

    private fun createRequest(
        post: Post,
        volunteer: Boolean,
        onComplete: (wasSuccessful: Boolean, message: String) -> Unit
    ) {
        lateinit var completionMessage: String
        val currentUserUID = firebaseAuthInstance.currentUser!!.uid

        //Determine receiver
        val receiverUID = if (volunteer) post.authorUID else post.volunteer

        val receiverMessagingDataRequestsRef = firestoreInstance
            .collection("$ROOT_COLLECTION/$receiverUID/$REQUESTS_COLLECTION")

        //Check if conversation that was not concluded already exists
        doesConversationExist(
            post.postUID,
            currentUserUID,
            false,
        ) { _conversationAlreadyActive, _ ->

            _conversationAlreadyActive?.let { conversationAlreadyActive ->

                if (!conversationAlreadyActive) {
                    //Check if there is already a pending request
                    isRequestRegistered(
                        post.postUID,
                        receiverUID,
                        currentUserUID
                    ) { _requestAlreadyRegistered ->

                        _requestAlreadyRegistered?.let { requestAlreadyRegistered ->

                            if (!requestAlreadyRegistered) {
                                //Check if user is on the block list of the receiver
                                isOnBlockList(
                                    currentUserUID,
                                    receiverUID
                                ) { _isBlocked, exception ->

                                    exception?.let {
                                        completionMessage =
                                            "Error occurred when trying to read block list."
                                        Log.e(FIRESTORE_LOG_TAG, completionMessage, it)
                                        onComplete(false, completionMessage)
                                        return@isOnBlockList
                                    }

                                    _isBlocked?.let { isBlocked ->
                                        if (!isBlocked) {
                                            FirebaseUtil.getUser(currentUserUID) { user ->
                                                if (user != null) {
                                                    val userFullName =
                                                        "${user.firstName} ${user.lastName}"
                                                    receiverMessagingDataRequestsRef
                                                        .add(
                                                            Request(
                                                                post.postUID,
                                                                currentUserUID,
                                                                post.description,
                                                                userFullName
                                                            )
                                                        ).addOnSuccessListener {
                                                            completionMessage =
                                                                "Request successfully sent!"
                                                            onComplete(true, completionMessage)
                                                        }
                                                        .addOnFailureListener {
                                                            completionMessage =
                                                                "Error occurred when trying to create request."
                                                            Log.e(
                                                                FIRESTORE_LOG_TAG,
                                                                completionMessage,
                                                                it
                                                            )
                                                            onComplete(false, completionMessage)
                                                        }
                                                } else {
                                                    completionMessage =
                                                        "Error occurred when trying to create request."
                                                    onComplete(false, completionMessage)
                                                }
                                            }
                                        } else {
                                            completionMessage =
                                                "Request could not be sent (blocked)."
                                            onComplete(false, completionMessage)
                                        }
                                    }
                                }
                            } else {
                                completionMessage = "Request already pending."
                                onComplete(false, completionMessage)
                            }
                        }
                    }
                } else {
                    completionMessage = "Conversation already exists with user."
                    onComplete(false, completionMessage)
                }
            }
        }
    }

    @JvmStatic
    fun createRequest(post: Post, volunteer: Boolean, onComplete: BiConsumer<Boolean, String>) =
        createRequest(post, volunteer, onComplete::accept)

    fun acceptRequest(
        requestQueryItem: RequestQueryItem,
        onComplete: (wasSuccessful: Boolean, message: String) -> Unit
    ) {
        createConversation(requestQueryItem) { _wasSuccessful, _message ->
            if (_wasSuccessful) {
                removeRequest(requestQueryItem.id) { wasSuccessful, message ->
                    if (wasSuccessful) {
                        onComplete(wasSuccessful, _message)
                    } else {
                        onComplete(wasSuccessful, message)
                    }
                }
            } else {
                onComplete(_wasSuccessful, _message)
            }
        }
    }

    fun addToBlockList(
        userUID: String,
        onComplete: (wasSuccessful: Boolean, message: String) -> Unit
    ) {
        val currentUserUID = firebaseAuthInstance.currentUser!!.uid
        val currentUserBlockListRef =
            firestoreInstance.collection("$ROOT_COLLECTION/$currentUserUID/$BLOCK_LIST_COLLECTION")
        currentUserBlockListRef.add(
            mutableMapOf(
                "userUID" to userUID
            )
        ).addOnSuccessListener {
            removeRequestsFromUser(userUID) { _wasSuccessful ->
                if (_wasSuccessful) {
                    concludeConversationsFromUser(userUID) { wasSuccessful ->
                        if (wasSuccessful) {
                            onComplete(
                                wasSuccessful,
                                "User blocked. All their requests were removed and conversations with them were archived."
                            )
                        } else {
                            onComplete(
                                wasSuccessful,
                                "Error: Failed to block user. If this keeps happening, contact an administrator."
                            )
                        }
                    }
                } else {
                    onComplete(_wasSuccessful, "Error: Failed to remove blocked user's requests.")
                }
            }
        }.addOnFailureListener {
            val errorMessage = "Error occurred when trying to add user to block list."
            Log.e(FIRESTORE_LOG_TAG, errorMessage, it)
            onComplete(false, errorMessage)
        }
    }

    fun removeRequest(
        requestUID: String,
        onComplete: (wasSuccessful: Boolean, message: String) -> Unit
    ) {
        currentUserMessagingDataRef.collection(REQUESTS_COLLECTION).document(requestUID).delete()
            .addOnSuccessListener {
                onComplete(true, "Request removed successfully.")
            }
            .addOnFailureListener {
                Log.e(FIRESTORE_LOG_TAG, "Error occurred when trying to delete request.", it)
                onComplete(false, "Error: Could not delete request.")
            }
    }

    private fun removeRequestsFromUser(
        userUID: String,
        onComplete: (wasSuccessful: Boolean) -> Unit
    ) {
        val currentUserRequestsCollectionRef =
            currentUserMessagingDataRef.collection(REQUESTS_COLLECTION)
        val query = currentUserRequestsCollectionRef.whereEqualTo("senderUID", userUID)

        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                firestoreInstance.runBatch { batch ->
                    querySnapshot.forEach { queryDocumentSnapshot ->
                        batch.delete(currentUserRequestsCollectionRef.document(queryDocumentSnapshot.id))
                    }
                }.addOnSuccessListener {
                    onComplete(true)
                }.addOnFailureListener {
                    Log.e(FIRESTORE_LOG_TAG, "Failed to remove requests from user.", it)
                    onComplete(false)
                }
            } else {
                //No requests to be deleted
                onComplete(true)
            }
        }.addOnFailureListener {
            Log.e(FIRESTORE_LOG_TAG, "Failed to query database for blocked user's requests.", it)
            onComplete(false)
        }
    }

    fun getRequestsLiveData(): LiveData<List<RequestQueryItem>> {
        val query = currentUserMessagingDataRef.collection(REQUESTS_COLLECTION)
        return FirestoreRequestQueryLiveData(query)
    }

    /*
    Conversations
     */

    private fun doesConversationExist(
        associatedPostUID: String,
        userUID: String,
        shouldBeConcluded: Boolean,
        onComplete: (alreadyExists: Boolean?, querySnapshot: QuerySnapshot?) -> Unit
    ) {
        conversationsCollectionRef.whereEqualTo(
            "associatedPostUID",
            associatedPostUID
        ).whereArrayContains(
            "userUIDs",
            userUID
        ).whereEqualTo("concluded", shouldBeConcluded).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                onComplete(false, null)
            } else {
                onComplete(true, querySnapshot)
            }
        }.addOnFailureListener {
            Log.e(FIRESTORE_LOG_TAG, "Failed to query database for conversations.", it)
            onComplete(null, null)
        }
    }

    private fun createConversation(
        requestQueryItem: RequestQueryItem,
        onComplete: (wasSuccessful: Boolean, message: String) -> Unit
    ) {
        lateinit var completionMessage: String
        val currentUserUID = firebaseAuthInstance.currentUser!!.uid

        doesConversationExist(
            requestQueryItem.item.associatedPostUID,
            currentUserUID,
            true,
        ) { _alreadyExists, querySnapshot ->
            _alreadyExists?.let { alreadyExists ->
                if (!alreadyExists) {
                    FirebaseUtil.getUser(currentUserUID) { user ->
                        if (user != null) {
                            val userFullName = "${user.firstName} ${user.lastName}"
                            conversationsCollectionRef.add(
                                Conversation(
                                    requestQueryItem.item.associatedPostUID,
                                    requestQueryItem.item.associatedPostDescription,
                                    listOf(currentUserUID, requestQueryItem.item.senderUID),
                                    mutableMapOf(
                                        currentUserUID to userFullName,
                                        requestQueryItem.item.senderUID to requestQueryItem.item.senderFullName
                                    ),
                                    ChatMessage(),
                                    unread = false,
                                    concluded = false
                                )
                            )
                                .addOnSuccessListener {
                                    completionMessage = "Request accepted."
                                    onComplete(true, completionMessage)
                                }.addOnFailureListener {
                                    completionMessage = "Error: Request could not be accepted."
                                    Log.e(
                                        FIRESTORE_LOG_TAG,
                                        "Failed to create new conversation.",
                                        it
                                    )
                                    onComplete(false, completionMessage)
                                }
                        } else {
                            completionMessage = "Error: Request could not be accepted."
                            onComplete(false, completionMessage)
                        }
                    }
                } else {
                    if (querySnapshot != null) {
                        updateConversationStatus(
                            querySnapshot.documents[0].id,
                            false
                        ) { wasSuccessful ->
                            if (wasSuccessful) {
                                completionMessage =
                                    "Request accepted. Reopened existing conversation."
                                onComplete(true, completionMessage)
                            } else {
                                completionMessage = "Error: Request could not be accepted."
                                onComplete(false, completionMessage)
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateConversationStatus(
        conversationUID: String,
        setConcluded: Boolean,
        onComplete: (wasSuccessful: Boolean) -> Unit
    ) {
        conversationsCollectionRef.document(conversationUID).update("concluded", setConcluded)
            .addOnSuccessListener {
                Log.d(FIRESTORE_LOG_TAG, "Conversation status updated")
                onComplete(true)
            }
            .addOnFailureListener {
                Log.e(FIRESTORE_LOG_TAG, "Failed to update conversation status.", it)
                onComplete(false)
            }
    }

    @JvmStatic
    fun concludeConversationsForPost(post: Post) {
        conversationsCollectionRef.whereEqualTo("associatedPostUID", post.postUID).get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    firestoreInstance.runBatch { batch ->
                        querySnapshot.forEach { queryDocumentSnapshot ->
                            batch.update(
                                conversationsCollectionRef.document(queryDocumentSnapshot.id),
                                "concluded",
                                true
                            )
                        }
                    }.addOnFailureListener {
                        Log.e(
                            FIRESTORE_LOG_TAG,
                            "Failed to conclude conversations associated with post.",
                            it
                        )
                    }
                } else {
                    Log.e(FIRESTORE_LOG_TAG, "No conversation was found for selected post.")
                }
            }.addOnFailureListener {
                Log.e(
                    FIRESTORE_LOG_TAG,
                    "Failed to query database for associated conversation.",
                    it
                )
            }
    }

    private fun concludeConversationsFromUser(
        userUID: String,
        onComplete: (wasSuccessful: Boolean) -> Unit
    ) {
        val query = conversationsCollectionRef.whereArrayContains("userUIDs", userUID)
            .whereEqualTo("concluded", false)

        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                firestoreInstance.runBatch { batch ->
                    querySnapshot.forEach { queryDocumentSnapshot ->
                        batch.update(
                            conversationsCollectionRef.document(queryDocumentSnapshot.id),
                            "concluded",
                            true
                        )
                    }
                }.addOnSuccessListener {
                    onComplete(true)
                }.addOnFailureListener {
                    Log.e(FIRESTORE_LOG_TAG, "Failed to conclude conversations of user.", it)
                    onComplete(false)
                }
            } else {
                //No conversations to conclude
                onComplete(true)
            }
        }.addOnFailureListener {
            Log.e(FIRESTORE_LOG_TAG, "Failed to query database for user's conversations.", it)
            onComplete(false)
        }
    }

    fun getConversationLiveData(conversationUID: String): LiveData<Conversation> {
        return FirestoreConversationLiveData(conversationsCollectionRef.document(conversationUID))
    }

    fun getConversationsLiveData(): LiveData<List<ConversationQueryItem>> {
        val currentUserUID = firebaseAuthInstance.currentUser!!.uid
        val query = conversationsCollectionRef.whereArrayContains("userUIDs", currentUserUID)
            .whereEqualTo("concluded", false)
            .orderBy("latestMessage.timestamp", Query.Direction.DESCENDING)
        return FirestoreConversationQueryLiveData(query)
    }

    fun updateConversationReadStatus(conversationUID: String, isUnread: Boolean) {
        val conversationDocumentRef = conversationsCollectionRef.document(conversationUID)
        conversationDocumentRef.update("unread", isUnread)
            .addOnFailureListener {
                Log.e(
                    FIRESTORE_LOG_TAG,
                    "Failed to update latest message for conversation with uid: $conversationUID",
                    it
                )
            }
    }

    /*
    Messages
     */

    fun sendMessage(conversationUID: String, messageText: String, onComplete: (String?) -> Unit) {
        val currentUserUID = firebaseAuthInstance.currentUser!!.uid
        val chatMessage = ChatMessage(currentUserUID, messageText, Timestamp.now())
        conversationsCollectionRef.document(conversationUID).collection(MESSAGES_COLLECTION)
            .add(chatMessage).addOnSuccessListener {
                updateLatestMessage(conversationUID, chatMessage)
                onComplete(null)
            }.addOnFailureListener {
                Log.e(FIRESTORE_LOG_TAG, "Error when adding message to database.", it)
                onComplete("Failed to send message.")
            }
    }

    private fun updateLatestMessage(
        conversationUID: String,
        chatMessage: ChatMessage
    ) {
        val conversationDocumentRef = conversationsCollectionRef.document(conversationUID)
        firestoreInstance.runBatch { batch ->
            batch.update(conversationDocumentRef, "latestMessage", chatMessage)
            batch.update(conversationDocumentRef, "unread", true)
        }
            .addOnFailureListener {
                Log.e(
                    FIRESTORE_LOG_TAG,
                    "Failed to update latest message for conversation with uid: $conversationUID",
                    it
                )
            }
    }

    fun getChatMessagesLiveData(conversationUID: String): LiveData<List<ChatMessageQueryItem>> {
        val query = conversationsCollectionRef.document(conversationUID).collection(
            MESSAGES_COLLECTION
        ).orderBy("timestamp", Query.Direction.DESCENDING)
        return FirestoreChatMessageQueryLiveData(query)
    }
}
