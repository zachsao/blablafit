package com.example.fsudouest.blablafit.features.conversation


import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fsudouest.blablafit.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.chat_to_item.view.*

@AndroidEntryPoint
class ConversationActivity : AppCompatActivity() {

    private lateinit var messagesSection: Section

    private val viewModel: ConversationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        supportActionBar?.apply {
            title = intent.getStringExtra("contactName")
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel.stateLiveData().observe(this, { render(it) })
        val contactId = intent.getStringExtra("userId") ?: error("Contact Id is null")
        viewModel.getOrCreateConversation(contactId)

        sendMessageButton.setOnClickListener {
            if (chatEdit.text.isNotEmpty()) {
                viewModel.sendMessage(chatEdit.text.toString(), contactId)
                closeKeyboard()
                chatEdit.text.clear()
            }
        }
    }

    private fun render(state: ConversationState) {
        when (state) {
            is ConversationState.Idle -> Unit
            is ConversationState.ConversationCreated,
            is ConversationState.ConversationLoaded -> {
                state.data.conversationId?.let { viewModel.listenToMessages(it) }
            }
            is ConversationState.ChatsUpdated -> submitList(state.data.chats)
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun submitList(list: List<Item>) {
        if (chatRecyclerView.adapter == null) initRecyclerView()
        messagesSection.update(list)
        chatRecyclerView.scrollToPosition(chatRecyclerView.adapter?.itemCount?.dec() ?: 0)
    }

    private fun initRecyclerView() {
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConversationActivity)
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                messagesSection = Section()
                add(messagesSection)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
