package com.example.fsudouest.blablafit.features.workoutDetails

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import timber.log.Timber
import javax.inject.Inject

class DetailsViewModel @Inject constructor(mDatabase: FirebaseFirestore, auth: FirebaseAuth): ViewModel() {

    private val workoutsRef = mDatabase.collection("workouts")
    private val detailsLiveData = MutableLiveData<Seance>()

    private val currentUser = auth.currentUser

    fun detailsLiveData() = detailsLiveData

    fun getWorkoutDetails(id: String){
        workoutsRef
                .document(id)
                .get(Source.CACHE)
                .addOnSuccessListener {
                    val workout = it.toObject(Seance::class.java)!!
                    detailsLiveData.value = workout
                }
                .addOnFailureListener{
                    Timber.e(it)
                }
    }

    fun joinWorkout(seance: Seance) {
        val previousParticipants = detailsLiveData.value?.participants ?: emptyMap()
        currentUser?.uid?.let {
            workoutsRef
                    .document(seance.id)
                    .update("participants.$it", RequestStatus.PENDING)
                    .addOnSuccessListener {_ ->
                        val workout = detailsLiveData.value!!.copy(participants = previousParticipants.plus(Pair(it,RequestStatus.PENDING)))
                        detailsLiveData.value = workout
                    }.addOnFailureListener {
                        Timber.e(it)
                    }
        }
    }

    fun unjoinWorkout(seance: Seance, onSuccess: () -> Unit){
            workoutsRef
                    .document(seance.id)
                    .update("participants.${currentUser?.uid}", FieldValue.delete())
                    .addOnSuccessListener {
                        onSuccess()
                    }.addOnFailureListener {
                        Timber.e(it)
                    }
    }

    fun deleteWorkout(seance: Seance, activity: Activity){
        workoutsRef
                .document(seance.id)
                .delete()
                .addOnSuccessListener {
                    activity.finish()
                }.addOnFailureListener {
                    Timber.e(it)
                }
    }
}