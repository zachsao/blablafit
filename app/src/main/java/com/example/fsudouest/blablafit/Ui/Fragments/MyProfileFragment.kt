package com.example.fsudouest.blablafit.Ui.Fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import android.app.Activity.RESULT_OK
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMyProfileBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.model.User
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var mFirebaseStorage: FirebaseStorage

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth


    private var mProfilePhotosStorageReference: StorageReference? = null
    private var firebaseUser: FirebaseUser? = null
    private lateinit var profile_pic: ImageView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMyProfileBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_profile, container, false)


        firebaseUser = mFirebaseAuth.currentUser

        mProfilePhotosStorageReference = mFirebaseStorage.reference.child("profile_pictures")

        binding.user = User(firebaseUser?.displayName!!,firebaseUser?.email!!)
        profile_pic = binding.profilePicImageView


        if (firebaseUser!!.photoUrl != null) {
            Glide.with(context!!)
                    .load(firebaseUser!!.photoUrl!!.toString())
                    .into(profile_pic)
        }

        profile_pic.setOnClickListener { createPhotoUpdateDialog() }

        binding.decoButton.setOnClickListener { signOut() }
        return binding.root
    }


    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER)
    }

    private fun createPhotoUpdateDialog() {
        val builder = AlertDialog.Builder(activity!!)

        builder.setTitle(getString(R.string.update_profile_dialog_title))
        builder.setMessage(getString(R.string.update_photo_dialog_message))
        // Add the button
        builder.setPositiveButton("OK") { dialog, id ->
            // User clicked OK button
            chooseImageFromGallery()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialogInterface, i ->
            //
        }
        // Create the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            val selectedImageUri = data!!.data
            profile_pic.setImageURI(selectedImageUri)
            // Get a reference to store file at profile_pictures/<FILENAME>
            val photoRef = mProfilePhotosStorageReference!!.child(selectedImageUri!!.lastPathSegment!!)
            photoRef.putFile(selectedImageUri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!.fillInStackTrace()
                }
                // Continue with the task to get the download URL
                photoRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val photoUpdate = UserProfileChangeRequest.Builder()
                            .setPhotoUri(downloadUri)
                            .build()
                    firebaseUser!!.updateProfile(photoUpdate)
                } else {
                    Toast.makeText(activity, "Upload Failed - please try again", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(activity!!)
    }

    companion object {

        private val RC_PHOTO_PICKER = 2
    }

}
