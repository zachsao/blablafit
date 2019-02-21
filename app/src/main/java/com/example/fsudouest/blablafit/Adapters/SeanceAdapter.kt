package com.example.fsudouest.blablafit.Adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.BR
import com.example.fsudouest.blablafit.Ui.Activities.DetailsSeanceActivity
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class SeanceAdapter(private var mContext: Context, private var mData: ArrayList<Seance>) : RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeanceViewHolder {

        val context = parent.context

        val layoutIdForListItem = R.layout.seance_item2
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val binding = DataBindingUtil.inflate<SeanceItem2Binding>(inflater,layoutIdForListItem, parent, shouldAttachToParentImmediately)
        val viewHolder = SeanceViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: SeanceViewHolder, position: Int) {
        holder.bind(position)
    }


    override fun getItemCount(): Int {
        return mData.size
    }

    inner class SeanceViewHolder(var binding: SeanceItem2Binding) : RecyclerView.ViewHolder(binding.root) {


        private var date: TextView
        private var heure: TextView
        var parent: LinearLayout

        init {

            parent = binding.parentLayout
            date = binding.tvDate
            heure = binding.tvHeure

        }


        fun bind(position: Int) {
            val dateFormat = SimpleDateFormat("dd/MM/yy")
            val hourFormat = SimpleDateFormat("HH:mm")

            val seance = mData[position]
            binding.setVariable(BR.seance,seance)
            binding.executePendingBindings()

            date.text = dateFormat.format(mData[position].date)
            heure.text = hourFormat.format(mData[position].date)

            parent.setOnClickListener {
                val intent = Intent(mContext, DetailsSeanceActivity::class.java)
                intent.putExtra("seance", mData[position])
                mContext.startActivity(intent)
            }
        }
    }
}
