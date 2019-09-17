package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.accountSetup.LevelFragment
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.BasicInformationFragment
import com.example.fsudouest.blablafit.features.accountSetup.genderSelection.GenderSelectionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountSetupActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesAccountSetupActivity(): AccountSetupActivity

    @ContributesAndroidInjector
    abstract fun contributesBasicInformationFragment(): BasicInformationFragment

    @ContributesAndroidInjector
    abstract fun contributesGenderSelectionFragment(): GenderSelectionFragment

    @ContributesAndroidInjector
    abstract fun contributesLevelFragment(): LevelFragment
}