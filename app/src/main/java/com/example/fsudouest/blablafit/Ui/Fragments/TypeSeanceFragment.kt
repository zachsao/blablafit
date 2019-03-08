package com.example.fsudouest.blablafit.Ui.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.Adapters.WorkoutTypeAdapter

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentTypeSeanceBinding
import com.example.fsudouest.blablafit.model.WorkoutType


class TypeSeanceFragment : Fragment() {

    private lateinit var mAdapter: WorkoutTypeAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var workouts: ArrayList<WorkoutType>
    private lateinit var list: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentTypeSeanceBinding>(inflater,R.layout.fragment_type_seance, container, false)

        list = binding.gridRecyclerView

        workouts = ArrayList()
        workouts.add(WorkoutType("Full body",R.drawable.icons8_weightlifting_50))
        workouts.add(WorkoutType("Yoga",R.drawable.icons8_yoga_50))
        workouts.add(WorkoutType("Running",R.drawable.icons8_running_50))
        workouts.add(WorkoutType("Upper body",R.drawable.icons8_torso_50))
        workouts.add(WorkoutType("Jambes",R.drawable.icons8_leg_50))
        workouts.add(WorkoutType("Bras",R.drawable.icons8_muscle_50))
        workouts.add(WorkoutType("Pecs",R.drawable.icons8_chest_50))
        workouts.add(WorkoutType("Epaules",R.drawable.icons8_shoulders_50))
        workouts.add(WorkoutType("Triceps",R.drawable.icons8_triceps_50))
        workouts.add(WorkoutType("Dos",R.drawable.icons8_bodybuilder_50))
        workouts.add(WorkoutType("Biceps",R.drawable.icons8_biceps_50))
        workouts.add(WorkoutType("Abdos",R.drawable.icons8_prelum_50))


        mAdapter = WorkoutTypeAdapter(activity!!,workouts)

        list.apply {
            layoutManager = GridLayoutManager(activity!!,3)
            adapter = mAdapter
        }

        binding.nextStepButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_typeSeanceFragment_to_addDescriptionFragment)
        }

        return binding.root
    }


}
