package com.example.fsudouest.blablafit.Ui.Fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.SearchManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.Adapters.SeanceAdapter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source

import java.util.ArrayList


class TrouverUneSeanceFragment : Fragment() {


    private var rootView: View? = null


    private var mAdapter: SeanceAdapter? = null
    private var mList: RecyclerView? = null
    internal var layoutManager = LinearLayoutManager(activity)
    private var mProgressView: View? = null
    private val mySeances = ArrayList<Seance>()
    private var mEmptyStateTextView: TextView? = null
    private val filteredSeances = ArrayList<Seance>()
    lateinit var searchView: SearchView


    lateinit var mDatabase: FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trouver_une_seance, container, false)

        mDatabase = FirebaseFirestore.getInstance()

        mEmptyStateTextView = rootView!!.findViewById(R.id.empty_state_textView)
        mProgressView = rootView!!.findViewById(R.id.seances_progress)
        mList = rootView!!.findViewById(R.id.rv_search_seances)


        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get details on the currently active default data network
        val networkInfo = connMgr.activeNetworkInfo

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected) {
            // Show a progress spinner, and kick off a background task
            showProgress(true)
            getSeances()
            //fetchSeances(BASE_URL,client);

        } else {
            mList!!.visibility = View.GONE
            mEmptyStateTextView!!.visibility = View.VISIBLE
            // Update empty state with no connection error message
            mEmptyStateTextView!!.text = getString(R.string.no_internet_connection)
        }


        return rootView
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

        mList!!.visibility = if (show) View.GONE else View.VISIBLE
        mList!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mList!!.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
        mProgressView!!.animate().setDuration(shortAnimTime.toLong()).alpha(
                (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                mProgressView!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    fun search(query: String) {
        for (seance in mySeances) {
            if (seance.titre.toLowerCase().contains(query)) {
                filteredSeances.add(seance)
            }
        }
        mList!!.layoutManager = layoutManager
        mAdapter = SeanceAdapter(activity!!, filteredSeances)

        mList!!.adapter = mAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.seance_filters, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.isSubmitButtonEnabled = true

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.length > 0) {
                    filteredSeances.clear()
                    search(newText)
                } else {
                    filteredSeances.clear()
                    mList!!.layoutManager = layoutManager
                    mAdapter = SeanceAdapter(activity!!, mySeances)

                    mList!!.adapter = mAdapter
                }
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(activity, "déployer les filtres", Toast.LENGTH_SHORT).show()
                return true
            }
            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }


    fun getSeances() {
        val ref = mDatabase.collection("workouts")

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER

        // Get the document, forcing the SDK to use the offline cache
        ref.get(source)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showProgress(false)
                        for (document in task.result!!) {
                            mySeances.add(document.toObject(Seance::class.java))
                        }

                        if (mySeances.isEmpty()) {
                            mList!!.visibility = View.GONE
                            mEmptyStateTextView!!.visibility = View.VISIBLE
                            mEmptyStateTextView!!.text = getString(R.string.no_seance_available)
                        } else {
                            mAdapter = SeanceAdapter(activity!!, mySeances)
                            mList!!.adapter = mAdapter
                            mList!!.layoutManager = layoutManager
                        }

                    } else {
                        mList!!.visibility = View.GONE
                        mEmptyStateTextView!!.visibility = View.VISIBLE
                        // Update empty state with no connection error message
                        mEmptyStateTextView!!.text = getString(R.string.server_error)
                        Log.d("Seances Fragment", "Error getting documents: ", task.exception)
                    }
                }
    }
}// Required empty public constructor
