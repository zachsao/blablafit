package com.example.fsudouest.blablafit.features.nearby.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.service.ResourceService
import com.example.fsudouest.blablafit.features.filters.WorkoutFilters
import com.example.fsudouest.blablafit.features.nearby.NearByData
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.nearby.ui.LatestWorkoutViewItem
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.service.LocationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


private const val MOST_RECENT_LIMIT = 10

class NearByViewModel @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
        private val locationService: LocationService,
        private val resourceService: ResourceService
) : ViewModel() {
    private val stateLiveData = MutableLiveData<NearByState>()

    fun stateLiveData(): LiveData<NearByState> = stateLiveData

    init {
        stateLiveData.value = NearByState.Idle(NearByData(categories = CategoryViewItems.getCategoryViewItems()))
    }

    fun getLatestWorkouts() {
        stateLiveData.value = NearByState.Loading(previousStateData())
        locationService.getCityFromLastLocation { city ->
            getWorkoutsInCity(city)
        }
    }

    fun searchWorkouts(search: String) {
        if (search.isNotBlank()) {
            stateLiveData.value =
                    NearByState.ResultsLoaded(previousStateData().copy(
                            searchResults = previousStateData().allWorkouts.filter { it.seance.titre.contains(search, true) }))
        } else stateLiveData.value = if (previousStateData().allWorkouts.isNotEmpty()) NearByState.Idle(previousStateData()) else NearByState.EmptyWorkouts(previousStateData())
    }

    fun applyFilters(filters: WorkoutFilters) {
        filters.city?.let { getWorkoutsInCity(it) } ?: run {
            val filteredWorkouts = previousStateData().allWorkouts.filter { workoutViewItem -> filters.categories.map { resourceService.getString(it) }.intersect(workoutViewItem.seance.titre.split(" - ")).isNotEmpty() }
            stateLiveData.value = when {
                filteredWorkouts.isNotEmpty() -> NearByState.ResultsLoaded(previousStateData().copy(searchResults = filteredWorkouts))
                filters.categories.isNotEmpty() && filteredWorkouts.isEmpty() -> NearByState.EmptyWorkouts(previousStateData().copy(searchResults = filteredWorkouts))
                else -> NearByState.LatestWorkoutsLoaded(previousStateData())
            }
        }
    }

    private fun getWorkoutsInCity(city: String?) {
        firestore.collection("workouts")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("location.city", city)
                .get()
                .addOnSuccessListener { snapshot ->
                    val results = snapshot.documents.mapNotNull { it.toObject(Seance::class.java) }
                            .filter { it.idAuteur != auth.currentUser?.uid }

                    val latestWorkouts = results
                            .map { seance -> modelToLatestWorkoutViewItem(seance) }
                            .take(MOST_RECENT_LIMIT)

                    val allWorkouts = results
                            .map { seance -> WorkoutViewItem(seance) }

                    stateLiveData.value = if (allWorkouts.isNotEmpty())
                        NearByState.LatestWorkoutsLoaded(previousStateData().copy(latestWorkouts = latestWorkouts, allWorkouts = allWorkouts, city = city))
                    else
                        NearByState.EmptyWorkouts(previousStateData().copy(city = city))
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun previousStateData() = stateLiveData.value?.data ?: NearByData()

    private fun modelToLatestWorkoutViewItem(workout: Seance): LatestWorkoutViewItem {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.CANADA)
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        val address = "${workout.location.name}, ${workout.location.address}, ${workout.location.zipCode}, ${workout.location.city}"
        return LatestWorkoutViewItem(
                id = workout.id,
                title = workout.titre,
                address = address,
                placesAvailable = workout.maxParticipants - workout.participants.size,
                authorName = workout.nomAuteur,
                authorPhotoUrl = workout.photoAuteur,
                time = timeFormat.format(workout.date),
                date = dateFormat.format(workout.date)
        )
    }
}