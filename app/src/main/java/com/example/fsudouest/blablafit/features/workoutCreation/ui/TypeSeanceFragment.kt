package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentTypeSeanceBinding
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import com.example.fsudouest.blablafit.model.Seance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TypeSeanceFragment : Fragment() {

    private lateinit var mAdapter: WorkoutTypeAdapter

    private val viewModel: WorkoutCreationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentTypeSeanceBinding.inflate(inflater, container, false)

        mAdapter = WorkoutTypeAdapter(CategoryViewItems.getCategoryViewItems())

        binding.gridRecyclerView.adapter = mAdapter

        binding.nextButton.setOnClickListener { view ->
            val selection = mAdapter.mData.filter { it.isSelected }.map { category -> getString(category.name) }
            if (selection.isEmpty()) {
                Toast.makeText(activity, getString(R.string.empty_selection_warning), LENGTH_SHORT).show()
            } else {
                val workout = Seance(selection)
                viewModel.addWorkout(workout)
                Navigation.findNavController(view)
                        .navigate(TypeSeanceFragmentDirections.actionTypeSeanceFragmentToAddDateDurationFragment())
            }
        }
        return binding.root
    }


}
