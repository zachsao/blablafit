package com.example.fsudouest.blablafit.features.conversation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.model.Conversation
import com.example.fsudouest.blablafit.utils.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import timber.log.Timber

class ConversationViewModel @ViewModelInject constructor(private val mDatabase: FirebaseFirestore, auth: FirebaseAuth) : ViewModel() {

    private val stateLiveData = MutableLiveData<ConversationState>()
    private val currentUserId = auth.currentUser?.uid ?: ""

    init {
        stateLiveData.value = ConversationState.Idle(ConversationData())
    }

    fun stateLiveData(): LiveData<ConversationState> = stateLiveData

    fun getOrCreateConversation(contactId: String) {
        val currentUserDocRef = mDatabase.collection("users").document(currentUserId)
        currentUserDocRef.collection("engagedConversations")
                .document(contactId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        stateLiveData.value = ConversationState.ConversationLoaded(previousStateData().copy(conversationId = it["conversationId"] as String))
                        return@addOnSuccessListener
                    }
                    createConversation(contactId)
                }
    }

    fun listenToMessages(conversationId: String){
        mDatabase.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, e ->
                    if (e != null) {
                        Timber.e(e)
                        return@addSnapshotListener
                    }
                    val chatLog = snapshot?.documents?.map {
                        val chat = it.toObject(Chat::class.java) ?: Chat()
                        if (it["senderId"] == currentUserId)
                            ChatToItem(chat)
                        else
                            ChatFromItem(chat)
                    } ?: emptyList()
                    stateLiveData.value = ConversationState.ChatsUpdated(previousStateData().copy(chats = chatLog))
                }
    }

    fun sendMessage(message: String, recipientId: String) {
        FirestoreUtil.getCurrentUser {
            val chat = Chat(currentUserId, message, recipientId, it.nomComplet, System.currentTimeMillis())
            val convId = previousStateData().conversationId ?: error("ConversationId is null")
            mDatabase.collection("conversations")
                    .document(convId)
                    .collection("messages")
                    .add(chat)
        }
    }

    private fun createConversation(contactId: String) {
        val newConversation = mDatabase.collection("conversations").document()
        newConversation.set(Conversation(mutableListOf(currentUserId, contactId)))

        mDatabase.collection("users")
                .document(currentUserId)
                .collection("engagedConversations")
                .document(contactId)
                .set(mapOf("conversationId" to newConversation.id))

        stateLiveData.value = ConversationState.ConversationCreated(previousStateData().copy(conversationId = newConversation.id))
    }

    private fun previousStateData(): ConversationData = stateLiveData().value?.data
            ?: ConversationData()
}