package com.example.fsudouest.blablafit.features.nearby.ui


import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_nearby.*
import javax.inject.Inject


class NearByFragment : Fragment(), Injectable {
    
    private lateinit var mostRecentWorkoutsAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var mostRecentSection: Section
    private lateinit var categoriesAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var categoriesSection: Section
    @Inject
    lateinit var factory: ViewModelFactory<NearByViewModel>

    private lateinit var viewModel: NearByViewModel

    private lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initLatestWorkouts()
        initCategories()
        viewModel = ViewModelProvider(this, factory).get(NearByViewModel::class.java).apply {
            stateLiveData().observe(viewLifecycleOwner, Observer {
                render(it)
            })
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seance_filters, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        searchView = searchViewItem.actionView as SearchView
        searchView.queryHint = getString(R.string.searchViewHint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchWorkouts(newText)
                return false
            }
        })
    }

    private fun render(state: NearByState) {
        when(state) {
            is NearByState.Idle -> displayCategories(state.data.categories)
            is NearByState.LatestWorkoutsLoaded -> {
                categoriesSection.clear()
                displayCategories(state.data.categories)
                displayMostRecentWorkouts(state.data.latestWorkouts)
            }
            is NearByState.Loading -> {
                categoriesRecyclerView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            is NearByState.ResultsLoaded -> {
                categoriesRecyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                categoriesSection.update(state.data.searchResults)
            }
        }
    }

    private fun displayCategories(categories: List<CategoryViewItem>) {
        categoriesSection.clear()
        categoriesSection.update(categories)
    }

    private fun navigateToDetails(seanceId: String) {
        findNavController().navigate(NearByFragmentDirections.actionTrouverUneSeanceFragmentToDetailsSeanceActivity(seanceId))
    }

    private fun displayMostRecentWorkouts(workouts: List<LatestWorkoutViewItem?>) {
        mostRecentSection.addAll(workouts)
    }

    private fun initLatestWorkouts(){
        mostRecentWorkoutsAdapter = GroupAdapter()
        mostRecentSection = Section()
        mostRecentWorkoutsAdapter.add(mostRecentSection)
        mostRecentRecyclerView.apply {
            adapter = mostRecentWorkoutsAdapter.apply {
                setOnItemClickListener { item, _ ->
                    navigateToDetails((item as LatestWorkoutViewItem).id)
                }
            }
        }
    }

    private fun initCategories(){
        categoriesAdapter = GroupAdapter()
        categoriesSection = Section()
        categoriesAdapter.add(categoriesSection)
        categoriesRecyclerView.apply {
            adapter = categoriesAdapter.apply {
                setOnItemClickListener { item, _ ->
                    when (item) {
                        is CategoryViewItem -> {
                            searchView.isIconified = false
                            searchView.setQuery(getString(item.name), false)
                        }
                        is WorkoutViewItem -> navigateToDetails(item.seance.id)
                    }
                }
            }
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }
}
