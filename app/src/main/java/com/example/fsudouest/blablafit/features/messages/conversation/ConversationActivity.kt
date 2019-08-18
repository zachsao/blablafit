package com.example.fsudouest.blablafit.features.messages.conversation


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.messages.ui.MessageViewItem
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.chat_item.view.*
import javax.inject.Inject

class ConversationActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: ConversationViewModel

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        supportActionBar.apply {
            title = intent.getStringExtra("contactName")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initRecyclerView()

        viewModel = ViewModelProviders.of(this, factory).get(ConversationViewModel::class.java).apply {
            chatsLiveData().observe(this@ConversationActivity, Observer {
                submitList(it)
            })
        }

        viewModel.getConversation(intent.getStringExtra("contactName"))
    }

    private fun submitList(list: List<ChatViewItem>){
        adapter.clear()
        list.forEach { adapter.add(it) }
        chatRecyclerView.adapter = adapter
    }

    private fun initRecyclerView(){
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConversationActivity)
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(this@ConversationActivity, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

data class ChatViewItem(val chat: Chat): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chatContentTextView.text = chat.message
    }

}
