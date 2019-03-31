package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.Ui.Fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMessagesFragment(): MessagesFragment

    @ContributesAndroidInjector
    abstract fun contributeMyProfileFragment(): MyProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeSeancesFragment(): SeancesFragment

    @ContributesAndroidInjector
    abstract fun contributeTrouverUneSeanceFragment(): TrouverUneSeanceFragment

    @ContributesAndroidInjector
    abstract fun contributeNouvelleSeanceFragment(): NouvelleSeanceFragment

    @ContributesAndroidInjector
    abstract fun contributeIndoorChoiceFragment(): IndoorChoiceFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchLocationFragment(): SearchLocationFragment
}