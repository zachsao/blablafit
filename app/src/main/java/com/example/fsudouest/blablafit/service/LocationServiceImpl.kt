package com.example.fsudouest.blablafit.service

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import timber.log.Timber
import javax.inject.Inject

class LocationServiceImpl @Inject constructor(private val application: Application) : LocationService {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

    override fun getLastKnownLocation(onLocationRetrieved: (location: Location?) -> Unit) {
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    // TODO("get city out of coordinates")
                    onLocationRetrieved(location)
                }
                .addOnFailureListener {
                    // TODO("not implemented: display dialog")
                    Timber.e(it)
                }
    }
}