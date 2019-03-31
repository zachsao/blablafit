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
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.BR
import com.example.fsudouest.blablafit.Ui.Activities.DetailsSeanceActivity
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class SeanceAdapter(private var mContext: Context, private var mData: ArrayList<Seance>) : RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeanceViewHolder {

        context = parent.context

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


        private lateinit var date: TextView
        private var heure: TextView
        var parent: LinearLayout

        init {

            parent = binding.parentLayout
            heure = binding.tvHeure

        }


        fun bind(position: Int) {
            val hourFormat = SimpleDateFormat("HH:mm")

            val seance = mData[position]
            binding.setVariable(BR.seance,seance)
            binding.executePendingBindings()

            Glide.with(context).load(R.drawable.weights).into(binding.itemImage)

            val auteurRef = FirebaseFirestore.getInstance().collection("workouts")
                    .document(seance.id).collection("users").document("auteur")
            auteurRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result!!.toObject<User>(User::class.java)
                    if (user?.photoUrl!=null)
                        Glide.with(context).load(user.photoUrl).into(binding.authorProfilePicture)
                }
            }

            //date.text = dateFormat.format(mData[position].date)
            heure.text = hourFormat.format(mData[position].date)

            parent.setOnClickListener {
                val intent = Intent(mContext, DetailsSeanceActivity::class.java)
                intent.putExtra("seance", mData[position])
                mContext.startActivity(intent)
            }
        }
    }
}
