package com.example.fsudouest.blablafit.features.login.signIn.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.login.signIn.SignInViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SignInModule {
    @Binds
    abstract fun bindSignUpViewModel(repoViewModel: SignInViewModel): ViewModel
}