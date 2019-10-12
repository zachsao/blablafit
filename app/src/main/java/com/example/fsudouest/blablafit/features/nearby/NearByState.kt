package com.example.fsudouest.blablafit.features.nearby

import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import com.example.fsudouest.blablafit.features.nearby.ui.LatestWorkoutViewItem

sealed class NearByState {
    abstract val data: NearByData

    data class Idle(override val data: NearByData): NearByState()
    data class Loading(override val data: NearByData): NearByState()
    data class LatestWorkoutsLoaded(override val data: NearByData): NearByState()
}

data class NearByData(
        val workouts: List<LatestWorkoutViewItem> = emptyList(),
        val categories: List<CategoryViewItem> = emptyList()
)