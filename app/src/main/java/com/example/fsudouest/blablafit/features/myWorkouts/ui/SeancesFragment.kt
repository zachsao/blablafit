package com.example.fsudouest.blablafit.features.myWorkouts.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.myWorkouts.MyWorkoutsState
import com.example.fsudouest.blablafit.features.myWorkouts.viewModel.WorkoutsViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.WorkoutViewItem
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.android.synthetic.main.fragment_seances.*
import org.jetbrains.anko.image
import javax.inject.Inject

class SeancesFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<WorkoutsViewModel>
    private val viewModel by lazy { ViewModelProviders.of(this, factory)[WorkoutsViewModel::class.java] }

    private var section = Section()

    private lateinit var binding: com.example.fsudouest.blablafit.databinding.FragmentSeancesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seances, container, false)

        initRecyclerView()

        viewModel.stateLiveData().observe(this, Observer { state -> render(state) })

        viewModel.getMyWorkouts()
        viewModel.getJoinedWorkouts()

        binding.swiperefresh.setOnRefreshListener {
            viewModel.getMyWorkouts()
            viewModel.getJoinedWorkouts()
        }
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvSeances.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
                // addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                setOnItemClickListener { item, view ->
                    item as WorkoutViewItem
                    navigateToDetails(item.seance.id)
                }
            }
        }
    }

    private fun render(state: MyWorkoutsState) {
        when (state){
            is MyWorkoutsState.Loading -> showProgress(true)
            is MyWorkoutsState.WorkoutsEmpty -> {
                binding.swiperefresh.isRefreshing = false
                showProgress(false)
                showEmptyState(R.string.no_seance_available, R.drawable.ic_undraw_healthy_habit)
            }
            is MyWorkoutsState.WorkoutsLoaded -> {
                binding.swiperefresh.isRefreshing = false
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
        binding.emptyStateTextView.text = getString(errorMessageId)
        binding.emptyStateImageView.setImageResource(imageId)
        binding.emptyStateTextView.visibility =  View.VISIBLE
        binding.emptyStateImageView.visibility = View.VISIBLE
        binding.rvSeances.visibility = View.GONE
    }

    private fun hideEmptyState(){
        binding.emptyStateTextView.text = null
        binding.emptyStateImageView.image = null
        binding.emptyStateTextView.visibility =  View.GONE
        binding.emptyStateImageView.visibility = View.GONE
        binding.rvSeances.visibility = View.VISIBLE
    }

    private fun showProgress(show: Boolean) {
        binding.seancesProgress.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun navigateToDetails(seanceId: String) {
        findNavController().navigate(SeancesFragmentDirections.actionSeancesFragmentToDetailsSeanceActivity(seanceId))
    }
}
