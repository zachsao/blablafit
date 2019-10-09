package com.example.fsudouest.blablafit.model

import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.FitnessLevel

data class User(
        var nomComplet: String="",
        var email: String="",
        var photoUrl: String = "",
        var note: Double=0.0,
        val registrationToken: String = "",
        val city: String = "",
        val birthday: String = "",
        val gender: Boolean = true,
        val fitnessLevel: FitnessLevel = FitnessLevel.Beginner
)
