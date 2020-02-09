package com.example.fsudouest.blablafit.features.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.profile.myProfile.ProfileData
import com.example.fsudouest.blablafit.features.profile.myProfile.ProfileState
import com.example.fsudouest.blablafit.features.profile.myProfile.buddies.BuddyViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(firebaseStorage: FirebaseStorage, firebaseAuth: FirebaseAuth, val firestore: FirebaseFirestore) : ViewModel() {

    private var profilePhotosStorageReference = firebaseStorage.reference.child("profile_pictures")
    private var firebaseUser = firebaseAuth.currentUser
    private val stateLiveData = MutableLiveData<ProfileState>()


    fun stateLiveData(): LiveData<ProfileState> = stateLiveData


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

    fun getBuddies() {
        firestore.collection("workouts")
                .whereEqualTo("idAuteur", firebaseUser?.uid ?: "")
                .whereLessThan("date", Calendar.getInstance().time)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val buddiesIds = querySnapshot.documents
                            .mapNotNull { it.toObject(Seance::class.java) }
                            .flatMap { it.participants.keys }

                    fetchUsers(buddiesIds)
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun fetchUsers(userIds: List<String>) {
        firestore.collection("users")
                .whereIn("uid", userIds)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val users = querySnapshot.documents
                            .mapNotNull { it.toObject(User::class.java) }
                            .map { BuddyViewItem(it.uid, it.nomComplet, it.photoUrl) }

                    stateLiveData.value = if (users.isNotEmpty()) ProfileState.BuddiesLoaded(previousData().copy(buddies = users))
                    else ProfileState.EmptyBuddies(previousData())
                }
                .addOnFailureListener { Timber.e(it) }
    }

    fun getUser(id: String = firebaseUser?.uid ?: ""){
        firestore.collection("users").document(id)
                .get(Source.CACHE)
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    stateLiveData.value = if (id == firebaseUser?.uid) ProfileState.UserLoaded(previousData().copy(currentUser = user))
                    else ProfileState.UserLoaded(previousData().copy(user = user))
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    private fun updateUsersPhotoUrl(uri: Uri?) {
        firestore.collection("users").document(firebaseUser?.uid ?: "")
                .update("photoUrl", uri.toString())
                .addOnSuccessListener {
                    Timber.d("Successfully updated photoUrl")
                }
    }

    private fun previousData() = stateLiveData.value?.data ?: ProfileData()

}