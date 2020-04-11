package com.example.fsudouest.blablafit.features.filters

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FiltersModule {
    @ContributesAndroidInjector
    abstract fun contributeFiltersActivity(): FiltersActivity

    @Binds
    abstract fun bindFiltersViewModel(repoViewModel: FiltersViewModel): ViewModel
}