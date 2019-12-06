package com.example.fsudouest.blablafit.features.workoutCreation.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class WorkoutCreationModule {
    @Binds
    abstract fun bindWorkoutCreationViewModel(repoViewModel: WorkoutCreationViewModel): ViewModel
}