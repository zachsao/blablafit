package com.example.fsudouest.blablafit.features.messages.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.ui.UserViewItem
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import timber.log.Timber
import javax.inject.Inject

class MessagesViewModel@Inject constructor(private val firestore: FirebaseFirestore, auth: FirebaseAuth): ViewModel() {

    private val usersLiveData = MutableLiveData<List<UserViewItem>>()
    private val currentUserId = auth.currentUser?.uid ?: ""
    fun usersLiveData() = usersLiveData

    fun getUserConversations(){
        firestore.collection("users").document(currentUserId).collection("engagedConversations")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.documents.isNullOrEmpty()) getConversations()
            }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun getConversations() {
        firestore.collection("conversations")
                .whereArrayContains("userIds", currentUserId)
                .get()
                .addOnSuccessListener {
                    val userIds = it.documents
                            .mapNotNull { documentSnapshot ->
                                (documentSnapshot["userIds"] as List<String>).find { it != currentUserId }
                            }
                    if (userIds.isNotEmpty()) getUsers(userIds)
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun getUsers(ids: List<String>) {
        firestore.collection("users")
                .whereIn("uid", ids)
                .get()
                .addOnSuccessListener {
                    val users = it.documents
                            .mapNotNull {
                                it.toObject(User::class.java)
                            }.map { UserViewItem(it) }

                    usersLiveData.value = users
                }
    }
}