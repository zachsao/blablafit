package com.example.fsudouest.blablafit.features.profile.myProfile.personalnfo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fsudouest.blablafit.databinding.FragmentPersonalInfoBinding
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInfoFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)

        viewModel.stateLiveData().observe(viewLifecycleOwner, {
            binding.user = it.data.currentUser
        })
        viewModel.getUser()

        return binding.root
    }


}
