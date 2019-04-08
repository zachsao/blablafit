package com.example.fsudouest.blablafit.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Seance

class WorkoutsViewModel: ViewModel() {

    private val workoutsLiveData = MutableLiveData<Seance>()

    fun getworkouts() = workoutsLiveData

    fun loadWorkouts(){

    }
}