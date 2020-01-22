package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import android.view.View
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_workout_request.*
import kotlinx.android.synthetic.main.item_workout_request.view.*

sealed class RequestsState {
    abstract val data: RequestsData
    data class Idle(override val data: RequestsData) : RequestsState()
    data class Loading(override val data: RequestsData) : RequestsState()
    data class ItemsUpdated(override val data: RequestsData) : RequestsState()
    data class ItemsEmpty(override val data: RequestsData) : RequestsState()
    data class RequestStatusUpdating(override val data: RequestsData): RequestsState()
    data class RequestStatusUpdated(override val data: RequestsData): RequestsState()
}

data class RequestsData(
        val requests: List<RequestViewItem> = emptyList()
)

data class RequestViewItem(
        val uid: String,
        val pictureUrl: String,
        val userName: String,
        val fitnessLevel: String,
        val onAcceptRequest: (position: Int) -> Unit,
        val onDeclineRequest: (position: Int) -> Unit,
        val isStatusUpdating: Boolean = false,
        val isGranted: Boolean = false
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.userName.text = userName
        viewHolder.itemView.fitnessLevel.text = fitnessLevel
        Glide.with(viewHolder.itemView.context).load(pictureUrl).placeholder(R.drawable.userphoto).into(viewHolder.itemView.profilePicture)

        viewHolder.accept.apply {
            setOnClickListener { onAcceptRequest(position) }
            if (isGranted) {
                isEnabled = false
                text = "Accepted"
            }
        }
        viewHolder.decline.apply {
            setOnClickListener { onDeclineRequest(position) }
            if (isGranted) {
                visibility = View.GONE
            }
        }

        viewHolder.RequestProgressBar.visibility = if (isStatusUpdating) View.VISIBLE else View.GONE
    }

    override fun getLayout() = R.layout.item_workout_request
}