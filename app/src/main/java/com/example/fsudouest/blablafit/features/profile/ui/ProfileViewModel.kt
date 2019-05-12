package com.example.fsudouest.blablafit.features.profile.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor(firebaseStorage: FirebaseStorage,
                                           firebaseAuth: FirebaseAuth) : ViewModel() {

    private var profilePhotosStorageReference = firebaseStorage.reference.child("profile_pictures")
    private var firebaseUser = firebaseAuth.currentUser
    private val profileLiveData = MutableLiveData<User>()
    private val firebaseUserLiveData = MutableLiveData<FirebaseUser>()

    init {
        firebaseUser?.displayName?.let {
            profileLiveData.value = User(it)
        }
        firebaseUserLiveData.value = firebaseUser
    }

    fun user() = profileLiveData

    fun firebaseUser() = firebaseUserLiveData

    fun uploadProfilePictureToStorage(selectedImageUri: Uri){
        val photoRef = profilePhotosStorageReference.child(selectedImageUri.lastPathSegment!!)
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
                firebaseUser?.updateProfile(photoUpdate)
            } else {
                Log.e("ProfileViewModel", task.exception?.message)
            }
        }
    }

}