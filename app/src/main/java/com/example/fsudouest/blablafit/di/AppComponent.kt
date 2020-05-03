package com.example.fsudouest.blablafit.di

import android.app.Activity
import android.app.Application
import com.example.fsudouest.blablafit.BlablaFitApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            FirebaseModule::class,
            ActivityModule::class,
            ServiceModule::class
        ]
)
interface AppComponent : AndroidInjector<BlablaFitApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: BlablaFitApp): Builder

        fun build(): AppComponent
    }

    fun activityInjector(): DispatchingAndroidInjector<Activity>
}