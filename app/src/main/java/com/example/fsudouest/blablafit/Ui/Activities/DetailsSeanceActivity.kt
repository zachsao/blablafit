package com.example.fsudouest.blablafit.Ui.Activities

import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import androidx.databinding.DataBindingUtil

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityDetailsSeanceBinding
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.Locale

import de.hdodenhof.circleimageview.CircleImageView


class DetailsSeanceActivity : AppCompatActivity() {


    private lateinit var date: TextView
    private lateinit var photo: CircleImageView

    private lateinit var mDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailsSeanceBinding = DataBindingUtil.setContentView(this,R.layout.activity_details_seance)

        mDatabase = FirebaseFirestore.getInstance()


        date = binding.workoutDateTextview
        photo = binding.imageView3


        val intent = intent
        val seance = intent.getSerializableExtra("seance") as Seance

        val format = SimpleDateFormat("EEE d MMM yy à HH:mm", Locale("fr", "FR"))
        val dateChaine = format.format(seance.date)

        date.text = dateChaine

        binding.seance = seance


        val auteurRef = mDatabase.collection("workouts")
                .document(seance.id).collection("users").document("auteur")
        auteurRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result!!.toObject<User>(User::class.java)
                if (user?.photoUrl!=null)
                    Glide.with(this).load(user.photoUrl).into(photo)
            }
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


}
