package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupActivity
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.BasicInformationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountSetupActivityModule {
    @ContributesAndroidInjector
    abstract fun contributesAccountSetupActivity(): AccountSetupActivity

    @ContributesAndroidInjector
    abstract fun contributesBasicInformationFragment(): BasicInformationFragment
}