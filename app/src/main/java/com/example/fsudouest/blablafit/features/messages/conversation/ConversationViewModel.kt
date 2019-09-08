package com.example.fsudouest.blablafit.features.messages.conversation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.model.Conversation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class ConversationViewModel @Inject constructor(private val mDatabase: FirebaseFirestore, auth: FirebaseAuth): ViewModel() {

    private val chatLiveData = MutableLiveData<List<Item>>()
    private val currentUserId = auth.currentUser?.uid ?: ""

    fun chatsLiveData() = chatLiveData

    fun getOrCreateConversation(otherUserId: String, onComplete: (String) -> Unit){
        val currentUserDocRef = mDatabase.collection("users").document(currentUserId)
        currentUserDocRef.collection("engagedConversations")
            .document(otherUserId)
                .get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["conversationId"] as String)
                        return@addOnSuccessListener
                    }
                    val newConversation = mDatabase.collection("conversations").document()
                    newConversation.set(Conversation(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef.collection("engagedConversations").document(otherUserId)
                            .set(mapOf("conversationId" to newConversation.id))

                    mDatabase.collection("users").document(otherUserId)
                            .collection("engagedConversations")
                            .document(currentUserId)
                            .set(mapOf("conversationId" to newConversation.id))

                    onComplete(newConversation.id)
                }
    }

    fun listenToMessages(conversationId: String){
        mDatabase.collection("conversations")
                .document(conversationId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(MetadataChanges.INCLUDE){ snapshot, e ->
                    if (e != null) {
                        Log.e("ConversationViewModel", "listen:error", e)
                        return@addSnapshotListener
                    }
                    Log.d("ConversationViewModel", "${snapshot?.documents}")
                    val chatLog = mutableListOf<Item>()
                    snapshot?.documents?.forEach {
                        val chatMessage = it.toObject(Chat::class.java) ?: Chat()
                        if (it["senderId"] == currentUserId) {
                            chatLog.add(ChatToItem(chatMessage))
                        } else chatLog.add(ChatFromItem(chatMessage))
                    }
                    chatLiveData.value = chatLog
                }

    }

    fun sendMessage(convId: String, message: String, recipientId: String, senderName: String) {
        val chat = Chat(currentUserId, message, recipientId, senderName, System.currentTimeMillis())
          mDatabase.collection("conversations")
                  .document(convId)
                  .collection("messages")
                  .add(chat)
        }
}