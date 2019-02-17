package com.example.fsudouest.blablafit

import android.content.Intent
import android.os.Bundle
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView

import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.Locale

import de.hdodenhof.circleimageview.CircleImageView


class DetailsSeanceActivity : AppCompatActivity() {

    private lateinit var title: TextView
    private lateinit var location: TextView
    private lateinit var duration: TextView
    private lateinit var description: TextView
    private lateinit var date: TextView
    private lateinit var places: TextView
    private lateinit var auteur: TextView
    private lateinit var photo: CircleImageView

    private lateinit var mDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_seance)

        mDatabase = FirebaseFirestore.getInstance()

        title = findViewById(R.id.workout_title_textview)
        location = findViewById(R.id.workout_location_textview)
        duration = findViewById(R.id.workout_length_textview)
        description = findViewById(R.id.workout_description_textview)
        date = findViewById(R.id.workout_date_textview)
        places = findViewById(R.id.workout_participants_textview)
        auteur = findViewById(R.id.workout_author_textview)
        photo = findViewById(R.id.imageView3)


        val intent = intent
        val b = intent.extras
        val seance = intent.getSerializableExtra("seance") as Seance

        val format = SimpleDateFormat("EEE d MMM yy à HH:mm", Locale("fr", "FR"))
        val dateChaine = format.format(seance.date)

        title.text = "Type de séance : " + seance.titre
        location.text = "Lieu : " + seance.lieu
        date.text = "Date : $dateChaine"
        duration.text = "Durée : " + seance.duree
        description.text = "Description : " + seance.description
        places.text = "Places restantes : " + seance.nb_participants
        auteur.text = seance.createur

        val auteurRef = mDatabase.collection("workouts")
                .document(seance.id).collection("users").document("auteur")
        auteurRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = task.result!!.toObject<User>(User::class.java)
                Glide.with(this).load(user!!.photoUrl).into(photo)
            }
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
