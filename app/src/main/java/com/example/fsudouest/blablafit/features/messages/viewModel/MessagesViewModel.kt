package com.example.fsudouest.blablafit.features.messages.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.ui.UserViewItem
import com.example.fsudouest.blablafit.model.Conversation
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MessagesViewModel@Inject constructor(private val mDatabase: FirebaseFirestore, auth: FirebaseAuth): ViewModel() {

    private val usersLiveData = MutableLiveData<List<UserViewItem>>()
    private val currentUserId = auth.currentUser?.uid
    fun usersLiveData() = usersLiveData

    fun getUsers(){
        mDatabase.collection("users")
            .get()
            .addOnCompleteListener { task ->
                when(task.isSuccessful){
                    true -> {
                        Log.d("Messages", "task is successful")
                        task.result?.let { querySnapshot ->
                            val users = mutableListOf<UserViewItem>()
                            querySnapshot.documents.forEach { documentSnapshot ->
                                val user = documentSnapshot.toObject(User::class.java)
                                user?.let {
                                    if (documentSnapshot.id != currentUserId) users.add(UserViewItem(it, documentSnapshot.id))
                                }
                            }
                            usersLiveData.value = users
                        }
                    }
                    false -> Log.e("Messages", task.exception.toString())
                }
            }
    }
}