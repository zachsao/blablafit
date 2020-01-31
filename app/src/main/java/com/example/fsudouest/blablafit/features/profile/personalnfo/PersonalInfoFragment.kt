package com.example.fsudouest.blablafit.features.profile.personalnfo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentPersonalInfoBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import javax.inject.Inject

class PersonalInfoFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<ProfileViewModel>

    private val viewModel by lazy { ViewModelProviders.of(this, factory).get(ProfileViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)

        viewModel.user().observe(this, Observer {
            binding.user = it
        })

        return binding.root
    }


}
