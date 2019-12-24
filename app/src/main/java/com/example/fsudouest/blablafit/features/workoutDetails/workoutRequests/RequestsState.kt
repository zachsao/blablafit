package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_workout_request.view.*

sealed class RequestsState {
    abstract val data: RequestsData
    data class Idle(override val data: RequestsData) : RequestsState()
    data class Loading(override val data: RequestsData) : RequestsState()
    data class ItemsUpdated(override val data: RequestsData) : RequestsState()
    data class ItemsEmpty(override val data: RequestsData) : RequestsState()

}

data class RequestsData(
        val requests: List<RequestViewItem> = emptyList()
)

data class RequestViewItem(
        val pictureUrl: String,
        val userName: String,
        val fitnessLevel: String
) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.userName.text = userName
        viewHolder.itemView.fitnessLevel.text = fitnessLevel
        Glide.with(viewHolder.itemView.context).load(pictureUrl).placeholder(R.drawable.userphoto).into(viewHolder.itemView.profilePicture)
    }

    override fun getLayout() = R.layout.item_workout_request
}