package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.accountSetup.di.AccountSetupModule
import com.example.fsudouest.blablafit.features.conversation.ConversationActivityModule
import com.example.fsudouest.blablafit.features.home.MainActivityModule
import com.example.fsudouest.blablafit.features.login.SignUpActivityModule
import com.example.fsudouest.blablafit.features.splash.SplashActivityModule
import com.example.fsudouest.blablafit.features.workoutDetails.DetailsSeanceActivityModule
import com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests.RequestsModule
import dagger.Module

@Module(includes = [
    MainActivityModule::class,
    DetailsSeanceActivityModule::class,
    SignUpActivityModule::class,
    ConversationActivityModule::class,
    SplashActivityModule::class,
    AccountSetupModule::class,
    RequestsModule::class]
)
class ActivityModule