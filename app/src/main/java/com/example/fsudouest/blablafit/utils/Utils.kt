package com.example.fsudouest.blablafit.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

fun isMarshmallowOrNewer() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

fun isPermissionGranted(activity: Activity, permission: String) = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED