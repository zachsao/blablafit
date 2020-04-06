package com.example.fsudouest.blablafit.service

import android.app.Application
import android.location.Geocoder
import android.location.Location
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(private val application: Application) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    override fun getLastKnownLocation(onLocationRetrieved: (city: String?) -> Unit) {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.

                    onLocationRetrieved(getCityFromLocation(location))
                }
                .addOnFailureListener {
                    // TODO("not implemented: display dialog")
                    Timber.e(it)
                }
    }

    private fun getCityFromLocation(location: Location?): String? {
        val gcd = Geocoder(application, Locale.getDefault())
        return location?.let { gcd.getFromLocation(it.latitude, it.longitude, 1).single().locality }
    }
}