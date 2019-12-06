package com.example.fsudouest.blablafit.features.nearby.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class NearByModule {
    @Binds
    abstract fun bindNearByViewModel(repoViewModel: NearByViewModel): ViewModel
}