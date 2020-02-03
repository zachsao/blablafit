package com.example.fsudouest.blablafit.features.profile.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class ProfileModule {
    @Binds
    abstract fun bindProfileViewModel(repoViewModel: ProfileViewModel): ViewModel
}