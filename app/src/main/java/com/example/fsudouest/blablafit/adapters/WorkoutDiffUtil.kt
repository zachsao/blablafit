package com.example.fsudouest.blablafit.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.fsudouest.blablafit.model.Seance

class WorkoutDiffUtil(private val oldList: ArrayList<Seance?>, private val newList: ArrayList<Seance?>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition]?.id == newList[newItemPosition]?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]!!
        val newItem = newList[newItemPosition]!!
        return oldItem.participants == newItem.participants &&
                oldItem.date == newItem.date &&
                oldItem.description == newItem.description &&
                oldItem.duree == newItem.duree &&
                oldItem.titre == newItem.titre &&
                oldItem.lieu == newItem.lieu &&
                oldItem.nb_participants == newItem.nb_participants
    }

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

}