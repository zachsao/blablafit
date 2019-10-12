package com.example.fsudouest.blablafit.features.nearby.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.BR
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.MostRecentItemBinding
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.features.workoutDetails.DetailsSeanceActivity
import com.example.fsudouest.blablafit.model.Seance
import java.text.SimpleDateFormat

class NearByAdapter(val context: Context, val mData: List<LatestWorkoutViewItem>, val clickListener: ClickListener) : RecyclerView.Adapter<NearByAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: MostRecentItemBinding = DataBindingUtil.inflate(inflater, R.layout.most_recent_item, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount() = mData.size

    inner class WorkoutViewHolder(val binding: MostRecentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(workoutViewItem: LatestWorkoutViewItem) {
            binding.workout = workoutViewItem
            binding.executePendingBindings()
            Glide.with(context)
                    .load(workoutViewItem.authorPhotoUrl)
                    .fallback(R.drawable.userphoto)
                    .into(binding.authorPhoto)

            itemView.setOnClickListener {
                clickListener.navigateToDetails(workoutViewItem.id)
            }
        }
    }

    interface ClickListener {
        fun navigateToDetails(seanceId: String)
    }
}