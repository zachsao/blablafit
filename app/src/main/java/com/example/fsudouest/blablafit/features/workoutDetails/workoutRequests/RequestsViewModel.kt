package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class RequestsViewModel @Inject constructor(val firestore: FirebaseFirestore) : ViewModel(){

    private val stateLiveData = MutableLiveData<RequestsState>()

    init {
        stateLiveData.value = RequestsState.Idle(RequestsData())
    }

    fun stateLiveData(): LiveData<RequestsState> = stateLiveData

    fun init(participants: List<String>){
        RequestsState.Loading(previousData())
        if (participants.isEmpty()) stateLiveData.value = RequestsState.ItemsEmpty(previousData())
        else {
            firestore.collection("users")
                    .whereIn("email", participants)
                    .get()
                    .addOnSuccessListener {snapshot ->
                        val requests = snapshot.documents
                                .mapNotNull { it.toObject(User::class.java) }
                                .map { it.toRequestViewItem() }

                        stateLiveData.value = if (requests.isNotEmpty()) RequestsState.ItemsUpdated(previousData().copy(requests = requests))
                        else RequestsState.ItemsEmpty(previousData())
                    }
                    .addOnFailureListener {
                        RequestsState.ItemsEmpty(previousData())
                        Timber.e(it)
                    }
        }
    }

    private fun previousData() = stateLiveData.value?.data ?: RequestsData()

    private fun User.toRequestViewItem() = RequestViewItem(photoUrl, nomComplet, fitnessLevel.toString())
}