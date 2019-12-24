package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RequestsModule {
    @ContributesAndroidInjector
    abstract fun contributeRequestsActivity(): RequestsActivity

    @Binds
    abstract fun bindRequestsViewModel(repoViewModel: RequestsViewModel): ViewModel
}