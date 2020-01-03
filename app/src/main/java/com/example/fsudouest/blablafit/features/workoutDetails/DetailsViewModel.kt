package com.example.fsudouest.blablafit.features.workoutDetails

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import timber.log.Timber
import javax.inject.Inject

class DetailsViewModel @Inject constructor(mDatabase: FirebaseFirestore): ViewModel() {

    private val workoutsRef = mDatabase.collection("workouts")
    private val detailsLiveData = MutableLiveData<Seance>()

    fun detailsLiveData() = detailsLiveData

    fun getWorkoutDetails(id: String){
        workoutsRef
                .document(id)
                .get()
                .addOnSuccessListener {
                    val workout = it.toObject(Seance::class.java)!!
                    detailsLiveData.value = workout
                }
                .addOnFailureListener{
                    Timber.e(it)
                }
    }

    fun joinWorkout(seance: Seance, user: FirebaseUser?) {
        val previousParticipants = detailsLiveData.value?.participants ?: emptyMap()
        user?.email?.let {
            val workout = detailsLiveData.value!!.copy(participants = previousParticipants.plus(Pair(it,RequestStatus.PENDING)))
            workoutsRef
                    .document(seance.id)
                    .set(workout, SetOptions.mergeFields("participants"))
                    .addOnSuccessListener {
                        detailsLiveData.value = workout
                    }.addOnFailureListener {
                        Timber.e(it)
                    }
        }
    }

    fun unjoinWorkout(seance: Seance, user: FirebaseUser?, activity: Activity){
        user?.email?.let {
            val updatedParticipants = detailsLiveData.value?.participants?.minus(it) ?: emptyMap()
            val workout = detailsLiveData.value!!.copy(participants = updatedParticipants)
            workoutsRef
                    .document(seance.id)
                    .set(workout, SetOptions.mergeFields("participants"))
                    .addOnSuccessListener {
                        activity.finish()
                    }.addOnFailureListener {
                        Timber.e(it)
                    }
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