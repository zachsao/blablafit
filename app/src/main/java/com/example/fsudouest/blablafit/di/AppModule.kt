package com.example.fsudouest.blablafit.di

import dagger.Module


@Module(includes = [FirebaseModule::class, ViewModelModule::class])
class AppModule