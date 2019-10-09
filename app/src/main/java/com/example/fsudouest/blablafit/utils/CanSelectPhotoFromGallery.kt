package com.example.fsudouest.blablafit.utils

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fsudouest.blablafit.R

const val RC_PHOTO_PICKER = 2
interface CanSelectPhotoFromGallery {

    fun createPhotoUpdateDialog(activity: Activity, fragment: Fragment) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Ajouter une photo de profil")
        builder.setMessage("Choisir une photo depuis la gallerie")
        builder.setPositiveButton("OK") { dialog, id ->
            chooseImageFromGallery(fragment)
        }
        builder.setNegativeButton(activity.resources.getString(R.string.cancel)) { dialogInterface, i -> }
        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    fun chooseImageFromGallery(fragment: Fragment) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        fragment.startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER)
    }
}