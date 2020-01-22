package com.example.fsudouest.blablafit.features.myWorkouts

import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem

sealed class MyWorkoutsState {
    abstract val data: MyWorkoutsData

    data class Idle(override val data: MyWorkoutsData) : MyWorkoutsState()
    data class WorkoutsEmpty(override val data: MyWorkoutsData) : MyWorkoutsState()
    data class WorkoutsLoaded(override val data: MyWorkoutsData) : MyWorkoutsState()
    data class Loading(override val data: MyWorkoutsData) : MyWorkoutsState()
    data class NetworkError(override val data: MyWorkoutsData) : MyWorkoutsState()

}

data class MyWorkoutsData(
        val myWorkouts: List<WorkoutViewItem> = emptyList(),
        val joinedWorkouts: List<WorkoutViewItem> = emptyList()
)