package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

sealed class ValidationError {
    object CityEmpty: ValidationError()
}