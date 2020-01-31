package com.example.fsudouest.blablafit.features.home

import com.example.fsudouest.blablafit.features.messages.di.MessagesModule
import com.example.fsudouest.blablafit.features.messages.ui.MessagesFragment
import com.example.fsudouest.blablafit.features.myWorkouts.di.MyWorkoutsModule
import com.example.fsudouest.blablafit.features.myWorkouts.ui.SeancesFragment
import com.example.fsudouest.blablafit.features.nearby.di.NearByModule
import com.example.fsudouest.blablafit.features.nearby.ui.NearByFragment
import com.example.fsudouest.blablafit.features.profile.MyProfileFragment
import com.example.fsudouest.blablafit.features.profile.di.ProfileModule
import com.example.fsudouest.blablafit.features.profile.personalnfo.PersonalInfoFragment
import com.example.fsudouest.blablafit.features.workoutCreation.di.WorkoutCreationModule
import com.example.fsudouest.blablafit.features.workoutCreation.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector(modules = [MessagesModule::class])
    abstract fun contributeMessagesFragment(): MessagesFragment

    @ContributesAndroidInjector(modules = [ProfileModule::class])
    abstract fun contributeMyProfileFragment(): MyProfileFragment

    @ContributesAndroidInjector(modules = [MyWorkoutsModule::class])
    abstract fun contributeSeancesFragment(): SeancesFragment

    @ContributesAndroidInjector(modules = [NearByModule::class])
    abstract fun contributeTrouverUneSeanceFragment(): NearByFragment

    @ContributesAndroidInjector(modules = [WorkoutCreationModule::class])
    abstract fun contributeIndoorChoiceFragment(): IndoorChoiceFragment

    @ContributesAndroidInjector(modules = [WorkoutCreationModule::class])
    abstract fun contributeSearchLocationFragment(): SearchLocationFragment

    @ContributesAndroidInjector(modules = [WorkoutCreationModule::class])
    abstract fun contributeAddDateDurationFragment(): AddDateDurationFragment

    @ContributesAndroidInjector(modules = [WorkoutCreationModule::class])
    abstract fun contributeTypeSeanceFragment(): TypeSeanceFragment

    @ContributesAndroidInjector(modules = [WorkoutCreationModule::class])
    abstract fun contributeDescriptionFragment(): AddDescriptionFragment

    @ContributesAndroidInjector
    abstract fun contributePersonalInfoFragment(): PersonalInfoFragment
}