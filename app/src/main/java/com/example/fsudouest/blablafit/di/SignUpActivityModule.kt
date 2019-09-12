package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.login.SignUpActivity
import com.example.fsudouest.blablafit.features.login.register.RegisterFragment
import com.example.fsudouest.blablafit.features.login.signIn.SignInFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignUpActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment
}