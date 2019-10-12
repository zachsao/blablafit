package com.example.fsudouest.blablafit.features.nearby.ui

import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.MostRecentItemBinding
import com.xwray.groupie.databinding.BindableItem

data class LatestWorkoutViewItem(
        val id: String = "",
        val title: String = "",
        val address: String = "",
        val placesAvailable: String = "",
        val authorName: String = "",
        val authorPhotoUrl: String? = null,
        val time: String = ""
): BindableItem<MostRecentItemBinding>() {
    override fun bind(viewBinding: MostRecentItemBinding, position: Int) {
        viewBinding.title.text = title
        viewBinding.address.text = address
        viewBinding.placesAvailable.text = placesAvailable
        viewBinding.time.text = time
        viewBinding.authorName.text = authorName

        if (!authorPhotoUrl.isNullOrEmpty()) {
            Glide.with(viewBinding.root.context)
                    .load(authorPhotoUrl)
                    .fallback(R.drawable.userphoto)
                    .into(viewBinding.authorPhoto)
        }
    }

    override fun getLayout(): Int = R.layout.most_recent_item
}