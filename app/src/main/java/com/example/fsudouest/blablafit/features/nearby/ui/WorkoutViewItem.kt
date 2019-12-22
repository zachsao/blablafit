package com.example.fsudouest.blablafit.features.nearby.ui

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.model.Seance
import com.xwray.groupie.databinding.BindableItem

class WorkoutViewItem(val seance: Seance) : BindableItem<SeanceItem2Binding>() {
    override fun bind(viewBinding: SeanceItem2Binding, position: Int) {
        viewBinding.seance = seance
    }

    override fun getLayout() = R.layout.seance_item2

}