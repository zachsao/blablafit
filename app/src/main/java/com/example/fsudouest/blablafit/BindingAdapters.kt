package com.example.fsudouest.blablafit

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.features.category.WorkoutViewItem
import com.xwray.groupie.GroupAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<WorkoutViewItem>) {
    if (data.isNotEmpty()) {
        recyclerView.visibility = View.VISIBLE
        val adapter = recyclerView.adapter as GroupAdapter
        adapter.clear()
        adapter.addAll(data)
    } else recyclerView.visibility = View.GONE
}

@BindingAdapter("status")
fun bindStatus(textView: TextView, text: String?){
    if (!text.isNullOrBlank()) {
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}