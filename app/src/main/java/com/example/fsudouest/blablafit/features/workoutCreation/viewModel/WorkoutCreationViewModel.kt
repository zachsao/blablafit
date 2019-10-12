package com.example.fsudouest.blablafit.features.workoutCreation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import javax.inject.Inject

class WorkoutCreationViewModel @Inject constructor(
        private val auth: FirebaseAuth,
        private val firestore: FirebaseFirestore
) : ViewModel() {

    val workoutLiveData = MutableLiveData<Seance>()
    private val firebaseUser = auth.currentUser

    fun addAuthor(onComplete: () -> Unit){
        val uid = firebaseUser?.uid ?: ""
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    workoutLiveData.value?.idAuteur = uid
                    workoutLiveData.value?.nomAuteur = user?.nomComplet ?: ""
                    workoutLiveData.value?.photoAuteur = user?.photoUrl
                    onComplete()
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun saveWorkout(navigateToHomePage: () -> Unit, showToast: () -> Unit){
        val doc = firestore.collection("workouts").document()
        workoutLiveData.value?.id = doc.id
        doc.set(workoutLiveData.value!!)
                .addOnSuccessListener { navigateToHomePage() }
                .addOnFailureListener {
                    showToast()
                    TODO("Not implemented : Show a dialog")
                }
    }

    fun addWorkout(workout: Seance){
        workoutLiveData.value = workout
    }
}