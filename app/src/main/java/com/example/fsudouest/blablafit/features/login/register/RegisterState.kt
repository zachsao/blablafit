package com.example.fsudouest.blablafit.features.login.register

sealed class RegisterState {
    abstract val data: RegisterData

    data class Idle(override val data: RegisterData): RegisterState()
    data class ValidationError(override val data: RegisterData): RegisterState()
}

data class RegisterData(
        val fullName: String = "",
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val errors: List<RegisterError> = listOf()
)