package com.example.fsudouest.blablafit.viewModel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.Adapters.SeanceAdapter
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import java.util.*

class WorkoutsViewModel: ViewModel() {

    private val workoutsLiveData = MutableLiveData<ArrayList<Seance?>>()

    fun workoutsLiveData() = workoutsLiveData

    fun getWorkouts(debutJournee: Date, finJournee: Date = Date(),email:String?,mDatabase: FirebaseFirestore){
        Log.i("SeancesFragment","récupération des séances")
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
}