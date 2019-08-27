package com.example.fsudouest.blablafit.features.login.signIn

sealed class SignInState {
    abstract val data: SignInData

    data class Idle(override val data: SignInData) : SignInState()
    data class ValidationError(override val data: SignInData) : SignInState()
    data class TextChanged(override val data: SignInData) : SignInState()
    data class Success(override val data: SignInData) : SignInState()
    data class Failure(override val data: SignInData, val message: String) : SignInState()
}

data class SignInData(
        val email: String = "",
        val password: String = "",
        val errors: List<SignInError> = emptyList()
)
