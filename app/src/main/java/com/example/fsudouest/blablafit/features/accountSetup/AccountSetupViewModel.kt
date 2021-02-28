package com.example.fsudouest.blablafit.features.accountSetup

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.accountSetup.basicinformation.ValidationError
import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.FitnessLevel
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class AccountSetupViewModel @ViewModelInject constructor(
        private val firestore: FirebaseFirestore,
        auth: FirebaseAuth
): ViewModel() {

    private val stateLiveData = MutableLiveData<AccountSetupState>()
    private val uid = auth.currentUser?.uid ?: ""
    init {
        stateLiveData.value = AccountSetupState.Idle(AccountSetupData())
    }

    fun stateLiveData(): LiveData<AccountSetupState> = stateLiveData

    private fun previousStateData() = stateLiveData.value?.data ?: AccountSetupData()
    fun submitBasicInfoForm() {
        stateLiveData.value = checkForm(previousStateData())
    }

    private fun checkForm(data: AccountSetupData): AccountSetupState {
        val errors = mutableListOf<ValidationError>()
        if (data.city.isEmpty()) errors.add(ValidationError.CityEmpty)

        return if (errors.isEmpty()) AccountSetupState.BasicInfoValid(data)
            else AccountSetupState.Error(data.copy(errors = errors))
    }

    fun getUsername() {
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    val username = user?.nomComplet ?: ""
                    stateLiveData.value = AccountSetupState.NameAndPhotoLoaded(previousStateData().copy(name = username, profilePictureUri = Uri.parse(user?.photoUrl)))
                }
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

    fun updateStatePictureUri(uri: Uri) {
        stateLiveData.value = AccountSetupState.PictureUpdated(previousStateData().copy(profilePictureUri = uri))
    }

    fun updateUser() {
        stateLiveData.value = AccountSetupState.Loading(previousStateData())
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.toObject(User::class.java)
                    user?.let { saveUserToFirestore(it) }
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
                fitnessLevel = data.level,
                setup = true
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
}
