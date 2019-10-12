package com.example.fsudouest.blablafit.features.nearby.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.NearByData
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.nearby.ui.LatestWorkoutViewItem
import com.example.fsudouest.blablafit.model.Seance
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class NearByViewModel @Inject constructor(private val mDatabase: FirebaseFirestore) : ViewModel() {

    private val stateLiveData = MutableLiveData<NearByState>()

    val filteredList = ArrayList<Seance?>()
    private val fullList = ArrayList<Seance?>()
    fun stateLiveData(): LiveData<NearByState> = stateLiveData

    init {
         stateLiveData.value = NearByState.Idle(NearByData(categories = CategoryViewItems.getCategoryViewItems()))
        updateWorkouts()
    }

    fun updateWorkouts() {
        val ref = mDatabase.collection("workouts")
        ref.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val results = task.result!!.documents
                                .map { it.toObject(Seance::class.java) }
                                .map { seance -> seance?.let { modelToViewItem(it) } ?: LatestWorkoutViewItem() }
                        stateLiveData.value = NearByState.LatestWorkoutsLoaded(previousStateData().copy(workouts = results))
                    } else {
                        Timber.e(task.exception)
                    }
                }
    }


    private fun previousStateData() = stateLiveData.value?.data ?: NearByData()

    private fun modelToViewItem(workout: Seance): LatestWorkoutViewItem {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.CANADA)
        return LatestWorkoutViewItem(
                id = workout.id,
                title = workout.titre,
                address = workout.lieu,
                placesAvailable = workout.maxParticipants,
                authorName = workout.nomAuteur,
                authorPhotoUrl = workout.photoAuteur,
                time = timeFormat.format(workout.date)
        )
    }

    fun search(query: String) {
        val wanted = fullList.filter { workout -> workout!!.titre.toLowerCase().contains(query) } as ArrayList<Seance?>
        filteredList.clear()
        filteredList.addAll(wanted)
    }
}