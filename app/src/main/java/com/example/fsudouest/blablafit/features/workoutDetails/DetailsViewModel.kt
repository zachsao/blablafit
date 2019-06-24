package com.example.fsudouest.blablafit.features.workoutDetails

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val mDatabase: FirebaseFirestore): ViewModel() {

    private val detailsLiveData = MutableLiveData<Seance>()

    fun detailsLiveData() = detailsLiveData

    fun getWorkoutDetails(id: String){
        mDatabase.collection("workouts")
                .document(id)
                .get()
                .addOnSuccessListener {
                    detailsLiveData.value = it.toObject(Seance::class.java)!!
                }
                .addOnFailureListener{
                    Log.e("Participer", it.message)
                }
    }

    fun joinWorkout(seance: Seance, user: FirebaseUser?, activity: Activity) {
        mDatabase.collection("workouts")
                .document(seance.id)
                .update("participants", FieldValue.arrayUnion(user?.email))
                .addOnSuccessListener {
                    Log.i("Participer", "Nouveau participant : ${user?.displayName}")
                    activity.finish()
                }.addOnFailureListener {
                    Log.e("Participer", it.message)
                }
    }

    fun unjoinWorkout(seance: Seance, user: FirebaseUser?, activity: Activity){
        mDatabase.collection("workouts")
                .document(seance.id)
                .update("participants", FieldValue.arrayRemove(user?.email))
                .addOnSuccessListener {
                    Log.i("Participer", "${user?.displayName} s'est désinscrit de la séance :(")
                    activity.finish()
                }.addOnFailureListener {
                    Log.e("Participer", it.message)
                }
        // TODO : remove workout from My workouts list
    }

    fun deleteWorkout(seance: Seance, activity: Activity){
        mDatabase.collection("workouts")
                .document(seance.id)
                .delete()
                .addOnSuccessListener {
                    activity.finish()
                }.addOnFailureListener {
                    Log.e("Delete workout", it.message)
                }
        // TODO : remove workout from both workouts list
    }
}