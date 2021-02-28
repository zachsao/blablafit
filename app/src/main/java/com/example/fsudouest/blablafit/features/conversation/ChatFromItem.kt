package com.example.fsudouest.blablafit.features.conversation

import androidx.core.view.isVisible
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.model.Chat
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.chat_from_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatFromItem(val chat: Chat) : Item() {
    override fun getLayout(): Int {
        return R.layout.chat_from_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.chat_from_content_textView.text = chat.message
        viewHolder.itemView.timestamp_from.text = SimpleDateFormat("d MMM. HH:mm", Locale.ROOT).format(chat.timestamp)
        viewHolder.itemView.chat_from_content_textView.setOnClickListener {
            viewHolder.itemView.timestamp_from.isVisible = !viewHolder.itemView.timestamp_from.isVisible
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ChatFromItem || other.chat.message != chat.message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ChatFromItem)
    }
}