package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.Ui.Fragments.NouvelleSeanceFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NouvelleSeanceActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeNouvelleSeanceActivity(): NouvelleSeanceFragment
}