package com.example.fsudouest.blablafit.features.myWorkouts.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(private val mDatabase: FirebaseFirestore,
                                            auth: FirebaseAuth) : ViewModel() {

    private val workoutsLiveData = MutableLiveData<ArrayList<Seance?>>()
    private val currentUser = auth.currentUser

    fun workoutsLiveData() = workoutsLiveData

    fun getWorkouts(debutJournee: Date, finJournee: Date = Date()) {
        Timber.i("récupération des séances")
        mDatabase.collection("workouts")
                .whereEqualTo("idAuteur", currentUser?.uid)
                .whereGreaterThanOrEqualTo("date", debutJournee)
                .whereLessThanOrEqualTo("date", finJournee)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Timber.e(exception)
                        return@addSnapshotListener
                    }else {
                        val workouts = snapshot!!.documents.map { it.toObject(Seance::class.java) }
                        workoutsLiveData.value = workouts as ArrayList
                    }
                }
    }

    fun deleteWorkout(workoutId: String) {
        val ref = mDatabase.collection("workouts")
        ref.document(workoutId)
                .delete()
                .addOnSuccessListener { Log.d("Seances Fragment", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.e("Seances Fragment", "ValidationError deleting document", e) }
    }
}