package com.example.fsudouest.blablafit.Ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.fsudouest.blablafit.databinding.FragmentAddDescriptionBinding
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Seance


class AddDescriptionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDescriptionBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_description, container, false)


        val workout = arguments?.getSerializable("workout") as Seance

        val description = binding.descriptionEdittext

        binding.nextStepButton.setOnClickListener {
            workout.description = description.text.toString()
            Toast.makeText(activity,"Séance : ${workout.titre}, ${workout.description}", Toast.LENGTH_SHORT).show()
            val bundle = bundleOf("workout" to workout)
            Navigation.findNavController(it).navigate(R.id.action_addDescriptionFragment_to_addDateDurationFragment,bundle)
        }
        return binding.root
    }


}
