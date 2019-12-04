package com.example.fsudouest.blablafit.features.nearby.ui

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.CategorieItemBinding
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.categorie_item.*


data class CategoryViewItem(@StringRes val name: Int, @DrawableRes val iconId: Int) : BindableItem<CategorieItemBinding>() {
    override fun bind(viewBinding: CategorieItemBinding, position: Int) {
        viewBinding.category = this
    }

    override fun getLayout(): Int = R.layout.categorie_item
}

object CategoryViewItems {
    private fun  fullBody() = CategoryViewItem(R.string.full_body, R.drawable.icons8_weightlifting_50)
    private fun  upperBody() = CategoryViewItem(R.string.upper_body, R.drawable.icons8_torso_50)
    private fun  chest() = CategoryViewItem(R.string.chest, R.drawable.icons8_chest_50)
    private fun  back() = CategoryViewItem(R.string.back, R.drawable.icons8_back_50)
    private fun  shoulders() = CategoryViewItem(R.string.shoulders, R.drawable.icons8_shoulders_50)
    private fun  legs() = CategoryViewItem(R.string.legs, R.drawable.icons8_leg_50)
    private fun  biceps() = CategoryViewItem(R.string.biceps, R.drawable.icons8_biceps_50)
    private fun  triceps() = CategoryViewItem(R.string.triceps, R.drawable.icons8_triceps_50)
    private fun  arms() = CategoryViewItem(R.string.arms, R.drawable.icons8_arm_50)
    private fun  yoga() = CategoryViewItem(R.string.yoga, R.drawable.icons8_yoga_50)
    private fun  cardio() = CategoryViewItem(R.string.cardio, R.drawable.icons8_running_50)
    private fun  abs() = CategoryViewItem(R.string.abs, R.drawable.icons8_abs_50)

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
