package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.MainActivity
import com.example.fsudouest.blablafit.Ui.Activities.DetailsSeanceActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DetailsSeanceActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeDetailsSeanceActivity(): DetailsSeanceActivity
}