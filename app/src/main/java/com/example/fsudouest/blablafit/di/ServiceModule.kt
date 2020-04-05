package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.service.LocationService
import com.example.fsudouest.blablafit.service.LocationServiceImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ServiceModule {

    @Binds
    abstract fun provideLocationService(locationService: LocationServiceImpl): LocationService
}