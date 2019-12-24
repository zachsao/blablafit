package com.example.fsudouest.blablafit.features.workoutDetails

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val mDatabase: FirebaseFirestore): ViewModel() {

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
                    Log.e("Participer", it.message)
                }
    }

    fun joinWorkout(seance: Seance, user: FirebaseUser?, activity: Activity) {
        workoutsRef
                .document(seance.id)
                .update("participants", FieldValue.arrayUnion(user?.email))
                .addOnSuccessListener {
                    activity.finish()
                }.addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun unjoinWorkout(seance: Seance, user: FirebaseUser?, activity: Activity){
        workoutsRef
                .document(seance.id)
                .update("participants", FieldValue.arrayRemove(user?.email))
                .addOnSuccessListener {
                    activity.finish()
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
                    Log.e("Delete workout", it.message)
                }
    }
}