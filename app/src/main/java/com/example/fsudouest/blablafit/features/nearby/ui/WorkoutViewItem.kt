package com.example.fsudouest.blablafit.features.nearby.ui

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.utils.toLocalDateTime
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.seance_item3.*
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.util.*

class WorkoutViewItem(val id: String, val title: String, val location: String, val date: Date, val duration: String, val photoUrl: String?) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = title
        viewHolder.duration.text = duration
        viewHolder.date.text = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(date.toLocalDateTime())
        viewHolder.address.text = location

        Glide.with(viewHolder.itemView.context)
                .load(photoUrl)
                .placeholder(R.drawable.userphoto)
                .error(R.drawable.userphoto)
                .into(viewHolder.authorPicture)
    }

    override fun getLayout() = R.layout.seance_item3
}