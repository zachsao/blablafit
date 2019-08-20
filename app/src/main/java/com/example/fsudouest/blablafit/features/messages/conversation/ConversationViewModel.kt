package com.example.fsudouest.blablafit.features.messages.conversation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.model.Conversation
import com.google.android.libraries.places.internal.it
import com.google.firebase.firestore.*
import com.xwray.groupie.Group
import javax.inject.Inject

class ConversationViewModel @Inject constructor(private val mDatabase: FirebaseFirestore): ViewModel() {

    private val chatLiveData = MutableLiveData<List<Group>>()

    fun chatsLiveData() = chatLiveData

    fun getConversation(convId: String, userId: String){
        mDatabase.collection("conversations")
            .whereEqualTo("id", convId)
            .addSnapshotListener(MetadataChanges.INCLUDE){ snapshots, e ->
                if (e != null) {
                    Log.e("ConversationViewModel", "listen:error", e)
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.let { list ->
                    for (dc in list) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED -> {
                                val chatLog = mutableListOf<Group>()
                                val convo = dc.document.toObject(Conversation::class.java)
                                convo.let { conversation ->
                                    conversation.chatLog.forEach {
                                        Log.d("ConversationViewModel", "$it")
                                        if (it.senderId == userId) chatLog.add(ChatToItem(it))
                                        else chatLog.add(ChatFromItem(it))
                                    }
                                }
                                Log.d("ConversationViewModel", "$chatLog")
                                chatLiveData.value = chatLog
                            }
                        }
                    }
                }
            }
    }

    fun sendMessage(convId: String, message: String, uid: String?) {

        uid?.let {
          mDatabase.collection("conversations")
                  .document(convId)
                  .update("chatLog", FieldValue.arrayUnion(Chat(it, message)))
        } ?: return
    }
}