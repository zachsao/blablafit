package com.example.fsudouest.blablafit.features.workoutDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests.RequestsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details_seance.*
import org.jetbrains.anko.backgroundColor

@AndroidEntryPoint
class DetailsSeanceFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsSeanceFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_details_seance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData().observe(viewLifecycleOwner, { state -> render(state) })
        viewModel.getWorkoutDetails(args.id)


        contactButton.setOnClickListener {
            viewModel.contactButtonClicked()
        }
        openMapsButton.setOnClickListener {
            openMaps()
        }
        requestsButton.setOnClickListener { viewModel.goToRequests() }
    }

    private fun render(state: WorkoutDetailsState) {
        when (state) {
            is WorkoutDetailsState.Idle -> TODO()
            is WorkoutDetailsState.JoinWorkoutSuccess -> TODO()
            is WorkoutDetailsState.UnJoinWorkoutSuccess -> TODO()
            is WorkoutDetailsState.DeleteWorkoutSuccess -> TODO()
            is WorkoutDetailsState.WorkoutLoadedAsAuthor -> {
                showWorkout(state.data)
                requestsButton.isVisible = true
                participate_button.apply {
                    text = getString(R.string.delete_workout)
                    backgroundColor = ContextCompat.getColor(requireContext(), R.color.dark_red)
                    setOnClickListener {
                        viewModel.deleteWorkout(args.id) { requireActivity().finish() }
                    }
                }
            }
            is WorkoutDetailsState.WorkoutLoadedAsJoined -> {
                showWorkout(state.data)
                contactButton.visibility = View.VISIBLE
                participate_button.text = getString(R.string.unjoin_workout)
                participate_button.backgroundColor = ContextCompat.getColor(requireContext(), R.color.dark_red)
                participate_button.setOnClickListener {
                    viewModel.unjoinWorkout(args.id) { requireActivity().finish() }
                }
            }
            is WorkoutDetailsState.WorkoutLoadedAsWaitingForApproval -> {
                showWorkout(state.data)
                participate_button.apply {
                    text = getString(R.string.request_sent)
                    isEnabled = false
                }
            }
            is WorkoutDetailsState.WorkoutLoaded -> {
                showWorkout(state.data)
                participate_button.setOnClickListener { viewModel.joinWorkout(args.id) }
            }
            is WorkoutDetailsState.RequestsNavigation -> {
                val intent = Intent(requireContext(), RequestsActivity::class.java).apply {
                    putExtra("participants", state.data.participants.toString())
                    putExtra("workoutId", args.id)
                }
                startActivity(intent)
            }
            is WorkoutDetailsState.ConversationNavigation -> {
                val intent = Intent(requireContext(), ConversationActivity::class.java).apply {
                    putExtra("contactName", state.data.authorName)
                    putExtra("userId", state.data.authorId)
                }
                startActivity(intent)
            }
        }
    }

    private fun openMaps() {
        val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=${workout_lieu_textview.text}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        }
    }

    private fun showWorkout(data: WorkoutDetailsData) {
        Glide.with(this)
                .load(R.drawable.weights)
                .into(imageView)

        workout_title_textview.text = data.title
        workout_author_textView.text = getString(R.string.created_by, data.authorName)

        Glide.with(this)
                .load(data.authorPictureUrl)
                .placeholder(R.drawable.userphoto)
                .into(circleImageView)

        workout_date_textview.text = data.date
        workout_hour_textview.text = data.time
        workout_lieu_textview.text = data.location
        workout_duration_textview.text = data.duration
        workout_description_textview.text = if (data.description.isBlank()) getString(R.string.empty_description) else data.description
        workout_participants_textview.text = resources.getQuantityString(R.plurals.numberOfPlacesAvailable, data.placesAvailable, data.placesAvailable)
    }
}
