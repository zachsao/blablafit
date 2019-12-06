package com.example.fsudouest.blablafit.features.category.di

import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.category.CategoryViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class CategoryModule {
    @Binds
    abstract fun bindCategoryViewModel(repoViewModel: CategoryViewModel): ViewModel
}