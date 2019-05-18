package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.fsudouest.blablafit.BuildConfig

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentSearchLocationBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_search_location.*
import java.util.*
import javax.inject.Inject


class SearchLocationFragment : Fragment(), Injectable {

    private val apiKey = BuildConfig.GOOGLE_PLACES_KEY
    private lateinit var lieu: String
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    @Inject
    lateinit var mDatabase: FirebaseFirestore

    private lateinit var viewModel: WorkoutCreationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSearchLocationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_location, container, false)


        val args: AddDateDurationFragmentArgs by navArgs()
        // Initialize Places.
        Places.initialize(activity!!.applicationContext, apiKey)

        autocompleteFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS))
        autocompleteFragment.setHint(getString(R.string.searchViewHint))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lieu = "${place.name}, ${place.address}"
                location_textview.text = lieu
                viewModel.workoutLiveData.value?.lieu = lieu
            }

            override fun onError(status: Status) {
                Log.e("SearchLocationFragment", "An error occurred: $status")
            }
        })
        when(args.choice){
            0 -> autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
            1 -> {
                autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
                binding.findGymTextView.text = getString(R.string.find_your_workout_location)
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        viewModel.workoutLiveData.value?.participants = listOf(user?.email ?: "")
        viewModel.workoutLiveData.value?.auteur = user?.email ?: ""

        binding.validateButton.setOnClickListener { view ->
            if(viewModel.workoutLiveData.value?.lieu == ""){
                Toast.makeText(activity, "Veuillez entrer une adresse valide", Toast.LENGTH_SHORT).show()
            }else{
                val ref = mDatabase.collection("workouts").document()
                viewModel.workoutLiveData.value?.id = ref.id

                ref.set(viewModel.workoutLiveData.value!!).addOnSuccessListener {
                    //add a subcollection of users
                    Navigation.findNavController(view).navigate(R.id.action_searchLocationFragment_to_seancesFragment)
                }.addOnFailureListener { Toast.makeText(activity, "Une erreur s'est produite", Toast.LENGTH_SHORT).show() }
            }

        }
        return binding.root
    }


}
