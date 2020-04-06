package com.example.fsudouest.blablafit.features.nearby.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.core.HasErrorDialog
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.example.fsudouest.blablafit.utils.isMarshmallowOrNewer
import com.example.fsudouest.blablafit.utils.isPermissionGranted
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_nearby.*
import timber.log.Timber
import javax.inject.Inject

private const val PERMISSION_REQUEST_CODE = 1002
private const val SETTINGS_REQUEST_CODE = 1003

class NearByFragment : Fragment(), Injectable, HasErrorDialog {
    override var dialog: AlertDialog? = null

    private lateinit var mostRecentWorkoutsAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var mostRecentSection: Section
    private lateinit var categoriesAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var categoriesSection: Section
    @Inject
    lateinit var factory: ViewModelFactory<NearByViewModel>

    private val viewModel by lazy { ViewModelProvider(this, factory).get(NearByViewModel::class.java) }

    private lateinit var searchView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkLocationPermissions()
        initLatestWorkouts()
        initCategories()
        viewModel.stateLiveData().observe(viewLifecycleOwner, Observer { render(it) })
        setHasOptionsMenu(true)

        buttonAllow.setOnClickListener {
            if (shouldShowRationale()) requirePermission()
            else goToSettings()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    hideMainLayout(false)
                    showMissingPermission(false)
                    viewModel.getLatestWorkouts()
                } else {
                    if (shouldShowRationale()) {
                        showDialog(requireContext(), R.string.location_rationale_title, R.string.location_rationale_message, R.string.error_dismiss)
                    }
                    hideMainLayout(true)
                    showMissingPermission(true)
                }
                return
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SETTINGS_REQUEST_CODE -> {
                checkLocationPermissions()
            }
        }
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
                hideMainLayout(false)
                progressBar.visibility = View.GONE
                emptyStateTextView.visibility = View.GONE
                mostRecentSectionTitle.text = getString(R.string.most_recent, state.data.city ?: "your city")
                displayCategories(state.data.categories)
                displayMostRecentWorkouts(state.data.latestWorkouts)
            }
            is NearByState.Loading -> {
                hideMainLayout(true)
                progressBar.visibility = View.VISIBLE
            }
            is NearByState.ResultsLoaded -> {
                categoriesRecyclerView.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                categoriesSection.update(state.data.searchResults)
            }
            is NearByState.EmptyWorkouts -> {
                hideMainLayout(true)
                progressBar.visibility = View.GONE
                emptyStateTextView.visibility = View.VISIBLE
                emptyStateTextView.text = getString(R.string.nearby_workouts_empty, state.data.city ?: "your city")
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

    private fun shouldShowRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun showMissingPermission(show: Boolean) {
        if (show) emptyStateTextView.text = getString(R.string.location_permission_denied)
        emptyStateTextView.visibility = if (show) View.VISIBLE else View.GONE
        buttonAllow.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun hideMainLayout(hide: Boolean) {
        mostRecentSectionTitle.visibility = if (hide) View.GONE else View.VISIBLE
        mostRecentRecyclerView.visibility = if (hide) View.GONE else View.VISIBLE
        categorySectionTitle.visibility = if (hide) View.GONE else View.VISIBLE
        categoriesRecyclerView.visibility = if (hide) View.GONE else View.VISIBLE
    }

    private fun checkLocationPermissions() {
        if (isMarshmallowOrNewer() && !isPermissionGranted(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            requirePermission()
        } else {
            hideMainLayout(false)
            showMissingPermission(false)
            viewModel.getLatestWorkouts()
        }
    }

    private fun requirePermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
    }



    private fun goToSettings() {
        startActivityForResult(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", requireActivity().packageName, null)),
                SETTINGS_REQUEST_CODE)
    }
}
