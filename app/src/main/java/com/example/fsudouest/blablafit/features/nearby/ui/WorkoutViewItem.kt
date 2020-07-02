package com.example.fsudouest.blablafit.features.nearby.ui

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.model.Seance
import com.xwray.groupie.databinding.BindableItem
import java.util.*

class WorkoutViewItem(val id: String, val title: String, val location: String, val date: Date, val duration: String, val placesAvailable: Int, val author: String, val photoUrl: String?) : BindableItem<SeanceItem2Binding>() {
    override fun bind(viewBinding: SeanceItem2Binding, position: Int) {
        viewBinding.seance = this
    }

    override fun getLayout() = R.layout.seance_item2
}