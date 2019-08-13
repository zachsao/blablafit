package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.login.BasicInformationFragment
import com.example.fsudouest.blablafit.features.login.SignUpActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignUpActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun contributeBasicInformationFragment(): BasicInformationFragment
}