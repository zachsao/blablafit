package com.example.fsudouest.blablafit.features.login

sealed class SignUpState() {
    abstract val data: SignUpData

    data class Idle(override val data: SignUpData): SignUpState()
    data class ValidationError(override val data: SignUpData): SignUpState()
}

data class SignUpData(
        val firstName: String = "",
        val lastName: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val errors: List<SignUpError> = emptyList()
)
