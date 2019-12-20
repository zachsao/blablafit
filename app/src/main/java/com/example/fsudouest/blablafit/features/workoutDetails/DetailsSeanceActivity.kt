package com.example.fsudouest.blablafit.features.workoutDetails

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityDetailsSeanceBinding
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.android.AndroidInjection
import de.hdodenhof.circleimageview.CircleImageView
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DetailsSeanceActivity : AppCompatActivity() {


    private lateinit var date: TextView
    private lateinit var heure: TextView
    private lateinit var photo: CircleImageView

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var user: FirebaseUser? = null

    lateinit var binding: ActivityDetailsSeanceBinding

    private lateinit var viewModel: DetailsViewModel

    @Inject lateinit var factory: ViewModelFactory<DetailsViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details_seance)
        user = firebaseAuth.currentUser
        date = binding.workoutDateTextview
        heure = binding.workoutHourTextview
        photo = binding.circleImageView

        Glide.with(this).load(R.drawable.weights).into(binding.imageView)

        val args : DetailsSeanceActivityArgs by navArgs()
        val workoutId = args.id

        viewModel = ViewModelProviders.of(this, factory).get(DetailsViewModel::class.java).apply {
            getWorkoutDetails(workoutId)
        }

        viewModel.detailsLiveData().observe(this, Observer {workout ->
            renderWorkout(workout)
            binding.contactButton.setOnClickListener {
                startActivity<ConversationActivity>("contactName" to workout.nomAuteur, "userId" to workout.idAuteur)
            }
        })
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun renderWorkout(seance: Seance){
        binding.seance = seance

        val authorProfilePicture = seance.photoAuteur
        Glide.with(this).load(authorProfilePicture).placeholder(R.drawable.userphoto).into(photo)

        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale("fr", "FR"))
        val dateChaine = dateFormat.format(seance.date)

        val hourFormat = SimpleDateFormat("HH:mm", Locale("fr", "FR"))
        val heureChaine = hourFormat.format(seance.date)

        date.text = dateChaine
        heure.text = heureChaine

        when {
            hasAlreadyJoined(seance) -> {
                binding.participateButton.text = "Unjoin workout"
                binding.participateButton.backgroundColor = ContextCompat.getColor(this, R.color.dark_red)
                binding.participateButton.setOnClickListener {
                    viewModel.unjoinWorkout(seance,user, this)
                }
            }
            currentUserIsWorkoutAuthor(seance) -> {
                binding.buttonsLayout.visibility = View.GONE
                binding.participateButton.text = "Delete workout"
                binding.participateButton.backgroundColor = ContextCompat.getColor(this, R.color.dark_red)
                binding.participateButton.setOnClickListener {
                    viewModel.deleteWorkout(seance, this)
                }
            }
            else -> binding.participateButton.setOnClickListener { viewModel.joinWorkout(seance, user, this) }
        }
    }

    private fun currentUserIsWorkoutAuthor(seance: Seance): Boolean {
        return user?.uid == seance.idAuteur
    }

    private fun hasAlreadyJoined(seance: Seance): Boolean{
        return seance.participants.contains(user?.email)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
