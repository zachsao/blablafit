package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.ui.activities.DetailsSeanceActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailsSeanceActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeDetailsSeanceActivity(): DetailsSeanceActivity
}