package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.Ui.Fragments.MessagesFragment
import com.example.fsudouest.blablafit.Ui.Fragments.MyProfileFragment
import com.example.fsudouest.blablafit.Ui.Fragments.SeancesFragment
import com.example.fsudouest.blablafit.Ui.Fragments.TrouverUneSeanceFragment
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
}