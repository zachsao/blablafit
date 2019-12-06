package com.example.fsudouest.blablafit.features.accountSetup.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.BasicInformationFragment
import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.LevelFragment
import com.example.fsudouest.blablafit.features.accountSetup.genderSelection.GenderSelectionFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountSetupModule {
    @ContributesAndroidInjector
    abstract fun contributesAccountSetupActivity(): AccountSetupActivity

    @Binds
    abstract fun bindAccountSetupViewModel(repoViewModel: AccountSetupViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesBasicInformationFragment(): BasicInformationFragment

    @ContributesAndroidInjector
    abstract fun contributesGenderSelectionFragment(): GenderSelectionFragment

    @ContributesAndroidInjector
    abstract fun contributesLevelFragment(): LevelFragment
}