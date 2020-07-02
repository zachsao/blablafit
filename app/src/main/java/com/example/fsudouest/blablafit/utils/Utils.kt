package com.example.fsudouest.blablafit.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance

fun isMarshmallowOrNewer() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isPermissionGranted(activity: Activity, permission: String) = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

fun Seance.toWorkoutViewItem() = WorkoutViewItem(
        id = id,
        title = titre.joinToString(" - "),
        location = "${location.name} ${location.address} ${location.zipCode} ${location.city}",
        date = date,
        placesAvailable = maxParticipants - participants.size,
        duration = duree,
        author = nomAuteur,
        photoUrl = photoAuteur
)