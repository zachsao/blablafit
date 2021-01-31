package com.example.fsudouest.blablafit.features.accountSetup.genderSelection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gender_selection.*
import org.jetbrains.anko.imageResource

@AndroidEntryPoint
class GenderSelectionFragment : Fragment() {

    private val viewModel: AccountSetupViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gender_selection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.stateLiveData().observe(viewLifecycleOwner, { state ->
            selectMale(state.data.gender)
            selectFemale(state.data.gender)
        })
        male.setOnClickListener { viewModel.updateGender(true) }
        female.setOnClickListener { viewModel.updateGender(false) }
        next.setOnClickListener { findNavController().navigate(R.id.action_genderSelectionFragment_to_levelFragment) }
        back.setOnClickListener { findNavController().navigate(R.id.action_genderSelectionFragment_to_basicInformationFragment) }
    }

    private fun selectFemale(isMale: Boolean) {
        female.imageResource = if (isMale) R.drawable.ic_female_unselected else R.drawable.ic_female_selected
    }

    private fun selectMale(isMale: Boolean) {
        male.imageResource = if (isMale) R.drawable.ic_male_selected else R.drawable.ic_male_unselected
    }

}
