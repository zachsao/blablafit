package com.example.fsudouest.blablafit.features.nearby.ui


import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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

    private lateinit var mList: RecyclerView
    private lateinit var mEmptyStateTextView: TextView
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
    }

    private fun render(state: NearByState) {
        when(state) {
            is NearByState.Idle -> { displayCategories(state.data.categories) }
            is NearByState.LatestWorkoutsLoaded -> { displayMostRecentWorkouts(state.data.workouts) }
        }
    }

    private fun displayCategories(categories: List<CategoryViewItem>) {
        categoriesSection.addAll(categories)
    }

    fun navigateToDetails(seanceId: String) {
        findNavController().navigate(NearByFragmentDirections.actionTrouverUneSeanceFragmentToDetailsSeanceActivity(seanceId))
    }

    private fun isOnline(): Boolean {
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showError(show: Boolean, errorMessage: String) {
        mList.visibility = if (show) View.GONE else View.VISIBLE
        mEmptyStateTextView.visibility = if (show) View.VISIBLE else View.GONE

        mEmptyStateTextView.text = errorMessage
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
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = categoriesAdapter
        }
    }
}
