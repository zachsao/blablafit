package com.example.fsudouest.blablafit.features.workoutDetails.workoutRequests

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.RequestStatus
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_requests.*

@AndroidEntryPoint
class RequestsActivity : AppCompatActivity() {

    private val viewModel: RequestsViewModel by viewModels()

    private lateinit var section: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        supportActionBar?.title = getString(R.string.requests)

        val participants = intent.getStringExtra("participants")?.split(",","{","}", " ")
                ?.filter { it.isNotBlank() }
                ?.map { Pair(it.substringBefore('='), RequestStatus.valueOf(it.substringAfter("="))) }
                ?.toMap() ?: error("participants are null")



        val workoutId = intent.getStringExtra("workoutId") ?: error("workoutId is null")


        initRecyclerView()

        viewModel.init(participants, workoutId)
        viewModel.stateLiveData().observe(this, { state ->
            render(state)
        })
    }

    private fun initRecyclerView() {
        section = Section()
        recyclerView.adapter = GroupAdapter<GroupieViewHolder>().apply { add(section) }
    }

    private fun render(state: RequestsState) {
        when (state) {
            is RequestsState.Loading -> showProgress(true)
            is RequestsState.ItemsUpdated -> {
                showProgress(false)
                showEmptyText(false)
                section.update(state.data.requests)
            }
            is RequestsState.ItemsEmpty -> {
                showProgress(false)
                showEmptyText(true)
            }
            is RequestsState.RequestStatusUpdating -> {
                section.update(state.data.requests)
                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
            is RequestsState.RequestStatusUpdated -> {
                section.update(state.data.requests)
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }

        }
    }

    private fun showProgress(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyText(show: Boolean) {
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        emptyListText.visibility = if (show) View.VISIBLE else View.GONE
    }
}
