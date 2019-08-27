package com.example.fsudouest.blablafit.features.nearby.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import javax.inject.Inject

class NearByViewModel @Inject constructor(private val mDatabase: FirebaseFirestore) : ViewModel() {

    private val workoutListLiveData = MutableLiveData<ArrayList<Seance?>>()

    val filteredList = ArrayList<Seance?>()
    private val fullList = ArrayList<Seance?>()

    fun workoutsLiveData() = workoutListLiveData

    fun updateWorkouts() {
        val ref = mDatabase.collection("workouts")
        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER
        // Get the document, forcing the SDK to use the offline cache
        ref.get(source)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val results = task.result!!.documents.map { it.toObject(Seance::class.java) } as ArrayList
                        fullList.addAll(results)
                        workoutListLiveData.value = results
                    } else {
                        Log.e("NearByViewModel", "ValidationError getting documents: ", task.exception)
                    }
                }
    }

    fun search(query: String) {
        val wanted = fullList.filter { workout -> workout!!.titre.toLowerCase().contains(query) } as ArrayList<Seance?>
        filteredList.clear()
        filteredList.addAll(wanted)
    }


}