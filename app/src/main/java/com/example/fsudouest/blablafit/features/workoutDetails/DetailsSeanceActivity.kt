package com.example.fsudouest.blablafit.features.workoutDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityDetailsSeanceBinding
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests.RequestsActivity
import com.example.fsudouest.blablafit.model.RequestStatus
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.android.AndroidInjection
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class DetailsSeanceActivity : AppCompatActivity() {

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

        supportActionBar?.title = getString(R.string.workout_details)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Glide.with(this).load(R.drawable.weights).into(binding.imageView)

        val args : DetailsSeanceActivityArgs by navArgs()
        val workoutId = args.id

        viewModel = ViewModelProvider(this, factory).get(DetailsViewModel::class.java).apply {
            getWorkoutDetails(workoutId)
        }

        viewModel.detailsLiveData().observe(this, Observer {workout ->
            renderWorkout(workout)
            binding.contactButton.setOnClickListener {
                startActivity<ConversationActivity>("contactName" to workout.nomAuteur, "userId" to workout.idAuteur)
            }
            binding.openMapsButton.setOnClickListener {
                openMaps()
            }
        })
    }

    private fun openMaps() {
        val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=${binding.workoutLieuTextview.text}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun renderWorkout(seance: Seance){
        binding.seance = seance

        val authorProfilePicture = seance.photoAuteur
        Glide.with(this).load(authorProfilePicture).placeholder(R.drawable.userphoto).into(binding.circleImageView)

        val dateFormat = SimpleDateFormat("dd/MM/yy", Locale("fr", "FR"))
        val dateChaine = dateFormat.format(seance.date)

        val hourFormat = SimpleDateFormat("HH:mm", Locale("fr", "FR"))
        val heureChaine = hourFormat.format(seance.date)

        binding.workoutDateTextview.text = dateChaine
        binding.workoutHourTextview.text = heureChaine

        val isMaxedOut = seance.maxParticipants - seance.participants.size == 0

        binding.buttonsLayout.visibility = View.GONE
        when {
            hasAlreadyJoined(seance) -> {
                binding.buttonsLayout.visibility = View.VISIBLE
                binding.participateButton.text = "Unjoin workout"
                binding.participateButton.backgroundColor = ContextCompat.getColor(this, R.color.dark_red)
                binding.participateButton.setOnClickListener {
                    viewModel.unjoinWorkout(seance) { finish() }
                }
            }
            currentUserIsWorkoutAuthor(seance) -> {
                binding.requestsButton.apply {
                    visibility = View.VISIBLE
                    setOnClickListener { goToRequests(seance) }
                }
                binding.participateButton.apply {
                    text = "Delete workout"
                    backgroundColor = ContextCompat.getColor(this@DetailsSeanceActivity, R.color.dark_red)
                    setOnClickListener {
                        viewModel.deleteWorkout(seance, this@DetailsSeanceActivity)
                    }
                }
            }
            isMaxedOut -> {
                binding.participateButton.apply {
                    text = "Workout is full"
                    isEnabled = false
                }
            }
            requestSent(seance) -> {
                binding.participateButton.apply {
                    text = "Request sent"
                    isEnabled = false
                }
            }
            else -> binding.participateButton.setOnClickListener { viewModel.joinWorkout(seance) }
        }
    }

    private fun goToRequests(workout: Seance) {
        val intent = Intent(this, RequestsActivity::class.java).apply {
            putExtra("participants", workout.participants.toString())
            putExtra("workoutId", workout.id)
        }
        startActivity(intent)
    }

    private fun currentUserIsWorkoutAuthor(seance: Seance): Boolean {
        return user?.uid == seance.idAuteur
    }

    private fun hasAlreadyJoined(seance: Seance): Boolean{
        return seance.participants.containsKey(user?.uid) && seance.participants[user?.uid] == RequestStatus.GRANTED
    }

    private fun requestSent(seance: Seance): Boolean {
        return seance.participants.containsKey(user?.uid) && seance.participants[user?.uid] == RequestStatus.PENDING
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
