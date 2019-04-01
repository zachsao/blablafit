package com.example.fsudouest.blablafit.Ui.Fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
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
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.Adapters.SeanceAdapter
import com.example.fsudouest.blablafit.Util.SwipeToDeleteCallback
import com.example.fsudouest.blablafit.di.Injectable
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_seances.*
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


    private var user: FirebaseUser? = null

    private var seances = ArrayList<Seance>()

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSeancesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_seances, container, false)


        user = mFirebaseAuth.currentUser

        mEmptyStateTextView = binding.emptyStateTextView
        mProgressView = binding.seancesProgress
        mList = binding.rvSeances

        //display today's date
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val dateFormat = SimpleDateFormat("EEEE dd MMM", Locale.FRENCH)

        binding.dateSelectionButton.text = dateFormat.format(c.time)

        //display date picker when the date button is clicked
        val datePickerDialog = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month,0,0)
            val debutJournee = c.time
            c.set(year, month, day_of_month,23,59)
            val finDeJournee = c.time
            binding.dateSelectionButton.text = dateFormat.format(c.time)
            getSeances(debutJournee,finDeJournee)
        }, year, currentMonth, day)

        // If there is a network connection, fetch data
        if (isOnline() && user!=null ) {
            c.set(year, currentMonth, day,0,0)
            val dayBegin = c.time
            c.set(year, currentMonth, day,23,59)
            val dayEnd = c.time
            getSeances(dayBegin,dayEnd)
            binding.dateSelectionButton.setOnClickListener {
                datePickerDialog.show()
            }
        } else {
            mList.visibility = View.GONE
            mEmptyStateTextView.visibility = View.VISIBLE
            // Update empty state with no connection error message
            mEmptyStateTextView.text = getString(R.string.no_internet_connection)
        }

        val swipeHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = mList.adapter as SeanceAdapter
                val deletedIndex = viewHolder.adapterPosition
                val deletedItem = seances[deletedIndex]
                adapter.removeAt(deletedIndex)

                showSnackBar(deletedItem,deletedIndex)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(mList)

        return binding.root
    }

    private fun isOnline():Boolean{
        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Get details on the currently active default data network
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mList.visibility = if (show) View.GONE else View.VISIBLE
        mList.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mList.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        mProgressView.visibility = if (show) View.VISIBLE else View.GONE
        mProgressView.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mProgressView.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }


    fun getSeances(debutJournee: Date,finJournee: Date=Date()) {
        // Show a progress spinner, and kick off a background task
        showProgress(true)
        Log.i("SeancesFragment","récupération des séances")
        val ref = mDatabase.collection("workouts")

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER

        // Get the document, forcing the SDK to use the offline cache
        ref.whereEqualTo("createur", user!!.email).whereGreaterThanOrEqualTo("date",debutJournee)
                .whereLessThanOrEqualTo("date",finJournee)
                .get(source)
                .addOnCompleteListener { task ->
                    showProgress(false)
                    if (task.isSuccessful) {
                        seances.clear()
                        for (document in task.result!!) {
                            seances.add(document.toObject(Seance::class.java))
                        }

                        if (seances.isEmpty()) {
                            mList.visibility = View.GONE
                            mEmptyStateTextView.visibility = View.VISIBLE
                            // Update empty state with no connection error message
                            mEmptyStateTextView.text = getString(R.string.no_seance_available)
                        } else {
                            mEmptyStateTextView.visibility = View.GONE
                            mAdapter = SeanceAdapter(activity!!, seances)
                            mList.adapter = mAdapter
                            mList.layoutManager = layoutManager
                        }

                    } else {
                        mList.visibility = View.GONE
                        mEmptyStateTextView.visibility = View.VISIBLE
                        // Update empty state with no connection error message
                        mEmptyStateTextView.text = getString(R.string.server_error)
                        Log.e("Seances Fragment", "Error getting documents: ", task.exception)
                    }
                }
    }

    fun showSnackBar(deletedItem: Seance, deletedIndex: Int){
        // showing snack bar with Undo option
            Snackbar.make(binding.coordinatorLayout, "Séance supprimée", Snackbar.LENGTH_LONG)
                .setAction("ANNULER"){
                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex)
                }
                .show()

    }

}
