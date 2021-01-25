package com.example.fsudouest.blablafit.features.workoutCreation.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.Location
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.service.LocationService
import com.google.android.libraries.places.api.model.AddressComponent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class WorkoutCreationViewModel @ViewModelInject constructor(
        auth: FirebaseAuth,
        private val firestore: FirebaseFirestore,
        private val locationService: LocationService
) : ViewModel() {

    val workoutLiveData = MutableLiveData<Seance>()
    private val firebaseUser = auth.currentUser

    fun addAuthor(onComplete: () -> Unit) {
        val uid = firebaseUser?.uid ?: ""
        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    workoutLiveData.value?.idAuteur = uid
                    workoutLiveData.value?.nomAuteur = user?.nomComplet ?: ""
                    workoutLiveData.value?.photoAuteur = user?.photoUrl
                    onComplete()
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun saveWorkout(navigateToHomePage: () -> Unit, showToast: () -> Unit){
        val doc = firestore.collection("workouts").document()
        workoutLiveData.value?.id = doc.id
        doc.set(workoutLiveData.value!!)
                .addOnSuccessListener { navigateToHomePage() }
                .addOnFailureListener {
                    showToast()
                    TODO("Not implemented : Show a dialog")
                }
    }

    fun addWorkout(workout: Seance){
        workoutLiveData.value = workout
    }

    fun getCountry(onCountryRetrieved: (country: String?) -> Unit) {
        locationService.getCountryFromLastLocation { onCountryRetrieved(it) }
    }

    fun setLocation(placeName: String?, addressComponents: List<AddressComponent>?) {
        val location = Location(
                name = placeName,
                address = addressComponents?.filter { it.types.contains("street_number") || it.types.contains("route") }?.map { it.name }?.joinToString(),
                city = addressComponents?.find { it.types.containsAll(listOf("locality", "political")) }?.name,
                state = addressComponents?.find { it.types.contains("administrative_area_level_1") }?.name,
                country = addressComponents?.find { it.types.containsAll(listOf("country", "political")) }?.name,
                zipCode = addressComponents?.find { it.types.contains("postal_code") }?.name
        )
        workoutLiveData.value?.location = location
    }
}