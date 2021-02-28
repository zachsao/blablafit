package com.example.fsudouest.blablafit.features.workoutDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.toLocalDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(mDatabase: FirebaseFirestore, auth: FirebaseAuth) : ViewModel() {

    private val workoutsRef = mDatabase.collection("workouts")
    private val stateLiveData = MutableLiveData<WorkoutDetailsState>()

    private val user = auth.currentUser

    init {
        stateLiveData.value = WorkoutDetailsState.Idle(WorkoutDetailsData())
    }

    fun stateLiveData(): LiveData<WorkoutDetailsState> = stateLiveData

    fun getWorkoutDetails(id: String) {
        stateLiveData.value = WorkoutDetailsState.Loading(previousStateData())
        workoutsRef
                .document(id)
                .get()
                .addOnSuccessListener {
                    val workout = it.toObject(Seance::class.java)!!
                    val newData = previousStateData().copy(
                            title = workout.titre.joinToString(" - "),
                            authorId = workout.idAuteur,
                            authorName = workout.nomAuteur,
                            authorPictureUrl = workout.photoAuteur,
                            time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(workout.date.toLocalDateTime()),
                            date = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(workout.date.toLocalDateTime()),
                            duration = workout.duree,
                            location = workout.location.let { location -> "${location.name}, ${location.address}, ${location.zipCode}, ${location.city}" },
                            placesAvailable = workout.maxParticipants - workout.participants.size,
                            description = workout.description,
                            participants = workout.participants
                    )
                    stateLiveData.value = reduceWorkoutLoaded(workout, newData)
                }
                .addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun joinWorkout(id: String) {
        val userId = user?.uid ?: error("UserId is null")
        workoutsRef
                .document(id)
                .update("participants.$userId", RequestStatus.PENDING)
                .addOnSuccessListener {
                    val data = previousStateData().copy(placesAvailable = previousStateData().placesAvailable.dec())
                    stateLiveData.value = WorkoutDetailsState.WorkoutLoadedAsWaitingForApproval(data)
                }.addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun unjoinWorkout(id: String, onSuccess: () -> Unit) {
        workoutsRef
                .document(id)
                .update("participants.${user?.uid}", FieldValue.delete())
                .addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun deleteWorkout(id: String, onSuccess: () -> Unit) {
        workoutsRef
                .document(id)
                .delete()
                .addOnSuccessListener {
                    onSuccess()
                }.addOnFailureListener {
                    Timber.e(it)
                }
    }

    fun goToRequests() {
        stateLiveData.value = WorkoutDetailsState.RequestsNavigation(previousStateData())
    }

    fun contactButtonClicked() {
        stateLiveData.value = WorkoutDetailsState.ConversationNavigation(previousStateData())
    }

    private fun reduceWorkoutLoaded(workout: Seance, data: WorkoutDetailsData): WorkoutDetailsState {
        return when {
            user?.uid == workout.idAuteur -> WorkoutDetailsState.WorkoutLoadedAsAuthor(data)
            workout.participants[user?.uid] == RequestStatus.GRANTED -> WorkoutDetailsState.WorkoutLoadedAsJoined(data)
            workout.participants[user?.uid] == RequestStatus.PENDING -> WorkoutDetailsState.WorkoutLoadedAsWaitingForApproval(data)
            else -> WorkoutDetailsState.WorkoutLoaded(data)
        }
    }

    private fun previousStateData() = stateLiveData.value?.data ?: WorkoutDetailsData()

}