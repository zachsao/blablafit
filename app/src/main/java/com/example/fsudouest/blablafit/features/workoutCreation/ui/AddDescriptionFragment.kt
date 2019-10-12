package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.fsudouest.blablafit.databinding.FragmentAddDescriptionBinding
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import javax.inject.Inject


class AddDescriptionFragment : Fragment(), Injectable {

    private lateinit var viewModel: WorkoutCreationViewModel
    @Inject
    lateinit var factory: ViewModelFactory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDescriptionBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_add_description, container, false)

        val description = binding.descriptionEdittext
        val args: AddDescriptionFragmentArgs by navArgs()

        binding.nextStepButton.setOnClickListener {
            viewModel.workoutLiveData.value?.description = description.text.toString()
            Navigation.findNavController(it)
                    .navigate(AddDescriptionFragmentDirections
                            .actionAddDescriptionFragmentToAddDateDurationFragment(args.choice))
        }
        return binding.root
    }


}
