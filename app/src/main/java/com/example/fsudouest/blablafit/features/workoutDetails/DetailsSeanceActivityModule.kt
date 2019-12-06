package com.example.fsudouest.blablafit.features.workoutDetails

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailsSeanceActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeDetailsSeanceActivity(): DetailsSeanceActivity

    @Binds
    abstract fun bindDetailsViewModel(repoViewModel: DetailsViewModel): ViewModel
}