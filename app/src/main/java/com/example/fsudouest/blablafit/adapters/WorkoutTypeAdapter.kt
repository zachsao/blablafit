package com.example.fsudouest.blablafit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceTypeItemBinding
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem

class WorkoutTypeAdapter(val mData: List<CategoryViewItem>) : RecyclerView.Adapter<WorkoutTypeAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = SeanceTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(mData[position])
    }



    inner class CardViewHolder(var binding: SeanceTypeItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryViewItem){
            binding.category = category

            binding.cardItem.setOnClickListener {
                category.isSelected = !category.isSelected
                if (category.isSelected) {
                    binding.cardItem.background = ContextCompat.getDrawable(binding.root.context, R.drawable.card_selected)
                } else {
                    binding.cardItem.background = ContextCompat.getDrawable(binding.root.context, R.drawable.card_unselected)
                }
            }

            if (category.isSelected) {
                binding.cardItem.background = ContextCompat.getDrawable(binding.root.context, R.drawable.card_selected)
            } else {
                binding.cardItem.background = ContextCompat.getDrawable(binding.root.context, R.drawable.card_unselected)
            }
        }
    }


}