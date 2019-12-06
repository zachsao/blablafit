package com.example.fsudouest.blablafit.features.splash

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity
}