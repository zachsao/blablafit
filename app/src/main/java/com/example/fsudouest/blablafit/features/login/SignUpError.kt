package com.example.fsudouest.blablafit.features.login

sealed class SignUpError {
    object FirstNameEmpty: SignUpError()
    object LastNameEmpty: SignUpError()
    object EmailEmpty: SignUpError()
    object PasswordEmpty: SignUpError()
    object ConfirmPasswordEmpty: SignUpError()
    object PasswordTooShort: SignUpError()
    object WrongPassword: SignUpError()
    object InvalidEmail: SignUpError()
    object InvalidPassword: SignUpError()
}