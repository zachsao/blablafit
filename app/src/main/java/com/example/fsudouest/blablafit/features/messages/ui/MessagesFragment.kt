package com.example.fsudouest.blablafit.features.messages.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMessagesBinding
import com.example.fsudouest.blablafit.features.messages.MessagesState
import com.example.fsudouest.blablafit.features.messages.viewModel.MessagesViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_messages.*

@AndroidEntryPoint
class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()

    private lateinit var section: Section

    lateinit var binding: FragmentMessagesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)

        initRecyclerView()

        viewModel.stateLiveData().observe(viewLifecycleOwner, { render(it) })
        return binding.root
    }

    private fun render(state: MessagesState) {
        when (state) {
            is MessagesState.Loading -> showProgressBar(true)
            is MessagesState.ConversationsLoaded -> {
                showEmptyState(false)
                showProgressBar(false)
                submitList(state.data.conversations)
            }
            is MessagesState.ConversationsEmpty -> {
                showProgressBar(false)
                showEmptyState(true)
            }
        }
    }

    private fun submitList(list: List<UserViewItem>) {
        section.update(list)
    }

    private fun initRecyclerView() {
        section = Section()
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            this.adapter = GroupAdapter<GroupieViewHolder>().apply { add(section) }
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun showProgressBar(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(show: Boolean) {
        emptyMessagesLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

}

