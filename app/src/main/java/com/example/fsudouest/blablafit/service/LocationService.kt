package com.example.fsudouest.blablafit.service

interface LocationService {
    fun getLastKnownLocation(onLocationRetrieved: (city: String?) -> Unit)
}