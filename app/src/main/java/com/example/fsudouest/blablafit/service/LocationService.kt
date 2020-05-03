package com.example.fsudouest.blablafit.service

import android.location.Geocoder
import android.location.Location
import com.example.fsudouest.blablafit.BlablaFitApp
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(private val application: BlablaFitApp) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    fun getCityFromLastLocation(onLocationRetrieved: (city: String?) -> Unit) {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    onLocationRetrieved(extractCityFromLocation(location))
                }
                .addOnFailureListener {
                    // TODO("not implemented: display dialog")
                    Timber.e(it)
                }
    }

    fun getCountryFromLastLocation(onLocationRetrieved: (country: String?) -> Unit) {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    onLocationRetrieved(extractCountryFromLocation(location))
                }
                .addOnFailureListener {
                    // TODO("not implemented: handle failure")
                    Timber.e(it)
                }
    }

    private fun extractCityFromLocation(location: Location?): String? {
        val gcd = Geocoder(application, Locale.getDefault())
        return location?.let { gcd.getFromLocation(it.latitude, it.longitude, 1).single().locality }
    }

    private fun extractCountryFromLocation(location: Location?): String? {
        val gcd = Geocoder(application, Locale.getDefault())
        return location?.let { gcd.getFromLocation(it.latitude, it.longitude, 1).single().countryCode }
    }
}