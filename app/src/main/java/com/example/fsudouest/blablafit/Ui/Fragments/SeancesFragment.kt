package com.example.fsudouest.blablafit.Ui.Fragments


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.Adapters.SeanceAdapter

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source


import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class SeancesFragment : Fragment() {

    private var rootView: View? = null

    private var mAdapter: SeanceAdapter? = null
    private var mList: RecyclerView? = null
    private val layoutManager = LinearLayoutManager(activity)
    private var mEmptyStateTextView: TextView? = null
    private var mProgressView: View? = null

    lateinit var mDatabase: FirebaseFirestore
    internal var user: FirebaseUser? = null

    internal var seances = ArrayList<Seance>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seances, container, false)

        mDatabase = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance().currentUser

        mEmptyStateTextView = rootView!!.findViewById(R.id.empty_state_textView)
        mProgressView = rootView!!.findViewById(R.id.seances_progress)
        mList = rootView!!.findViewById(R.id.rv_seances)


        // Get a reference to the ConnectivityManager to check state of network connectivity
        val connMgr = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Get details on the currently active default data network
        val networkInfo = connMgr.activeNetworkInfo

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected) {
            // Show a progress spinner, and kick off a background task
            showProgress(true)
            getSeances()
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


    fun getSeances() {
        val ref = mDatabase.collection("workouts")

        // Source can be CACHE, SERVER, or DEFAULT.
        val source = Source.SERVER

        // Get the document, forcing the SDK to use the offline cache
        ref.whereEqualTo("createur", user!!.email)
                .get(source)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showProgress(false)
                        for (document in task.result!!) {
                            seances.add(document.toObject(Seance::class.java))
                        }

                        if (seances.isEmpty()) {
                            mList!!.visibility = View.GONE
                            mEmptyStateTextView!!.visibility = View.VISIBLE
                            // Update empty state with no connection error message
                            mEmptyStateTextView!!.text = getString(R.string.no_seance_available)
                        } else {
                            mAdapter = SeanceAdapter(activity!!, seances)
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
