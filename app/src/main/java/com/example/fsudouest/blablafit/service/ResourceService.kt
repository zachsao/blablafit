package com.example.fsudouest.blablafit.service

import android.content.res.Resources
import javax.inject.Inject

class ResourceService @Inject constructor(private val resources: Resources){
    fun getString(resId: Int): String {
        return resources.getString(resId)
    }
}