package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.BlablaFitApp
import com.example.fsudouest.blablafit.service.LocationService
import com.example.fsudouest.blablafit.service.ResourceService
import dagger.Module
import dagger.Provides

@Module
object ServiceModule {

    @JvmStatic
    @Provides
    fun provideLocationService(app: BlablaFitApp): LocationService {
        return LocationService(app)
    }

    @JvmStatic
    @Provides
    fun provideResourceService(app: BlablaFitApp): ResourceService {
        return ResourceService(app.resources)
    }

}