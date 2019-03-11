package com.example.fsudouest.blablafit.Ui.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.BuildConfig

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentSearchLocationBinding
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*


class SearchLocationFragment : Fragment() {

    private val apiKey = BuildConfig.GOOGLE_PLACES_KEY
    private lateinit var lieu: String
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentSearchLocationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_search_location, container, false)

        // Initialize Places.
        Places.initialize(activity!!.applicationContext, apiKey)

        autocompleteFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS))
        autocompleteFragment.setHint("Rechercher une salle de sport")
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lieu = "${place.name}, ${place.address}"
                autocompleteFragment.setText(lieu)
            }

            override fun onError(status: Status) {
                Log.e("SearchLocationFragment", "An error occurred: $status")
            }
        })

        return binding.root
    }


}
