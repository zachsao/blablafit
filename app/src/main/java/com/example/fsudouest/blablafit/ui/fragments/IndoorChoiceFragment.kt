package com.example.fsudouest.blablafit.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.bumptech.glide.Glide

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentIndoorChoiceBinding
import com.example.fsudouest.blablafit.di.Injectable



class IndoorChoiceFragment : Fragment(), Injectable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentIndoorChoiceBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_indoor_choice, container, false)

        Glide.with(this).load(R.drawable.gym).into(binding.gymImage)
        Glide.with(this).load(R.drawable.outdoors).into(binding.outdoorImage)

        binding.cardGym.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_indoorChoiceFragment_to_typeSeanceFragment)
        }
        return binding.root
    }


}
