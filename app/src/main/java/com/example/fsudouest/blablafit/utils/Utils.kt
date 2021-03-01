package com.example.fsudouest.blablafit.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import java.util.*

fun isMarshmallowOrNewer() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isPermissionGranted(activity: Activity, permission: String) = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

fun Seance.toWorkoutViewItem() = WorkoutViewItem(
        id = id,
        title = titre.joinToString(" - "),
        location = "${location.name} ${location.address} ${location.zipCode} ${location.city}",
        date = date,
        duration = duree,
        photoUrl = photoAuteur
)

fun Date.toLocalDateTime() : LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())