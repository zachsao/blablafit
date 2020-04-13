package com.example.fsudouest.blablafit.features.filters

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.categorie_item.view.*
import kotlinx.android.synthetic.main.category_filter_item.view.*

data class CategoryFilterViewItem(val category: CategoryViewItem): Item(){
    override fun getLayout() = R.layout.category_filter_item

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.title.text = viewHolder.itemView.context.getString(category.name)
        viewHolder.itemView.imageView8.setImageResource(category.iconId)
        viewHolder.itemView.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            category.isSelected = isChecked
        }
    }

}