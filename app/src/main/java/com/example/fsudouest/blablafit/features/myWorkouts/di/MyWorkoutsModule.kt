package com.example.fsudouest.blablafit.features.myWorkouts.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class MyWorkoutsModule {
    @Binds
    abstract fun bindWorkoutsViewModel(repoViewModel: WorkoutsViewModel): ViewModel
}