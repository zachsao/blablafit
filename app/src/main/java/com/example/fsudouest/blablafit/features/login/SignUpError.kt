package com.example.fsudouest.blablafit.features.login

import androidx.annotation.StringRes

sealed class SignUpError {
    object FirstNameEmpty: SignUpError()
    object LastNameEmpty: SignUpError()
    object EmailEmpty: SignUpError()
    object PasswordEmpty: SignUpError()
    object ConfirmPasswordEmpty: SignUpError()
    object PasswordTooShort: SignUpError()
    object WrongPassword: SignUpError()
    data class InvalidEmail(@StringRes val stringId: Int): SignUpError()
    object InvalidPassword: SignUpError()
}