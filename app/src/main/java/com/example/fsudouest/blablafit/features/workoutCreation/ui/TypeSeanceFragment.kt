package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentTypeSeanceBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import javax.inject.Inject


class TypeSeanceFragment : Fragment(), Injectable {

    private lateinit var mAdapter: WorkoutTypeAdapter
    private lateinit var categories: List<CategoryViewItem>

    private lateinit var viewModel: WorkoutCreationViewModel
    @Inject
    lateinit var factory: ViewModelFactory<WorkoutCreationViewModel>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this, factory).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentTypeSeanceBinding.inflate(inflater, container, false)

        mAdapter = WorkoutTypeAdapter(CategoryViewItems.getCategoryViewItems())

        binding.gridRecyclerView.adapter = mAdapter

        binding.nextButton.setOnClickListener {
            val selection = mAdapter.mData.filter { it.isSelected }
            if (selection.isEmpty()) {
                Toast.makeText(activity, getString(R.string.empty_selection_warning), LENGTH_SHORT).show()
            } else {
                val workoutTitle = selection.joinToString(separator = " - ") { category ->
                    getString(category.name)
                }
                val workout = Seance(workoutTitle)
                viewModel.addWorkout(workout)
                Navigation.findNavController(it)
                        .navigate(TypeSeanceFragmentDirections.actionTypeSeanceFragmentToAddDateDurationFragment())
            }

        }
        return binding.root
    }


}
