package com.example.fsudouest.blablafit.features.profile.buddies


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.fsudouest.blablafit.databinding.FragmentWorkoutBuddiesBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.profile.ProfileState
import com.example.fsudouest.blablafit.features.profile.ProfileViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import javax.inject.Inject

class WorkoutBuddiesFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<ProfileViewModel>

    private val viewModel by lazy { ViewModelProvider(this, factory)[ProfileViewModel::class.java] }

    private lateinit var binding: FragmentWorkoutBuddiesBinding
    private val section = Section()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentWorkoutBuddiesBinding.inflate(inflater, container, false)

        initRecyclerView()

        viewModel.stateLiveData().observe(viewLifecycleOwner, Observer {
            render(it)
        })

        return binding.root
    }

    private fun render(profileState: ProfileState) {
        when (profileState) {
            is ProfileState.BuddiesLoaded -> {
                updateList(profileState.data.buddies)
                showList(true)
                showEmptyMessage(false)
            }
            is ProfileState.EmptyBuddies -> {
                showList(false)
                showEmptyMessage(true)
            }
        }
    }

    private fun showEmptyMessage(show: Boolean) {
        binding.emptyListText.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun updateList(buddies: List<BuddyViewItem>) {
        section.update(buddies)
    }

    private fun showList(show: Boolean) {
        binding.recyclerView.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply { add(section) }
        }
    }


}
