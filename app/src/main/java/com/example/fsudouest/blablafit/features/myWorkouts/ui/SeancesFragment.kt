package com.example.fsudouest.blablafit.features.myWorkouts.ui


import android.app.DatePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.adapters.SeanceAdapter
import com.example.fsudouest.blablafit.utils.SwipeToDeleteCallback
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


import javax.inject.Inject


/**
 * A simple [Fragment] subclas.
 */
class SeancesFragment : Fragment(), Injectable {

    private lateinit var mAdapter: SeanceAdapter
    private lateinit var mList: RecyclerView
    private val layoutManager = LinearLayoutManager(activity)
    private lateinit var mEmptyStateTextView: TextView
    private lateinit var mProgressView: View

    @Inject
    lateinit var mDatabase: FirebaseFirestore

    @Inject
    lateinit var mFirebaseAuth: FirebaseAuth

    private lateinit var viewModel: WorkoutsViewModel

    private var user: FirebaseUser? = null

    private var seances = ArrayList<Seance?>()

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSeancesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_seances, container, false)


        user = mFirebaseAuth.currentUser

        mEmptyStateTextView = binding.emptyStateTextView
        mProgressView = binding.seancesProgress
        mList = binding.rvSeances

        viewModel = ViewModelProviders.of(this).get(WorkoutsViewModel::class.java).apply {
            workoutsLiveData().observe(this@SeancesFragment, androidx.lifecycle.Observer {
                Log.i("SeanceFragment","Observing workouts")
                showError(false)
                showProgress(true)
                mAdapter = SeanceAdapter(activity!!,it)
                displayList(it)
                seances = it
            })
        }


        //display today's date
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        c.set(year, currentMonth, day,0,0)
        var debutJournee = c.time
        c.set(year, currentMonth, day,23,59)
        var finDeJournee = c.time
        viewModel.getWorkouts(debutJournee,finDeJournee,user?.email,mDatabase)

        val dateFormat = SimpleDateFormat("EEEE dd MMM", Locale.FRENCH)

        binding.dateSelectionButton.text = dateFormat.format(c.time)

        //display date picker when the date button is clicked
        val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month,0,0)
            debutJournee = c.time
            c.set(year, month, day_of_month,23,59)
            finDeJournee = c.time
            binding.dateSelectionButton.text = dateFormat.format(c.time)
            viewModel.getWorkouts(debutJournee,finDeJournee,user?.email,mDatabase)
        }, year, currentMonth, day)

        // If there is a network connection, fetch data
        if (isOnline() && user!=null ) {
            showError(false)
            binding.dateSelectionButton.setOnClickListener {
                datePickerDialog.show()
            }
        } else {
            showError(true)
            // Update empty state with no connection error message
            mEmptyStateTextView.text = getString(R.string.no_internet_connection)
        }

        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mList.adapter as SeanceAdapter
                val deletedIndex = viewHolder.adapterPosition
                val deletedItem = seances[deletedIndex]
                adapter.removeAt(deletedIndex)
                showSnackBar(deletedItem!!,deletedIndex)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mList)

        return binding.root
    }

    private fun displayList(workouts: ArrayList<Seance?>) {
        showProgress(false)
        if (workouts.isNotEmpty()){
            mList.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(activity)
                setHasFixedSize(true)
            }
        }else showError(true)

    }

    private fun showError(show: Boolean) {
        binding.emptyStateTextView.visibility = if (show) View.VISIBLE else View.GONE
        mList.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun isOnline():Boolean{
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






    fun showSnackBar(deletedItem: Seance, deletedIndex: Int){
        // showing snack bar with Undo option
            Snackbar.make(binding.coordinatorLayout, "Séance supprimée", Snackbar.LENGTH_LONG)
                .addCallback(object : Snackbar.Callback(){
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        Log.d("Seances Fragment", "Snackbar dismissed")
                        if(event == DISMISS_EVENT_TIMEOUT)
                            viewModel.deleteWorkout(deletedItem.id, mDatabase)
                    }
                })
                .setAction("ANNULER"){
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex)
                }
                .show()

    }

}
