package com.example.fsudouest.blablafit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceTypeItemBinding
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem

class WorkoutTypeAdapter(private var mContext: Context, private var mData: List<CategoryViewItem>): RecyclerView.Adapter<WorkoutTypeAdapter.CardViewHolder>() {

    lateinit var context: Context

    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        context = parent.context

        val inflater = LayoutInflater.from(context)

        val binding = DataBindingUtil.inflate<SeanceTypeItemBinding>(inflater,R.layout.seance_type_item, parent, false)

        return CardViewHolder(binding)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }



    inner class CardViewHolder(var binding: SeanceTypeItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: CategoryViewItem){
            binding.category = category

            if(tracker!!.isSelected(adapterPosition.toLong())) {
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