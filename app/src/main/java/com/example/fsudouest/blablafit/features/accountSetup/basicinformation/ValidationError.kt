package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

sealed class ValidationError {
    object BirthDateEmpty: ValidationError()
    object CityEmpty: ValidationError()
}