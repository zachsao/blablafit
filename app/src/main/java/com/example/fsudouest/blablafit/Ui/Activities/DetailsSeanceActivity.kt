package com.example.fsudouest.blablafit.Ui.Activities

import android.os.Bundle
import android.util.Log
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityDetailsSeanceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.AndroidInjection
import dagger.android.support.HasSupportFragmentInjector

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
    private var author: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        val binding: ActivityDetailsSeanceBinding = DataBindingUtil.setContentView(this,R.layout.activity_details_seance)

        date = binding.workoutDateTextview
        heure = binding.workoutHourTextview
        photo = binding.circleImageView

        Glide.with(this).load(R.drawable.weights).into(binding.imageView)

        val intent = intent
        val seance = intent.getSerializableExtra("seance") as Seance

        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale("fr", "FR"))
        val dateChaine = dateFormat.format(seance.date)

        val hourFormat = SimpleDateFormat("HH:mm", Locale("fr", "FR"))
        val heureChaine = hourFormat.format(seance.date)

        date.text = dateChaine
        heure.text = heureChaine

        binding.seance = seance

        user = firebaseAuth.currentUser

        val workoutParticipantsRef = mDatabase.collection("workouts")
                .document(seance.id).collection("users")
        val auteurRef = workoutParticipantsRef.document("auteur")
        auteurRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                author = task.result!!.toObject<User>(User::class.java)
                if (author?.photoUrl!=null)
                    Glide.with(this).load(author?.photoUrl).into(photo)
            }
        }

        if(!user?.displayName.equals(author?.nomComplet)){
            binding.participateButton.isEnabled = false
            binding.participateButton.isClickable = false
        }else {
            binding.participateButton.setOnClickListener {
                workoutParticipantsRef.document()
                        .set(User(user?.displayName!!,user?.email!!,user?.photoUrl.toString()))
                        .addOnSuccessListener {
                            Log.i("Participer", "Nouveau participant : ${user?.displayName}")
                            finish()
                        }.addOnFailureListener {
                            Log.e("Participer", it.message)
                        }

            }
        }



        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }


}
