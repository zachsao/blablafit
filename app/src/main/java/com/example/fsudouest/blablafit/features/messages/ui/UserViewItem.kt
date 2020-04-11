package com.example.fsudouest.blablafit.features.messages.ui

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.conversation.ConversationActivity
import com.example.fsudouest.blablafit.model.User
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_item.view.*
import org.jetbrains.anko.startActivity

class UserViewItem(val userId: String, val userName: String, val userPhotoUrl: String) : Item<GroupieViewHolder>() {
    override fun getLayout() = R.layout.latest_message_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textView.text = userName
        Glide.with(viewHolder.root.context)
                .load(userPhotoUrl)
                .placeholder(R.drawable.ic_user_dark)
                .error(R.drawable.ic_user_dark)
                .into(viewHolder.itemView.latest_message_photo_imageView)

        viewHolder.itemView.setOnClickListener {
            it.context.startActivity<ConversationActivity>(
                    "contactName" to userName,
                    "userId" to userId
            )
        }
    }

}