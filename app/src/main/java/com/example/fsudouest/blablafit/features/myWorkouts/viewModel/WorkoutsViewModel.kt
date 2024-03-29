package com.example.fsudouest.blablafit.features.myWorkouts.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.myWorkouts.MyWorkoutsData
import com.example.fsudouest.blablafit.features.myWorkouts.MyWorkoutsState
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.toWorkoutViewItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class WorkoutsViewModel @ViewModelInject constructor(private val mDatabase: FirebaseFirestore, auth: FirebaseAuth) : ViewModel() {

    private val stateLiveData = MutableLiveData<MyWorkoutsState>()
    private val currentUser = auth.currentUser

    fun stateLiveData(): LiveData<MyWorkoutsState> = stateLiveData

    fun getMyWorkouts() {
        Timber.i("récupération des séances")
        mDatabase.collection("workouts")
                .whereEqualTo("idAuteur", currentUser?.uid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val workouts = querySnapshot.documents.mapNotNull { it.toObject(Seance::class.java) }.map { it.toWorkoutViewItem() }
                    stateLiveData.value = when {
                        workouts.isNotEmpty() -> MyWorkoutsState.WorkoutsLoaded(previousStateData().copy(myWorkouts = workouts))
                        workouts.isEmpty() && previousStateData().joinedWorkouts.isNotEmpty() -> MyWorkoutsState.Idle(previousStateData())
                        workouts.isEmpty() && previousStateData().joinedWorkouts.isEmpty() -> MyWorkoutsState.WorkoutsEmpty(previousStateData().copy(myWorkouts = workouts))
                        else -> throw IllegalStateException()
                    }
                }
    }

    fun getJoinedWorkouts() {
        mDatabase.collection("workouts")
                .whereEqualTo("participants.${currentUser?.uid}", RequestStatus.GRANTED)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val workouts = querySnapshot.documents.mapNotNull { it.toObject(Seance::class.java) }.map { it.toWorkoutViewItem() }
                    stateLiveData.value = when {
                        workouts.isNotEmpty() -> MyWorkoutsState.WorkoutsLoaded(previousStateData().copy(joinedWorkouts = workouts))
                        workouts.isEmpty() && previousStateData().myWorkouts.isNotEmpty() -> MyWorkoutsState.Idle(previousStateData())
                        workouts.isEmpty() && previousStateData().myWorkouts.isEmpty() -> MyWorkoutsState.WorkoutsEmpty(previousStateData().copy(joinedWorkouts = workouts))
                        else -> throw IllegalStateException()
                    }
                }

    }

    private fun previousStateData() = stateLiveData.value?.data ?: MyWorkoutsData()
}