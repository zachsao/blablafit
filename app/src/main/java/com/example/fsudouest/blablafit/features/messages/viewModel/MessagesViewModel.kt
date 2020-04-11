package com.example.fsudouest.blablafit.features.messages.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.MessagesData
import com.example.fsudouest.blablafit.features.messages.MessagesState
import com.example.fsudouest.blablafit.features.messages.ui.UserViewItem
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class MessagesViewModel@Inject constructor(private val firestore: FirebaseFirestore, auth: FirebaseAuth): ViewModel() {

    private val stateLiveData = MutableLiveData<MessagesState>()
    private val currentUserId = auth.currentUser?.uid ?: ""

    init {
        stateLiveData.value = MessagesState.Idle(MessagesData())
        getUserConversations()
    }

    fun stateLiveData() = stateLiveData

    private fun getUserConversations(){
        firestore.collection("users").document(currentUserId).collection("engagedConversations")
            .get()
                .addOnSuccessListener { getConversations() }
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
                    else stateLiveData.value = MessagesState.ConversationsEmpty(previousData().copy(conversations = emptyList()))
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun getUsers(ids: List<String>) {
        firestore.collection("users")
                .whereIn("uid", ids)
                .get()
                .addOnSuccessListener {
                    val users = it.documents
                            .mapNotNull { it.toObject(User::class.java) }
                            .map { user -> UserViewItem(user.uid, user.nomComplet, user.photoUrl) }

                    stateLiveData.value = MessagesState.ConversationsLoaded(previousData().copy(conversations = users))
                }
    }

    private fun previousData() = stateLiveData.value?.data ?: MessagesData()
}