package com.example.fsudouest.blablafit.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.WorkoutType
import com.example.fsudouest.blablafit.databinding.SeanceTypeItemBinding

class WorkoutTypeAdapter(private var mContext: Context, private var mData: ArrayList<WorkoutType>): RecyclerView.Adapter<WorkoutTypeAdapter.CardViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutTypeAdapter.CardViewHolder {
        context = parent.context

        val inflater = LayoutInflater.from(context)

        val binding = DataBindingUtil.inflate<SeanceTypeItemBinding>(inflater,R.layout.seance_type_item, parent, false)

        return CardViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: WorkoutTypeAdapter.CardViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class CardViewHolder(var binding: SeanceTypeItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.workoutTypeTv.text = mData[position].title
            Glide.with(context).load(mData[position].iconId).into(binding.workoutIcon)
        }
    }


}