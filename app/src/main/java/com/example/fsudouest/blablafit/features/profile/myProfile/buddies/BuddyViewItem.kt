package com.example.fsudouest.blablafit.features.profile.myProfile.buddies

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ItemWorkoutBuddyBinding
import com.xwray.groupie.databinding.BindableItem

data class BuddyViewItem(
        val id: String,
        val name: String,
        val photoUrl: String
) : BindableItem<ItemWorkoutBuddyBinding>() {
    override fun getLayout(): Int = R.layout.item_workout_buddy

    override fun bind(viewBinding: ItemWorkoutBuddyBinding, position: Int) {
        viewBinding.buddy = this
    }
}