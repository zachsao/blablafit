package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var list: RecyclerView

    private lateinit var viewModel: WorkoutCreationViewModel
    @Inject
    lateinit var factory: ViewModelFactory<WorkoutCreationViewModel>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentTypeSeanceBinding.inflate(inflater, container, false)

        list = binding.gridRecyclerView

        categories = CategoryViewItems.getCategoryViewItems()
        mAdapter = WorkoutTypeAdapter(categories)

        list.apply {
            layoutManager = GridLayoutManager(activity!!, 3)
            adapter = mAdapter
        }

        val args: TypeSeanceFragmentArgs by navArgs()

        binding.nextStepButton.setOnClickListener {
            val selection = mAdapter.mData.filter { it.isSelected }
            if (selection.isEmpty()) {
                Toast.makeText(activity, "Veuillez séléctionner au moins un élément", LENGTH_SHORT).show()
            } else {
                val workoutTitle = selection.joinToString(separator = " - ") { category ->
                    getString(category.name)
                }
                val workout = Seance(workoutTitle)
                viewModel.addWorkout(workout)
                Navigation.findNavController(it)
                        .navigate(TypeSeanceFragmentDirections.
                                actionTypeSeanceFragmentToAddDescriptionFragment(args.choice))
            }

        }
        return binding.root
    }


}
