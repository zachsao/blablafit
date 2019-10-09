package com.example.fsudouest.blablafit.features.accountSetup

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.ValidationError
import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.FitnessLevel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import javax.inject.Inject

class AccountSetupViewModel @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
        private val storage: FirebaseStorage
): ViewModel() {

    private val stateLiveData = MutableLiveData<AccountSetupState>()
    private val uid = auth.currentUser?.uid ?: ""
    init {
        stateLiveData.value = AccountSetupState.Idle(AccountSetupData())
    }

    fun stateLiveData(): LiveData<AccountSetupState> = stateLiveData

    fun dateChanged(date: String) {
        stateLiveData.value = AccountSetupState.DateUpdated(previousStateData()
                .copy(
                        birthday = date,
                        errors = previousStateData().errors.filter { it !is ValidationError.BirthDateEmpty }
                )
        )
    }
    private fun previousStateData() = stateLiveData.value?.data ?: AccountSetupData()
    fun submitBasicInfoForm() {
        stateLiveData.value = checkForm(previousStateData())
    }

    private fun checkForm(data: AccountSetupData): AccountSetupState {
        val errors = mutableListOf<ValidationError>()
        if (data.birthday.isEmpty()) errors.add(ValidationError.BirthDateEmpty)
        if (data.city.isEmpty()) errors.add(ValidationError.CityEmpty)

        return if (errors.isEmpty()) AccountSetupState.BasicInfoValid(data)
            else AccountSetupState.Error(data.copy(errors = errors))
    }

    fun updateCity(placeName: String?) {
        stateLiveData.value = AccountSetupState.CityUpdated(previousStateData()
                .copy(
                        city = placeName ?: "",
                        errors = previousStateData().errors.filter { it !is ValidationError.CityEmpty }
                )
        )
    }

    fun updateGender(gender: Boolean) {
        val previousGender = previousStateData().gender
        if (previousGender != gender) stateLiveData.value = AccountSetupState.GenderUpdated(previousStateData().copy(
                gender = gender
        ))
    }

    fun updateLevel(level: FitnessLevel) {
        stateLiveData.value = AccountSetupState.LevelUpdated(previousStateData().copy(level = level))
    }

    fun idle() {
        stateLiveData.value = AccountSetupState.Idle(previousStateData())
    }

    fun saveProfilePictureToStorage(user: User?){
        val uri = previousStateData().profilePictureUri
        val photoRef = storage.reference.child("profile_pictures").child(uri?.lastPathSegment!!)
        photoRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                throw task.exception!!.fillInStackTrace()
            }
            photoRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                user?.let { saveUserToFirestore(it.copy(photoUrl = downloadUri.toString())) }
            } else {
                Timber.e(task.exception)
            }
        }
    }

    fun updateStatePictureUri(uri: Uri) {
        stateLiveData.value = AccountSetupState.PictureUpdated(previousStateData().copy(profilePictureUri = uri))
    }

    fun updateUser() {
        stateLiveData.value = AccountSetupState.Loading(previousStateData())
        val data = previousStateData()
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    data.profilePictureUri?.let { saveProfilePictureToStorage(user) }
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    private fun saveUserToFirestore(user: User) {
        val data = previousStateData()
        val updatedUser = user.copy(
                birthday = data.birthday,
                city = data.city,
                gender = data.gender,
                fitnessLevel = data.level
        )
        firestore.collection("users").document(uid)
                .set(updatedUser)
                .addOnSuccessListener {
                    stateLiveData.value = AccountSetupState.Success(previousStateData())
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun completeSetup(prefs: SharedPreferences?) {
        prefs?.edit()?.putBoolean("IsSetup:${auth.currentUser?.uid}", true)?.apply()
    }
}
