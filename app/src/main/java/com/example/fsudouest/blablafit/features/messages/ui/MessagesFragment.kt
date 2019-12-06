package com.example.fsudouest.blablafit.features.messages.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentMessagesBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.features.messages.viewModel.MessagesViewModel
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.latest_message_item.view.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class MessagesFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory<MessagesViewModel>

    private lateinit var viewModel: MessagesViewModel

    private val adapter = GroupAdapter<GroupieViewHolder>()

    lateinit var binding: FragmentMessagesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)

        initRecyclerView()

        viewModel = ViewModelProviders.of(this, factory).get(MessagesViewModel::class.java).apply {
            usersLiveData().observe(this@MessagesFragment, Observer {
                if (it.isEmpty()) showEmptyState(true)
                else showEmptyState(false)
                submitList(it)
            })
        }

        viewModel.getUsers()
        return binding.root
    }

    private fun submitList(list: List<UserViewItem>) {
        adapter.clear()
        list.forEach { adapter.add(it) }
        binding.messagesRecyclerView.adapter = adapter
    }

    private fun initRecyclerView() {
        binding.messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    private fun showEmptyState(show: Boolean) {
        messagesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
        emptyMessagesLayout.visibility = if (show) View.VISIBLE else View.GONE
    }

}

class UserViewItem(val user: User, val userId: String) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.latest_message_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textView.text = user.nomComplet
        Glide.with(viewHolder.root.context)
                .load(user.photoUrl)
                .placeholder(R.drawable.ic_user_dark)
                .error(R.drawable.ic_user_dark)
                .into(viewHolder.itemView.latest_message_photo_imageView)

        viewHolder.itemView.setOnClickListener {
            it.context.startActivity<ConversationActivity>(
                    "contactName" to user.nomComplet,
                    "userId" to userId
            )
        }
    }

}
