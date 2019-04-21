package com.example.fsudouest.blablafit.features.myWorkouts.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.util.*
import javax.inject.Inject

class WorkoutsViewModel @Inject constructor(private val mDatabase: FirebaseFirestore) : ViewModel() {

    private val workoutsLiveData = MutableLiveData<ArrayList<Seance?>>()

    fun workoutsLiveData() = workoutsLiveData

    fun getWorkouts(debutJournee: Date, finJournee: Date = Date(),email:String?){
        Log.i("Workouts view model","récupération des séances")
        val ref = mDatabase.collection("workouts")
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER
        // Get the document, forcing the SDK to use the offline cache
        ref.whereEqualTo("createur", email).whereGreaterThanOrEqualTo("date",debutJournee)
            .whereLessThanOrEqualTo("date",finJournee)
            .get(source)
            .addOnCompleteListener {task ->
                when(task.isSuccessful){
                    true -> {
                        workoutsLiveData.value = task.result!!.documents.map { it.toObject(Seance::class.java)} as ArrayList
                    }
                    else -> Log.e("WorkoutsViewModel",task.exception?.message)
                }
            }
    }

    fun deleteWorkout(workoutId: String){
        val ref = mDatabase.collection("workouts")
        ref.document(workoutId)
                .delete()
                .addOnSuccessListener { Log.d("Seances Fragment", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.e("Seances Fragment", "Error deleting document", e) }
    }
}