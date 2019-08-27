package com.example.fsudouest.blablafit.features.login.signIn

import androidx.annotation.StringRes

sealed class SignInError {
    object EmailEmpty: SignInError()
    object PasswordEmpty: SignInError()
    data class InvalidEmail(@StringRes val stringId: Int): SignInError()
    object InvalidPassword: SignInError()
}