package com.example.fsudouest.blablafit.di

import android.content.Context
import com.example.fsudouest.blablafit.service.LocationService
import com.example.fsudouest.blablafit.service.ResourceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideLocationService(@ApplicationContext context: Context): LocationService {
        return LocationService(context)
    }

    @Provides
    fun provideResourceService(@ApplicationContext context: Context): ResourceService {
        return ResourceService(context.resources)
    }

}