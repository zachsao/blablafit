package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber
import javax.inject.Inject

class RequestsViewModel @Inject constructor(val firestore: FirebaseFirestore) : ViewModel(){

    private val stateLiveData = MutableLiveData<RequestsState>()
    private var workout: Seance? = null
    private lateinit var workoutId: String

    init {
        stateLiveData.value = RequestsState.Idle(RequestsData())
    }

    fun stateLiveData(): LiveData<RequestsState> = stateLiveData

    fun init(participants: Map<String, RequestStatus>, workoutId: String){
        RequestsState.Loading(previousData())
        Timber.d("init requests")
        this.workoutId = workoutId
        firestore.collection("workouts").document(workoutId)
                .get()
                .addOnSuccessListener { workout = it.toObject(Seance::class.java) }
                .addOnFailureListener { Timber.e(it) }


        if (participants.isEmpty()) stateLiveData.value = RequestsState.ItemsEmpty(previousData())
        else {
            firestore.collection("users")
                    .whereIn("uid", participants.map { it.key })
                    .get()
                    .addOnSuccessListener {snapshot ->
                        val requests = snapshot.documents
                                .mapNotNull { it.toObject(User::class.java) }
                                .mapIndexed { index, user ->
                                    val status = participants
                                            .filterKeys { it == user.uid }
                                            .map { it.value }
                                            .first()
                                    user.toRequestViewItem(index, status)
                                }

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

    private fun User.toRequestViewItem(position: Int, requestStatus: RequestStatus) = RequestViewItem(
            uid,
            photoUrl,
            nomComplet,
            fitnessLevel.toString(),
            onAcceptRequest = { acceptRequest(position) },
            onDeclineRequest = { declineRequest(position) },
            isGranted = requestStatus == RequestStatus.GRANTED
    )

    private fun acceptRequest(position: Int) {
        workout?.let {

            val update = previousData().requests.mapIndexed { index, requestViewItem ->
                if (index == position) requestViewItem.copy(isStatusUpdating = true) else requestViewItem
            }
            stateLiveData.value = RequestsState.RequestStatusUpdating(previousData().copy(requests = update))
            firestore.collection("workouts").document(workoutId)
                    .update("participants.${previousData().requests[position].uid}", RequestStatus.GRANTED)
                    .addOnSuccessListener {
                        val update2 = previousData().requests.mapIndexed { index, requestViewItem ->
                            if (index == position) requestViewItem.copy(isStatusUpdating =  false, isGranted = true) else requestViewItem
                        }
                        stateLiveData.value = RequestsState.RequestStatusUpdated(previousData().copy(requests = update2))
                    }.addOnFailureListener {
                        val update2 = previousData().requests.mapIndexed { index, requestViewItem ->
                            if (index == position) requestViewItem.copy(isStatusUpdating =  false) else requestViewItem
                        }
                        stateLiveData.value = RequestsState.RequestStatusUpdated(previousData().copy(requests = update2))
                    }

        }
    }
    private fun declineRequest(position: Int){
        workout?.let {

            val update = previousData().requests.mapIndexed { index, requestViewItem ->
                if (index == position) requestViewItem.copy(isStatusUpdating = true) else requestViewItem
            }

            stateLiveData.value = RequestsState.RequestStatusUpdating(previousData().copy(requests = update))

            firestore.collection("workouts").document(workoutId)
                    .update("participants.${previousData().requests[position].uid}", FieldValue.delete())
                    .addOnSuccessListener {
                        val update2 = previousData().requests.toMutableList().apply { removeAt(position) }
                        stateLiveData.value = if (update2.isEmpty()) RequestsState.ItemsEmpty(previousData().copy(requests = update2))
                            else RequestsState.RequestStatusUpdated(previousData().copy(requests = update2))
                    }.addOnFailureListener {
                        val update2 = previousData().requests.mapIndexed { index, requestViewItem ->
                            if (index == position) requestViewItem.copy(isStatusUpdating =  false) else requestViewItem
                        }
                        stateLiveData.value = RequestsState.RequestStatusUpdated(previousData().copy(requests = update2))
                    }

        }
    }
}