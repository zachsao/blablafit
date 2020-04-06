package com.example.fsudouest.blablafit.features.nearby.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.NearByData
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.nearby.ui.LatestWorkoutViewItem
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.service.LocationServiceImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


private const val MOST_RECENT_LIMIT = 10L

class NearByViewModel @Inject constructor(
        private val firestore: FirebaseFirestore, private val auth: FirebaseAuth, private val locationService: LocationServiceImpl
) : ViewModel() {
    private val stateLiveData = MutableLiveData<NearByState>()

    fun stateLiveData(): LiveData<NearByState> = stateLiveData

    init {
        stateLiveData.value = NearByState.Idle(NearByData(categories = CategoryViewItems.getCategoryViewItems()))
    }

    fun getLatestWorkouts() {
        stateLiveData.value = NearByState.Loading(previousStateData())
        locationService.getCityFromLastLocation { city ->
            firestore.collection("workouts")
                    .orderBy("date", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        val results = snapshot.documents.mapNotNull { it.toObject(Seance::class.java) }
                                .filter { it.idAuteur != auth.currentUser?.uid }

                        val latestWorkouts = results
                                .map { seance -> modelToLatestWorkoutViewItem(seance) }
                                .filterIndexed { index, _ -> index < MOST_RECENT_LIMIT }

                        val allWorkouts = results
                                .map { seance -> WorkoutViewItem(seance) }

                        stateLiveData.value = NearByState.LatestWorkoutsLoaded(previousStateData().copy(latestWorkouts = latestWorkouts, allWorkouts = allWorkouts, city = city))
                    }
                    .addOnFailureListener { Timber.e(it) }
        }
    }

    private fun previousStateData() = stateLiveData.value?.data ?: NearByData()

    private fun modelToLatestWorkoutViewItem(workout: Seance): LatestWorkoutViewItem {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.CANADA)
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        return LatestWorkoutViewItem(
                id = workout.id,
                title = workout.titre,
                address = workout.lieu,
                placesAvailable = workout.maxParticipants - workout.participants.size,
                authorName = workout.nomAuteur,
                authorPhotoUrl = workout.photoAuteur,
                time = timeFormat.format(workout.date),
                date = dateFormat.format(workout.date)
        )
    }

    fun searchWorkouts(search: String) {
        if (search.isNotBlank()) {
            stateLiveData.value =
                    NearByState.ResultsLoaded(previousStateData().copy(
                            searchResults = previousStateData().allWorkouts.filter { it.seance.titre.contains(search, true) }))
        } else stateLiveData.value = NearByState.Idle(previousStateData())
    }
}