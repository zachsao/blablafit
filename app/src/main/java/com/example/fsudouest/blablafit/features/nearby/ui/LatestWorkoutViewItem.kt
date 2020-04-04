package com.example.fsudouest.blablafit.features.nearby.ui

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.MostRecentItemBinding
import com.xwray.groupie.databinding.BindableItem

data class LatestWorkoutViewItem(
        val id: String = "",
        val title: String = "",
        val address: String = "",
        val placesAvailable: Int = 0,
        val authorName: String = "",
        val authorPhotoUrl: String? = null,
        val time: String = "",
        val date: String = ""
): BindableItem<MostRecentItemBinding>() {
    override fun bind(viewBinding: MostRecentItemBinding, position: Int) {
        viewBinding.workout = this
        viewBinding.executePendingBindings()
    }

    override fun getLayout(): Int = R.layout.most_recent_item
}