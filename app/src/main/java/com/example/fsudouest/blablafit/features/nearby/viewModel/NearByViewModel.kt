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
import com.google.firebase.firestore.Query
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val MOST_RECENT_LIMIT = 10L
class NearByViewModel @Inject constructor(private val mDatabase: FirebaseFirestore) : ViewModel() {
    private val stateLiveData = MutableLiveData<NearByState>()

    fun stateLiveData(): LiveData<NearByState> = stateLiveData

    init {
        stateLiveData.value = NearByState.Idle(NearByData(categories = CategoryViewItems.getCategoryViewItems()))
        getLatestWorkouts()
    }

    private fun getLatestWorkouts() {
        mDatabase.collection("workouts")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(MOST_RECENT_LIMIT)
                .get()
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
                placesAvailable = workout.maxParticipants - workout.participants.size,
                authorName = workout.nomAuteur,
                authorPhotoUrl = workout.photoAuteur,
                time = timeFormat.format(workout.date)
        )
    }
}