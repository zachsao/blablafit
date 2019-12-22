package com.example.fsudouest.blablafit.features.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class CategoryViewModel @Inject constructor(val firestore: FirebaseFirestore): ViewModel() {

    private val stateLiveData = MutableLiveData<CategoryState>()

    fun stateLiveData(): LiveData<CategoryState> = stateLiveData

    init {
        stateLiveData.value = CategoryState.Idle(CategoryData())
    }

    fun getWorkouts(categoryName: String) {
        firestore.collection("workouts")
                .whereEqualTo("titre", categoryName)
                .get()
                .addOnSuccessListener { task ->
                    val workouts = task.documents.map { it.toObject(Seance::class.java)?.let { seance -> WorkoutViewItem(seance) }}
                    if (workouts.isNotEmpty()){
                        stateLiveData.value = CategoryState.WorkoutsLoaded(previousData().copy(workouts = workouts, error = null))
                    } else {
                        stateLiveData.value = CategoryState.WorkoutsEmpty(previousData().copy(error = "empty"))
                    }
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun previousData() = stateLiveData.value?.data ?: CategoryData()
}