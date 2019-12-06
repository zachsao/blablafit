package com.example.fsudouest.blablafit.features.messages.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.messages.viewModel.MessagesViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class MessagesModule {
    @Binds
    abstract fun bindMessageViewModel(repoViewModel: MessagesViewModel): ViewModel
}