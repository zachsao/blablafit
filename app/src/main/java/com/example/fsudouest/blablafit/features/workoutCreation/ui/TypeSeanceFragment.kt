package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.adapters.WorkoutTypeAdapter

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.utils.MyLookup
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

    private var tracker: SelectionTracker<Long>? = null

    private lateinit var viewModel: WorkoutCreationViewModel
    @Inject lateinit var factory: ViewModelFactory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, factory).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentTypeSeanceBinding>(inflater, R.layout.fragment_type_seance, container, false)

        if (savedInstanceState != null)
            tracker?.onRestoreInstanceState(savedInstanceState)

        list = binding.gridRecyclerView

        categories = CategoryViewItems.getCategoryViewItems()
        mAdapter = WorkoutTypeAdapter(activity!!, categories)



        list.apply {
            layoutManager = GridLayoutManager(activity!!, 3)
            adapter = mAdapter
        }

        tracker = SelectionTracker.Builder<Long>(
                "selection-1",
                list,
                StableIdKeyProvider(list),
                MyLookup(list),
                StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
        ).build()

        val args: TypeSeanceFragmentArgs by navArgs()

        mAdapter.setTracker(tracker)
        binding.nextStepButton.setOnClickListener {
            if (tracker!!.selection.isEmpty) {
                Toast.makeText(activity, "Veuillez séléctionner au moins un élément", LENGTH_SHORT).show()
            } else {
                val workoutTitle = tracker!!.selection.joinToString(separator = " - ") { index ->
                    getString(categories[index.toInt()].name)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        tracker?.onSaveInstanceState(outState)
    }


}
