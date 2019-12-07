package com.example.fsudouest.blablafit.features.splash

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @Binds
    abstract fun bindSplashViewModel(repoViewModel: SplashViewModel): ViewModel
}