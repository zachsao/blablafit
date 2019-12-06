package com.example.fsudouest.blablafit.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class ViewModelFactory<VM : ViewModel> @Inject constructor(private val viewModel: Lazy<VM>) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModel.get() as T
    }
}