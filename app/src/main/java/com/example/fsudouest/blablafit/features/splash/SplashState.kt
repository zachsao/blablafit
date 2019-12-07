package com.example.fsudouest.blablafit.features.splash

sealed class SplashState {
    object Loading: SplashState()
    data class UserLoaded(val isSetup: Boolean?): SplashState()
}
