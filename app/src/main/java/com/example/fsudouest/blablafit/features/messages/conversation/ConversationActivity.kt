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
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.utils.FirestoreUtil
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.chat_from_item.view.*
import kotlinx.android.synthetic.main.chat_to_item.view.*
import javax.inject.Inject

class ConversationActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private lateinit var viewModel: ConversationViewModel
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    private lateinit var currentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        supportActionBar.apply {
            title = intent.getStringExtra("contactName")
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        FirestoreUtil.getCurrentUser {
            currentUser = it
        }

        viewModel = ViewModelProviders.of(this, factory).get(ConversationViewModel::class.java).apply {
            chatsLiveData().observe(this@ConversationActivity, Observer {
                submitList(it)
            })
        }
        val otherUserId = intent.getStringExtra("userId")
        viewModel.getOrCreateConversation(otherUserId) { conversationId ->
            viewModel.listenToMessages(conversationId)

            sendMessageButton.setOnClickListener{
                if (chatEdit.text.isNotEmpty()){
                    viewModel.sendMessage(conversationId, chatEdit.text.toString(), otherUserId, currentUser.nomComplet)
                    closeKeyboard()
                    chatEdit.text.clear()
                }

            }
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    private fun submitList(list: List<Item>){
        fun init(){
            chatRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@ConversationActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(list)
                    add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        if (shouldInitRecyclerView) init() else messagesSection.update(list)
        chatRecyclerView.scrollToPosition(chatRecyclerView.adapter?.itemCount?.dec() ?: 0)
    }


    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

class ChatFromItem(val chat: Chat): Item(){
    override fun getLayout(): Int {
        return R.layout.chat_from_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_from_content_textView.text = chat.message
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ChatFromItem || other.chat.message != chat.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ChatFromItem)
    }
}

class ChatToItem(val chat: Chat): Item(){
    override fun getLayout(): Int {
        return R.layout.chat_to_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.chat_to_content_textView.text = chat.message
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ChatToItem || other.chat.message != chat.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ChatToItem)
    }

}
