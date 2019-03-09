package com.example.fsudouest.blablafit.Ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentAddDateDurationBinding


class AddDateDurationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDateDurationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_date_duration, container, false)
        return binding.root
    }


}
