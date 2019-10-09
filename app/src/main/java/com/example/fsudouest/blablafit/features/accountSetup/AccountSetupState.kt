package com.example.fsudouest.blablafit.features.accountSetup

import android.net.Uri
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.ValidationError
import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.FitnessLevel

sealed class AccountSetupState {
    abstract val data: AccountSetupData
    data class Idle(override val data: AccountSetupData): AccountSetupState()
    data class BasicInfoValid(override val data: AccountSetupData): AccountSetupState()
    data class Error(override val data: AccountSetupData): AccountSetupState()
    data class DateUpdated(override val data: AccountSetupData): AccountSetupState()
    data class CityUpdated(override val data: AccountSetupData): AccountSetupState()
    data class LevelUpdated(override val data: AccountSetupData): AccountSetupState()
    data class GenderUpdated(override val data: AccountSetupData): AccountSetupState()
    data class PictureUpdated(override val data: AccountSetupData): AccountSetupState()
    data class Success(override val data: AccountSetupData): AccountSetupState()
    data class Loading(override val data: AccountSetupData): AccountSetupState()
    data class Failure(override val data: AccountSetupData): AccountSetupState()
    data class NameChanged(override val data: AccountSetupData) : AccountSetupState()
}

data class AccountSetupData(
        val name: String = "",
        val birthday: String = "",
        val city: String = "",
        val errors: List<ValidationError> = emptyList(),
        val gender: Boolean = true,
        val level: FitnessLevel = FitnessLevel.Beginner,
        val profilePictureUri: Uri? = null
)