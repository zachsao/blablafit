package com.example.fsudouest.blablafit.features.category

import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem

sealed class CategoryState {
    abstract val data: CategoryData

    data class Idle(override val data: CategoryData): CategoryState()
    data class WorkoutsLoaded(override val data: CategoryData): CategoryState()
    data class WorkoutsEmpty(override val data: CategoryData): CategoryState()
}

data class CategoryData(
        val workouts: List<WorkoutViewItem?> = emptyList(),
        val error: String? = null
)