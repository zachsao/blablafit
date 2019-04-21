package com.example.fsudouest.blablafit.utils

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.adapters.WorkoutTypeAdapter

class MyLookup(private val rv: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(e.x, e.y)
        if(view != null) {
            return (rv.getChildViewHolder(view) as WorkoutTypeAdapter.CardViewHolder).getItemDetails()
        }
        return null
    }

}