package com.example.fsudouest.blablafit.features.myWorkouts.ui


import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.NearByAdapter
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.utils.SwipeToDeleteCallback
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SeancesFragment : Fragment(), Injectable, NearByAdapter.ClickListener {

    private lateinit var mAdapter: SeanceAdapter
    private lateinit var mList: RecyclerView
    private lateinit var mEmptyStateTextView: TextView
    private lateinit var mProgressView: View

    @Inject
    lateinit var factory: ViewModelFactory<WorkoutsViewModel>
    private lateinit var viewModel: WorkoutsViewModel

    private var seances = ArrayList<Seance?>()

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSeancesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seances, container, false)

        mEmptyStateTextView = binding.emptyStateTextView
        mProgressView = binding.seancesProgress
        mList = binding.rvSeances

        viewModel = ViewModelProviders.of(this, factory).get(WorkoutsViewModel::class.java).apply {
            workoutsLiveData().observe(this@SeancesFragment, androidx.lifecycle.Observer {
                Timber.i("Observing workouts")
                mAdapter = SeanceAdapter(it, this@SeancesFragment)
                displayList(it)
                seances = it
            })
        }


        //display today's date
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        c.set(year, currentMonth, day, 0, 0)
        var debutJournee = c.time
        c.set(year, currentMonth, day, 23, 59)
        var finDeJournee = c.time
        viewModel.getWorkouts(debutJournee, finDeJournee)

        val dateFormat = SimpleDateFormat("EEEE dd MMM", Locale.FRENCH)

        binding.dateSelectionButton.text = dateFormat.format(c.time)

        //display date picker when the date button is clicked
        val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month, 0, 0)
            debutJournee = c.time
            c.set(year, month, day_of_month, 23, 59)
            finDeJournee = c.time
            binding.dateSelectionButton.text = dateFormat.format(c.time)
            viewModel.getWorkouts(debutJournee, finDeJournee)
        }, year, currentMonth, day)

        // If there is a network connection, fetch data
        if (isOnline()) {
            showProgress(true)
            binding.dateSelectionButton.setOnClickListener {
                datePickerDialog.show()
            }
        } else {
            showError(R.string.no_internet_connection, R.drawable.ic_undraw_fitness_tracker)
        }

        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mList.adapter as SeanceAdapter
                val deletedIndex = viewHolder.adapterPosition
                val deletedItem = seances[deletedIndex]
                adapter.removeAt(deletedIndex)
                deletedItem?.let { showSnackBar(it, deletedIndex) }
                if (adapter.itemCount == 0){
                    showError(R.string.no_seance_available,R.drawable.ic_undraw_healthy_habit)
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mList)

        return binding.root
    }

    private fun displayList(workouts: ArrayList<Seance?>) {
        showProgress(false)
        if (workouts.isNotEmpty()) {
            hideError()
            mList.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
            }
        } else showError( R.string.no_seance_available, R.drawable.ic_undraw_healthy_habit)

    }

    private fun showError(@StringRes errorMessageId: Int, @DrawableRes imageId: Int) {
        binding.emptyStateTextView.text = getString(errorMessageId)
        binding.emptyStateImageView.setImageResource(imageId)
        binding.emptyStateTextView.visibility =  View.VISIBLE
        binding.emptyStateImageView.visibility = View.VISIBLE
        mList.visibility = View.GONE
    }

    private fun hideError(){
        binding.emptyStateTextView.visibility =  View.GONE
        binding.emptyStateImageView.visibility =  View.GONE
        mList.visibility = View.VISIBLE
    }

    private fun isOnline(): Boolean {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Get details on the currently active default data network
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showProgress(show: Boolean) {
        mList.visibility = if (show) View.GONE else View.VISIBLE
        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun navigateToDetails(seanceId: String) {
        findNavController().navigate(SeancesFragmentDirections.actionSeancesFragmentToDetailsSeanceActivity(seanceId))
    }

    fun showSnackBar(deletedItem: Seance, deletedIndex: Int) {
        // showing snack bar with Undo option
        Snackbar.make(binding.coordinatorLayout, "Séance supprimée", Snackbar.LENGTH_LONG).apply {
            anchorView = activity!!.bottom_navigation
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != DISMISS_EVENT_ACTION)
                        viewModel.deleteWorkout(deletedItem.id)
                }
            })
            setAction("ANNULER") {
                if (deletedIndex == 0) hideError()
                mAdapter.restoreItem(deletedItem, deletedIndex)
            }
        }.show()
    }
}
