package com.example.fsudouest.blablafit.features.messages.conversation


import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Chat
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.chat_to_item.view.*
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

        val convId = intent.getStringExtra("convId")
        val currentUser = FirebaseAuth.getInstance().currentUser

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
        viewModel.getConversation(convId, currentUser?.uid ?: "")

        sendMessageButton.setOnClickListener{
            if (chatEdit.text.isNotEmpty()){
                viewModel.sendMessage(convId, chatEdit.text.toString(), currentUser?.uid)
                closeKeyboard()
                chatEdit.setText("")
            }

        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    private fun submitList(list: List<Group>){
        adapter.clear()
        list.forEach { adapter.add(it) }
        chatRecyclerView.adapter = adapter
    }

    private fun initRecyclerView(){
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ConversationActivity)
            this.adapter = adapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class ChatFromItem(val chat: Chat): Item<ViewHolder>(), Group{
    override fun getLayout(): Int {
        return R.layout.chat_from_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_from_content_textView.text = chat.message
    }

}

class ChatToItem(val chat: Chat): Item<ViewHolder>(), Group{
    override fun getLayout(): Int {
        return R.layout.chat_to_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_content_textView.text = chat.message
    }

}
