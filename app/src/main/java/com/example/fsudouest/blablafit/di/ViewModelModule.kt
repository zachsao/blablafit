package com.example.fsudouest.blablafit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(WorkoutsViewModel::class)
    abstract fun bindWorkoutsViewModel(repoViewModel: WorkoutsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NearByViewModel::class)
    abstract fun bindNearByViewModel(repoViewModel: NearByViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}