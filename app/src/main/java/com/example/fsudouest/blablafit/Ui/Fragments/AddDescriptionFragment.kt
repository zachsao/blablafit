package com.example.fsudouest.blablafit.Ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.fsudouest.blablafit.databinding.FragmentAddDescriptionBinding
import com.example.fsudouest.blablafit.R


class AddDescriptionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDescriptionBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_description, container, false)
        binding.nextStepButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_addDescriptionFragment_to_addDateDurationFragment)
        }
        return binding.root
    }


}