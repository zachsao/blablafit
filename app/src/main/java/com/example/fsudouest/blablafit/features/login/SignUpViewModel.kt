package com.example.fsudouest.blablafit.features.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SignUpViewModel @Inject constructor(): ViewModel() {

    private val stateLiveData = MutableLiveData<SignUpState>()

    fun stateLiveData(): LiveData<SignUpState> = stateLiveData

    init {
        stateLiveData.value = SignUpState.Idle(SignUpData())
    }

    fun submitForm(){
        val errors = mutableListOf<SignUpError>()
        val data = stateLiveData.value?.data
        if (data?.firstName.isNullOrEmpty()) errors.add(SignUpError.FirstNameEmpty)
        if (data?.lastName.isNullOrEmpty()) errors.add(SignUpError.LastNameEmpty)
        if (data?.email.isNullOrEmpty()) errors.add(SignUpError.EmailEmpty)
        if (data?.password.isNullOrEmpty()) errors.add(SignUpError.PasswordEmpty)
        if (data?.confirmPassword.isNullOrEmpty()) errors.add(SignUpError.ConfirmPasswordEmpty)

        stateLiveData.value = SignUpState.ValidationError(data?.copy(errors = errors) ?: SignUpData())
    }
}