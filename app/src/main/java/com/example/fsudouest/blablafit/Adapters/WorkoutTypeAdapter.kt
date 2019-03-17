package com.example.fsudouest.blablafit.Adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.WorkoutType
import com.example.fsudouest.blablafit.databinding.SeanceTypeItemBinding

class WorkoutTypeAdapter(private var mContext: Context, private var mData: ArrayList<WorkoutType>): RecyclerView.Adapter<WorkoutTypeAdapter.CardViewHolder>() {

    lateinit var context: Context

    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }



    inner class CardViewHolder(var binding: SeanceTypeItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int){
            binding.workoutTypeTv.text = mData[position].title
            Glide.with(context).load(mData[position].iconId).into(binding.workoutIcon)

            if(tracker!!.isSelected(position.toLong())) {
                binding.cardItem.background = ContextCompat.getDrawable(context,R.drawable.card_selected)
            } else {
                // Reset color to white if not selected
                binding.cardItem.background = ContextCompat.getDrawable(context,R.drawable.card_unselected)
            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object: ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getSelectionKey(): Long? = itemId
                    override fun getPosition(): Int = adapterPosition
                }
    }


}