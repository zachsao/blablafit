package com.example.fsudouest.blablafit.features.nearby.ui

import com.example.fsudouest.blablafit.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.categorie_item.*


data class CategoryViewItem(val name: String = "") : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.title.text = name
    }
    override fun getLayout(): Int = R.layout.categorie_item
}

object CategoryViewItems {
    private fun  fullBody() = CategoryViewItem("Full body")
    private fun  upperBody() = CategoryViewItem("Upper body")
    private fun  chest() = CategoryViewItem("Chest")
    private fun  back() = CategoryViewItem("Back")
    private fun  shoulders() = CategoryViewItem("Shoulders")
    private fun  legs() = CategoryViewItem("Legs")
    private fun  biceps() = CategoryViewItem("Biceps")
    private fun  triceps() = CategoryViewItem("Triceps")
    private fun  arms() = CategoryViewItem("Arms")
    private fun  yoga() = CategoryViewItem("Yoga - Stretching")
    private fun  cardio() = CategoryViewItem("Cardio")
    private fun  abs() = CategoryViewItem("Abs - core")

    fun getCategoryViewItems():List<CategoryViewItem> {
        return listOf(
                fullBody(),
                upperBody(),
                chest(),
                back(),
                legs(),
                shoulders(),
                arms(),
                biceps(),
                triceps(),
                cardio(),
                yoga(),
                abs()
        )
    }
}
