package com.example.fsudouest.blablafit.features.workoutCreation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance

class WorkoutCreationViewModel : ViewModel() {

    val workoutLiveData = MutableLiveData<Seance>()

    fun addWorkout(workout: Seance){
        workoutLiveData.value = workout
    }
}