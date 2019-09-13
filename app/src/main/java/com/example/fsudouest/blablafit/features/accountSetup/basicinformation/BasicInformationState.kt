package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

sealed class BasicInformationState {
    abstract val data: BasicInformationData
    data class Idle(override val data: BasicInformationData): BasicInformationState()
    data class Success(override val data: BasicInformationData): BasicInformationState()
    data class Error(override val data: BasicInformationData): BasicInformationState()
    data class DateUpdated(override val data: BasicInformationData): BasicInformationState()
    data class CityUpdated(override val data: BasicInformationData): BasicInformationState()
}

data class BasicInformationData(
        val name: String = "",
        val birthday: String = "",
        val city: String = "",
        val errors: List<ValidationError> = emptyList()
)