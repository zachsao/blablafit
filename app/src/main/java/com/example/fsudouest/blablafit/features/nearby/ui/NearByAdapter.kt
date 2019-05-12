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
import com.example.fsudouest.blablafit.databinding.SeanceItem2Binding
import com.example.fsudouest.blablafit.features.workoutDetails.DetailsSeanceActivity
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class NearByAdapter(val context: Context, val mData: ArrayList<Seance?>) : RecyclerView.Adapter<NearByAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: SeanceItem2Binding = DataBindingUtil.inflate(inflater, R.layout.seance_item2, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(mData[position]!!)
    }

    override fun getItemCount() = mData.size

    inner class WorkoutViewHolder(val binding: SeanceItem2Binding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(seance: Seance) {
            val hourFormat = SimpleDateFormat("HH:mm")

            binding.setVariable(BR.seance, seance)
            binding.executePendingBindings()

            Glide.with(context).load(R.drawable.weights).into(binding.itemImage)

            val auteurRef = FirebaseFirestore.getInstance().collection("workouts")
                    .document(seance.id).collection("users").document("auteur")
            auteurRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result!!.toObject<User>(User::class.java)
                    if (user?.photoUrl != null)
                        Glide.with(context).load(user.photoUrl).placeholder(R.drawable.userphoto).into(binding.authorProfilePicture)
                }
            }
            binding.tvHeure.text = hourFormat.format(seance.date)

            binding.parentLayout.setOnClickListener {
                val intent = Intent(context, DetailsSeanceActivity::class.java)
                intent.putExtra("seance", seance)
                context.startActivity(intent)
            }
        }

    }
}