package com.example.fsudouest.blablafit.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fsudouest.blablafit.features.login.register.RegisterViewModel
import com.example.fsudouest.blablafit.features.login.signIn.SignInViewModel
import com.example.fsudouest.blablafit.features.messages.conversation.ConversationViewModel
import com.example.fsudouest.blablafit.features.messages.viewModel.MessagesViewModel
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.features.workoutDetails.DetailsViewModel
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
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(repoViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(repoViewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindSignUpViewModel(repoViewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    abstract fun bindMessageViewModel(repoViewModel: MessagesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConversationViewModel::class)
    abstract fun bindConversationViewModel(repoViewModel: ConversationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(repoViewModel: RegisterViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}