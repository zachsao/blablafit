package com.example.fsudouest.blablafit.di

import com.example.fsudouest.blablafit.features.messages.conversation.ConversationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ConversationActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): ConversationActivity
}