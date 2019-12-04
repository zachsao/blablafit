package com.example.fsudouest.blablafit.features.nearby.ui


import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: NearByViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nearby, container, false)
        val rootView = inflater.inflate(R.layout.fragment_nearby,container, false)
        return rootView//binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initLatestWorkouts()
        initCategories()
        viewModel = ViewModelProviders.of(this, factory).get(NearByViewModel::class.java).apply {
            stateLiveData().observe(this@NearByFragment, Observer {
                render(it)
            })
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seance_filters, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = searchViewItem.actionView as SearchView
        searchView.queryHint = getString(R.string.searchViewHint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
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
                displayMostRecentWorkouts(state.data.workouts)
            }
        }
    }

    private fun displayCategories(categories: List<CategoryViewItem>) {
        categoriesSection.addAll(categories)
    }

    fun navigateToDetails(seanceId: String) {
        findNavController().navigate(NearByFragmentDirections.actionTrouverUneSeanceFragmentToDetailsSeanceActivity(seanceId))
    }

    private fun displayMostRecentWorkouts(workouts: List<LatestWorkoutViewItem>) {
        mostRecentSection.addAll(workouts)
    }

    private fun initLatestWorkouts(){
        mostRecentWorkoutsAdapter = GroupAdapter()
        mostRecentSection = Section()
        mostRecentWorkoutsAdapter.add(mostRecentSection)
        mostRecentRecyclerView.apply {
            adapter = mostRecentWorkoutsAdapter.apply {
                setOnItemClickListener { item, view ->
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
            setHasFixedSize(true)
            adapter = categoriesAdapter.apply {
                setOnItemClickListener { item, _ ->
                    item as CategoryViewItem
                    findNavController()
                            .navigate(NearByFragmentDirections.actionTrouverUneSeanceFragmentToCategoryFragment(getString(item.name)))
                }
            }
        }
    }
}
