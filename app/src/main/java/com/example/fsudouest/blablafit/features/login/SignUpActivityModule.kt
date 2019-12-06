package com.example.fsudouest.blablafit.features.login

import com.example.fsudouest.blablafit.features.login.register.RegisterFragment
import com.example.fsudouest.blablafit.features.login.register.di.RegisterModule
import com.example.fsudouest.blablafit.features.login.signIn.SignInFragment
import com.example.fsudouest.blablafit.features.login.signIn.di.SignInModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SignUpActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSignUpActivity(): SignUpActivity

    @ContributesAndroidInjector(modules = [SignInModule::class])
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector(modules = [RegisterModule::class])
    abstract fun contributeRegisterFragment(): RegisterFragment
}