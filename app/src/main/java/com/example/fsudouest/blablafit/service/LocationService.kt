package com.example.fsudouest.blablafit.service

import android.location.Location

interface LocationService {
    fun getLastKnownLocation(onLocationRetrieved: (location: Location?) -> Unit)
}