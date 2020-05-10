package com.example.fsudouest.blablafit.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.accountSetup.fitnessLevel.FitnessLevel
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.xwray.groupie.GroupAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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

@BindingAdapter("resIcon")
fun bindResIcon(imageView: ImageView, @DrawableRes icon: Int) {
    Glide.with(imageView.context).load(icon).into(imageView)
}

@BindingAdapter("genderIcon")
fun bindGenderResIcon(imageView: ImageView, gender: Boolean) {
    if (gender) imageView.setImageResource(R.drawable.ic_male_selected)
    else imageView.setImageResource(R.drawable.ic_female_selected)
}

@BindingAdapter("imgUrl")
fun bindImage(imageView: ImageView, imageUrl: String?){
    Glide.with(imageView.context)
            .load(imageUrl)
            .placeholder(R.drawable.userphoto)
            .into(imageView)
}

@BindingAdapter("placesAvailable")
fun bindText(textView: TextView, available: Int){
    textView.apply {
        text = resources.getQuantityString(R.plurals.numberOfPlacesAvailable, available, available)
    }
}

@BindingAdapter("gender")
fun bindGender(textView: TextView, gender: Boolean){
    textView.apply {
        text = if (gender) context.getString(R.string.male) else context.getString(R.string.female)
    }
}

@BindingAdapter("formattedDate")
fun bindWorkoutDate(textView: TextView, date: Date){
    val dateString = DateFormat.getDateInstance(DateFormat.SHORT).format(date)
    textView.text = dateString
}

@BindingAdapter("formattedTime")
fun bindWorkoutTime(textView: TextView, date: Date){
    val dateString = DateFormat.getTimeInstance(DateFormat.SHORT).format(date)
    textView.text = dateString
}
