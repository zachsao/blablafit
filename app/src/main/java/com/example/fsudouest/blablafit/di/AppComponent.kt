package com.example.fsudouest.blablafit.di

import android.app.Application
import com.example.fsudouest.blablafit.BlablaFitApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            FirebaseModule::class,
            ActivityModule::class,
            AppModule::class,
            ServiceModule::class
        ]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(blablaFitApp: BlablaFitApp)
}