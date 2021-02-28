package com.example.fsudouest.blablafit.features.nearby.viewModel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.filters.WorkoutFilters
import com.example.fsudouest.blablafit.features.nearby.NearByData
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.service.LocationService
import com.example.fsudouest.blablafit.service.ResourceService
import com.example.fsudouest.blablafit.utils.toWorkoutViewItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import javax.inject.Inject

@HiltViewModel
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
            getWorkoutsInCity(city, null, emptyList())
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
        filters.city?.let { city -> getWorkoutsInCity(city, filters.date, getCategoriesNamesFromIds(filters.categories)) } ?:
        run {
            val filteredWorkouts = previousStateData().allWorkouts.filter { workoutViewItem -> getCategoriesNamesFromIds(filters.categories).intersect(workoutViewItem.title.split(" - ")).isNotEmpty() }
            filters.date?.let { date -> filteredWorkouts.apply { filter { it.date == getDateInstance(DateFormat.LONG).parse(date) } } }
            stateLiveData.value = when {
                filteredWorkouts.isNotEmpty() -> NearByState.ResultsLoaded(previousStateData().copy(searchResults = filteredWorkouts))
                filters.categories.isNotEmpty() && filteredWorkouts.isEmpty() -> NearByState.EmptyWorkouts(previousStateData().copy(searchResults = filteredWorkouts))
                else -> NearByState.WorkoutsLoaded(previousStateData())
            }
        }
    }

    private fun getWorkoutsInCity(city: String?, date: String?, categories: List<String>) {
        createQuery(city, date, categories).get()
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

    private fun getCategoriesNamesFromIds(@StringRes ids: List<Int>) = ids.map { resourceService.getString(it) }

    private fun createQuery(city: String?, date: String?, categories: List<String>): Query {
        var query = firestore.collection("workouts")
                .whereEqualTo("location.city", city)

        if (categories.isNotEmpty()) query = query.whereArrayContainsAny("titre", categories)
        query = if (date.isNullOrBlank())
            query.orderBy("date", Query.Direction.DESCENDING)
        else
            query.whereEqualTo("date", getDateInstance(DateFormat.LONG).parse(date))

        return query
    }

    private fun previousStateData() = stateLiveData.value?.data ?: NearByData()
}