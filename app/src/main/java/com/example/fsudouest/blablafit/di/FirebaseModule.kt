package com.example.fsudouest.blablafit.di


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun provideDataBaseInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

}