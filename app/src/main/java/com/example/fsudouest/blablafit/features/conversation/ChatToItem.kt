package com.example.fsudouest.blablafit.features.conversation

import androidx.core.view.isVisible
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Chat
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.chat_to_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatToItem(val chat: Chat) : Item() {
    override fun getLayout(): Int {
        return R.layout.chat_to_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_to_content_textView.text = chat.message
        viewHolder.itemView.timestamp_to.text = SimpleDateFormat("d MMM. HH:mm", Locale.ROOT).format(chat.timestamp)
        viewHolder.itemView.chat_to_content_textView.setOnClickListener {
            viewHolder.itemView.timestamp_to.isVisible = !viewHolder.itemView.timestamp_to.isVisible
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ChatToItem || other.chat.message != chat.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ChatToItem)
    }

}