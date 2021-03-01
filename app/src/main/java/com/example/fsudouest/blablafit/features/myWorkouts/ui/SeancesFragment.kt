package com.example.fsudouest.blablafit.features.myWorkouts.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.myWorkouts.MyWorkoutsState
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_seances.*
import org.jetbrains.anko.image

@AndroidEntryPoint
class SeancesFragment : Fragment() {

    private val viewModel: WorkoutsViewModel by viewModels()

    private var section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seances, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.stateLiveData().observe(viewLifecycleOwner, { state -> render(state) })

        viewModel.getMyWorkouts()
        viewModel.getJoinedWorkouts()

        swiperefresh.setOnRefreshListener {
            viewModel.getMyWorkouts()
            viewModel.getJoinedWorkouts()
        }
    }

    private fun initRecyclerView() {
        rv_seances.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
                setOnItemClickListener { item, view ->
                    item as WorkoutViewItem
                    navigateToDetails(item.id)
                }
            }
        }
    }

    private fun render(state: MyWorkoutsState) {
        when (state){
            is MyWorkoutsState.Loading -> showProgress(true)
            is MyWorkoutsState.WorkoutsEmpty -> {
                swiperefresh.isRefreshing = false
                showProgress(false)
                showEmptyState(R.string.no_seance_available, R.drawable.ic_undraw_healthy_habit)
            }
            is MyWorkoutsState.WorkoutsLoaded -> {
                swiperefresh.isRefreshing = false
                showProgress(false)
                updateList(state)
                hideEmptyState()
            }
        }
    }

    private fun updateList(state: MyWorkoutsState.WorkoutsLoaded) {
        section.update(state.data.myWorkouts.plus(state.data.joinedWorkouts))
    }


    private fun showEmptyState(@StringRes errorMessageId: Int, @DrawableRes imageId: Int) {
        emptyStateTextView.text = getString(errorMessageId)
        emptyStateImageView.setImageResource(imageId)
        emptyStateTextView.visibility = View.VISIBLE
        emptyStateImageView.visibility = View.VISIBLE
        rv_seances.visibility = View.GONE
    }

    private fun hideEmptyState() {
        emptyStateTextView.text = null
        emptyStateImageView.image = null
        emptyStateTextView.visibility = View.GONE
        emptyStateImageView.visibility = View.GONE
        rv_seances.visibility = View.VISIBLE
    }

    private fun showProgress(show: Boolean) {
        seances_progress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToDetails(seanceId: String) {
        findNavController().navigate(SeancesFragmentDirections.actionSeancesFragmentToDetailsSeanceFragment(seanceId))
    }
}
