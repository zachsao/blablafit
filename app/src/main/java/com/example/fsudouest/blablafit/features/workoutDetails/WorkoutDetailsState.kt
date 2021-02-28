package com.example.fsudouest.blablafit.features.workoutDetails

import com.example.fsudouest.blablafit.model.RequestStatus

sealed class WorkoutDetailsState {
    abstract val data: WorkoutDetailsData
    data class Idle(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class Loading(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class WorkoutLoadedAsAuthor(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class WorkoutLoadedAsJoined(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class WorkoutLoadedAsWaitingForApproval(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class WorkoutLoaded(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class JoinWorkoutSuccess(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class UnJoinWorkoutSuccess(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class DeleteWorkoutSuccess(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class RequestsNavigation(override val data: WorkoutDetailsData) : WorkoutDetailsState()
    data class ConversationNavigation(override val data: WorkoutDetailsData) : WorkoutDetailsState()
}

data class WorkoutDetailsData(
        val title: String = "",
        val authorId: String = "",
        val authorName: String = "",
        val authorPictureUrl: String? = null,
        val time: String = "",
        val date: String = "",
        val duration: String = "",
        val location: String = "",
        val placesAvailable: Int = 0,
        val description: String = "",
        val participants: Map<String, RequestStatus> = emptyMap()
)