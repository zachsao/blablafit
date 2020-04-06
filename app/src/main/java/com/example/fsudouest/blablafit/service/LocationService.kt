package com.example.fsudouest.blablafit.service

interface LocationService {
    fun getCityFromLastLocation(onLocationRetrieved: (city: String?) -> Unit)
    fun getCountryFromLastLocation(onLocationRetrieved: (country: String?) -> Unit)
}