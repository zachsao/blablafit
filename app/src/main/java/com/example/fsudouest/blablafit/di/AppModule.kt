package com.example.fsudouest.blablafit.di

import android.content.Context
import android.content.SharedPreferences
import com.example.fsudouest.blablafit.BlablaFitApp
import dagger.Module
import dagger.Provides

@Module
object AppModule {

    @JvmStatic
    @Provides
    fun provideSharedPreference(application: BlablaFitApp): SharedPreferences {
        return application.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
    }
}