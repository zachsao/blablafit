package com.example.fsudouest.blablafit.features.messages.conversation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.ui.MessageViewItem
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.model.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ConversationViewModel @Inject constructor(private val mDatabase: FirebaseFirestore): ViewModel() {

    private val chatLiveData = MutableLiveData<List<ChatViewItem>>()

    fun chatsLiveData() = chatLiveData

    fun getConversation(contactName: String){
        mDatabase.collection("conversations")
                .whereEqualTo("user.nomComplet", contactName)
                .get()
                .addOnCompleteListener {task ->
                    when(task.isSuccessful){
                        true -> {
                            Log.d("Messages", "task is successful")
                            task.result?.let { querySnapshot ->
                                val chatLog = mutableListOf<ChatViewItem>()
                                querySnapshot.documents.forEach { documentSnapshot ->
                                    val convo = documentSnapshot.toObject(Conversation::class.java)
                                    convo?.let { conversation ->
                                        conversation.chatLog.forEach {
                                            chatLog.add(ChatViewItem(it))
                                        }
                                    }
                                }
                                chatLiveData.value = chatLog
                            }
                        }
                        false -> Log.e("Messages", task.exception.toString())
                    }
                }
    }
}