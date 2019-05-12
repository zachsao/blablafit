package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.messages.ui.MessagesFragment
import com.example.fsudouest.blablafit.features.myWorkouts.ui.SeancesFragment
import com.example.fsudouest.blablafit.features.nearby.ui.NearByFragment
import com.example.fsudouest.blablafit.features.profile.ui.MyProfileFragment
import com.example.fsudouest.blablafit.features.workoutCreation.ui.IndoorChoiceFragment
import com.example.fsudouest.blablafit.features.workoutCreation.ui.SearchLocationFragment
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
    abstract fun contributeTrouverUneSeanceFragment(): NearByFragment

    @ContributesAndroidInjector
    abstract fun contributeIndoorChoiceFragment(): IndoorChoiceFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchLocationFragment(): SearchLocationFragment
}