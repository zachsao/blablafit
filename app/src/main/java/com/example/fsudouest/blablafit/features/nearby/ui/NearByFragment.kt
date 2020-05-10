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
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.core.HasErrorDialog
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.filters.FILTERS_KEY
import com.example.fsudouest.blablafit.features.filters.FiltersActivity
import com.example.fsudouest.blablafit.features.filters.WorkoutFilters
import com.example.fsudouest.blablafit.features.nearby.NearByState
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.example.fsudouest.blablafit.utils.isMarshmallowOrNewer
import com.example.fsudouest.blablafit.utils.isPermissionGranted
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_nearby.*
import javax.inject.Inject

private const val PERMISSION_REQUEST_CODE = 1002
private const val SETTINGS_REQUEST_CODE = 1003
private const val REQUEST_CODE_FILTERS = 1004

class NearByFragment : Fragment(), Injectable, HasErrorDialog {
    override var dialog: AlertDialog? = null

    @Inject
    lateinit var factory: ViewModelFactory<NearByViewModel>
    private lateinit var searchView: SearchView

    private val viewModel by lazy { ViewModelProvider(this, factory).get(NearByViewModel::class.java) }
    private val section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nearby, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        checkLocationPermissions()
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
            SETTINGS_REQUEST_CODE -> checkLocationPermissions()
            REQUEST_CODE_FILTERS -> data?.let {
                val filters = it.getSerializableExtra(FILTERS_KEY) as WorkoutFilters
                viewModel.applyFilters(filters)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.nearby_menu, menu)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> startActivityForResult(Intent(requireActivity(), FiltersActivity::class.java), REQUEST_CODE_FILTERS)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun render(state: NearByState) {
        when(state) {
            is NearByState.WorkoutsLoaded -> {
                hideMainLayout(false)
                state.data.city?.let { sectionTitle.text = getString(R.string.all_workouts_section_title, it) } ?: run { sectionTitle.visibility = View.GONE }
                section.update(state.data.allWorkouts)
            }
            is NearByState.Loading -> {
                progressBar.visibility = View.VISIBLE
            }
            is NearByState.ResultsLoaded -> section.update(state.data.searchResults)
            is NearByState.EmptyWorkouts -> {
                hideMainLayout(true)
                emptyStateTextView.text = getString(R.string.nearby_workouts_empty, state.data.city ?: "your city")
            }
        }
    }

    private fun navigateToDetails(seanceId: String) {
        findNavController().navigate(NearByFragmentDirections.actionTrouverUneSeanceFragmentToDetailsSeanceActivity(seanceId))
    }

    private fun initCategories(){
        allWorkoutsRecyclerView.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
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
        progressBar.visibility = View.GONE
        sectionTitle.visibility = if (hide) View.GONE else View.VISIBLE
        allWorkoutsRecyclerView.visibility = if (hide) View.GONE else View.VISIBLE
        emptyStateTextView.visibility = if (hide) View.VISIBLE else View.GONE
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
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", requireActivity().packageName, null)),
                SETTINGS_REQUEST_CODE)
    }
}
