package com.example.fsudouest.blablafit.features.conversation

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ConversationActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): ConversationActivity

    @Binds
    abstract fun bindConversationViewModel(repoViewModel: ConversationViewModel): ViewModel
}