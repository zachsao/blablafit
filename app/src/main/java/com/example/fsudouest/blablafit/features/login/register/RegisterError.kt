package com.example.fsudouest.blablafit.features.login.register

import androidx.annotation.StringRes

sealed class RegisterError {
    data class FullNameEmpty(@StringRes val stringId: Int): RegisterError()
    data class FullNameIncomplete(@StringRes val stringId: Int): RegisterError()
    data class EmailEmpty(@StringRes val stringId: Int): RegisterError()
    data class PasswordEmpty(@StringRes val stringId: Int): RegisterError()
    data class ConfirmPasswordEmpty(@StringRes val stringId: Int): RegisterError()
    data class WrongPassword(@StringRes val stringId: Int): RegisterError()
    data class InvalidEmail(@StringRes val stringId: Int): RegisterError()
    data class InvalidPassword(@StringRes val stringId: Int): RegisterError()
}