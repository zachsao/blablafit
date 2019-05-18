package com.example.fsudouest.blablafit.features.workoutDetails

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityDetailsSeanceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.AndroidInjection

import java.text.SimpleDateFormat
import java.util.Locale

import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject


class DetailsSeanceActivity : AppCompatActivity() {


    private lateinit var date: TextView
    private lateinit var heure: TextView
    private lateinit var photo: CircleImageView

    @Inject
    lateinit var mDatabase: FirebaseFirestore

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var user: FirebaseUser? = null

    lateinit var binding: ActivityDetailsSeanceBinding

    lateinit var seance: Seance

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_seance)

        date = binding.workoutDateTextview
        heure = binding.workoutHourTextview
        photo = binding.circleImageView

        Glide.with(this).load(R.drawable.weights).into(binding.imageView)

        val intent = intent
        seance = intent.getSerializableExtra("seance") as Seance

        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale("fr", "FR"))
        val dateChaine = dateFormat.format(seance.date)

        val hourFormat = SimpleDateFormat("HH:mm", Locale("fr", "FR"))
        val heureChaine = hourFormat.format(seance.date)

        date.text = dateChaine
        heure.text = heureChaine

        binding.seance = seance

        user = firebaseAuth.currentUser

        val authorProfilePicture = seance.auteurPhotoUrl
        if (authorProfilePicture.isNotEmpty())
            Glide.with(this).load(authorProfilePicture).placeholder(R.drawable.userphoto).into(photo)

        if (currentUserIsWorkoutAuthor(seance)) {
            disableParticipateButton()
        } else {
            binding.participateButton.setOnClickListener { joinWorkout() }
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


    private fun joinWorkout() {
        mDatabase.collection("workouts")
                .document(seance.id)
                .update("participants", FieldValue.arrayUnion(user?.email))
                .addOnSuccessListener {
                    Log.i("Participer", "Nouveau participant : ${user?.displayName}")
                    finish()
                }.addOnFailureListener {
                    Log.e("Participer", it.message)
                }
    }

    private fun disableParticipateButton() {
        binding.participateButton.visibility = View.GONE
    }

    private fun currentUserIsWorkoutAuthor(seance: Seance): Boolean {
        return user?.email == seance.auteur
    }


}
