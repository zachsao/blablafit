package com.example.fsudouest.blablafit.features.profile.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.features.profile.personalnfo.PersonalInfoFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ProfileModule {
    @Binds
    abstract fun bindProfileViewModel(repoViewModel: ProfileViewModel): ViewModel
}