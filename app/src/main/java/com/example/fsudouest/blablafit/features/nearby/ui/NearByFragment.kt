package com.example.fsudouest.blablafit.features.nearby.ui


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.adapters.WorkoutDiffUtil
import com.example.fsudouest.blablafit.databinding.FragmentSeancesBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.nearby.viewModel.NearByViewModel
import com.example.fsudouest.blablafit.features.workoutDetails.RESULT_DETAILS
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class NearByFragment : Fragment(), Injectable, NearByAdapter.ClickListener {

    private lateinit var mList: RecyclerView
    private val layoutManager = LinearLayoutManager(activity)
    private lateinit var mProgressView: View
    private lateinit var mEmptyStateTextView: TextView
    lateinit var searchView: SearchView
    private lateinit var binding: FragmentSeancesBinding
    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: NearByViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seances, container, false)

        mEmptyStateTextView = binding.emptyStateTextView
        mProgressView = binding.seancesProgress
        mList = binding.rvSeances

        mList.layoutManager = layoutManager

        viewModel = ViewModelProviders.of(this, factory).get(NearByViewModel::class.java).apply {
            workoutsLiveData().observe(this@NearByFragment, Observer {
                renderList(it)
            })
        }

        // If there is a network connection, fetch data
        if (isOnline()) {
            showProgress(true)
            viewModel.updateWorkouts()

        } else {
            showError(true, getString(R.string.no_internet_connection))
        }

        return binding.root
    }

    override fun navigateToDetails(seanceId: String) {
        /*val intent = Intent(context, DetailsSeanceActivity::class.java)
        intent.putExtra("seance", seance)
        startActivity(intent)*/
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

    private fun renderList(list: ArrayList<Seance?>) {
        showProgress(false)
        if (list.isEmpty()) {
            showError(true, getString(R.string.no_seance_available))
        } else {
            mList.adapter = NearByAdapter(activity!!, list, this)
        }
    }

    private fun showProgress(show: Boolean) {
        mList.visibility = if (show) View.GONE else View.VISIBLE
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seance_filters, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                val diffResult = DiffUtil.calculateDiff(WorkoutDiffUtil(viewModel.workoutsLiveData().value ?: ArrayList(), viewModel.filteredList))
                viewModel.workoutsLiveData().value?.clear()
                viewModel.workoutsLiveData().value?.addAll(viewModel.filteredList)
                binding.rvSeances.adapter?.let { diffResult.dispatchUpdatesTo(it) }

                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(activity, "déployer les filtres", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
