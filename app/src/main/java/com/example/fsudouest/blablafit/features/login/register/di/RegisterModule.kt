package com.example.fsudouest.blablafit.features.login.register.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.login.register.RegisterViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class RegisterModule {
    @Binds
    abstract fun bindRegisterViewModel(repoViewModel: RegisterViewModel): ViewModel
}