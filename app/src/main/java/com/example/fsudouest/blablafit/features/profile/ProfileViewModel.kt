package com.example.fsudouest.blablafit.features.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileViewModel @Inject constructor(firebaseStorage: FirebaseStorage,
                                           firebaseAuth: FirebaseAuth,
                                           val firestore: FirebaseFirestore) : ViewModel() {

    private var profilePhotosStorageReference = firebaseStorage.reference.child("profile_pictures")
    private var firebaseUser = firebaseAuth.currentUser
    private val profileLiveData = MutableLiveData<User>()
    private val firebaseUserLiveData = MutableLiveData<FirebaseUser>()

    init {
        getUser()
    }

    fun user() = profileLiveData

    private fun getUser(){
        firestore.collection("users").document(firebaseUser?.uid ?: "")
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    profileLiveData.value = user
                }
                .addOnFailureListener {
                    Log.e("ProfileViewModel", it.message)
                }
    }

    fun uploadProfilePictureToStorage(selectedImageUri: Uri){
        val photoRef = profilePhotosStorageReference.child(selectedImageUri.lastPathSegment!!)
        photoRef.putFile(selectedImageUri).continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!.fillInStackTrace()
            }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val photoUpdate = UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUri)
                        .build()
                updateUsersPhotoUrl(downloadUri)
                firebaseUser?.updateProfile(photoUpdate)
            } else {
                Log.e("ProfileViewModel", task.exception?.message)
            }
        }
    }

    private fun updateUsersPhotoUrl(uri: Uri?) {
        firestore.collection("users").document(firebaseUser?.uid ?: "")
                .update("photoUrl", uri.toString())
                .addOnSuccessListener {
                    Log.d("ProfileViewModel", "successfully updated photoUrl")
                }
    }

}