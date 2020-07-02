package com.example.fsudouest.blablafit.features.nearby.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.filters.WorkoutFilters
import com.example.fsudouest.blablafit.features.nearby.NearByData
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.service.LocationService
import com.example.fsudouest.blablafit.service.ResourceService
import com.example.fsudouest.blablafit.utils.toWorkoutViewItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import timber.log.Timber
import javax.inject.Inject


class NearByViewModel @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val auth: FirebaseAuth,
        private val locationService: LocationService,
        private val resourceService: ResourceService
) : ViewModel() {
    private val stateLiveData = MutableLiveData<NearByState>()

    fun stateLiveData(): LiveData<NearByState> = stateLiveData

    init {
        stateLiveData.value = NearByState.Idle(NearByData())
    }

    fun getWorkouts() {
        stateLiveData.value = NearByState.Loading(previousStateData())
        locationService.getCityFromLastLocation { city ->
            getWorkoutsInCity(city)
        }
    }

    fun searchWorkouts(search: String) {
        if (search.isNotBlank()) {
            stateLiveData.value =
                    NearByState.ResultsLoaded(previousStateData().copy(
                            searchResults = previousStateData().allWorkouts.filter { it.title.contains(search) }))
        } else stateLiveData.value = if (previousStateData().allWorkouts.isNotEmpty()) NearByState.WorkoutsLoaded(previousStateData()) else NearByState.EmptyWorkouts(previousStateData())
    }

    fun applyFilters(filters: WorkoutFilters) {
        filters.city?.let { getWorkoutsInCity(it) } ?: run {
            val filteredWorkouts = previousStateData().allWorkouts.filter { workoutViewItem -> filters.categories.map { resourceService.getString(it) }.intersect(workoutViewItem.seance.titre.split(" - ")).isNotEmpty() }
            stateLiveData.value = when {
                filteredWorkouts.isNotEmpty() -> NearByState.ResultsLoaded(previousStateData().copy(searchResults = filteredWorkouts))
                filters.categories.isNotEmpty() && filteredWorkouts.isEmpty() -> NearByState.EmptyWorkouts(previousStateData().copy(searchResults = filteredWorkouts))
                else -> NearByState.WorkoutsLoaded(previousStateData())
            }
        }
    }

    private fun getWorkoutsInCity(city: String?) {
        firestore.collection("workouts")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("location.city", city)
                .get()
                .addOnSuccessListener { snapshot ->
                    val workouts = snapshot.documents.mapNotNull { it.toObject(Seance::class.java) }
                            .filter { it.idAuteur != auth.currentUser?.uid }
                            .map { seance -> seance.toWorkoutViewItem() }

                    stateLiveData.value = if (workouts.isNotEmpty())
                        NearByState.WorkoutsLoaded(previousStateData().copy(allWorkouts = workouts, city = city))
                    else
                        NearByState.EmptyWorkouts(previousStateData().copy(city = city))
                }
                .addOnFailureListener { Timber.e(it) }
    }

    private fun previousStateData() = stateLiveData.value?.data ?: NearByData()
}