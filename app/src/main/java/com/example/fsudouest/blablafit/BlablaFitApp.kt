package com.example.fsudouest.blablafit

import android.app.Application
import com.example.fsudouest.blablafit.di.AppInjector
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class BlablaFitApp : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AndroidThreeTen.init(this)
    }

    override fun androidInjector() = dispatchingAndroidInjector
}