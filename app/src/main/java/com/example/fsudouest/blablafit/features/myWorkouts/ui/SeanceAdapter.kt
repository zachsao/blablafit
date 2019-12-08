package com.example.fsudouest.blablafit.features.myWorkouts.ui

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.features.nearby.ui.NearByAdapter
import java.text.SimpleDateFormat
import java.util.ArrayList


class SeanceAdapter(private var mData: ArrayList<Seance?>, val clickListener: NearByAdapter.ClickListener) : RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeanceViewHolder {
        context = parent.context
        val binding = SeanceItem2Binding.inflate(LayoutInflater.from(context), parent, false)
        return SeanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeanceViewHolder, position: Int) {
        mData[position]?.let {
            holder.bind(it)
        }
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    fun removeAt(position: Int) {
        mData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Seance, position: Int) {
        mData.add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }

    inner class SeanceViewHolder(var binding: SeanceItem2Binding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(seance: Seance) {
            val hourFormat = SimpleDateFormat("HH:mm")
            binding.seance = seance
            binding.executePendingBindings()
            binding.tvHeure.text = hourFormat.format(seance.date)
            binding.parentLayout.setOnClickListener {
                clickListener.navigateToDetails(seance.id)
            }
        }
    }
}
