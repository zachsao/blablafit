package com.example.fsudouest.blablafit.features.messages.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.ui.MessageViewItem
import com.example.fsudouest.blablafit.model.Conversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import javax.inject.Inject

class MessagesViewModel@Inject constructor(private val mDatabase: FirebaseFirestore): ViewModel() {

    private val usersLiveData = MutableLiveData<List<MessageViewItem>>()

    fun usersLiveData() = usersLiveData

    fun getUsers(){
        mDatabase.collection("conversations")
            .get()
            .addOnCompleteListener { task ->
                when(task.isSuccessful){
                    true -> {
                        Log.d("Messages", "task is successful")
                        task.result?.let { querySnapshot ->
                            val messages = mutableListOf<MessageViewItem>()
                            querySnapshot.documents.forEach { documentSnapshot ->
                                val convo = documentSnapshot.toObject(Conversation::class.java)
                                convo?.let { messages.add(MessageViewItem(it)) }
                            }
                            usersLiveData.value = messages
                        }
                    }
                    false -> Log.e("Messages", task.exception.toString())
                }
            }
    }
}