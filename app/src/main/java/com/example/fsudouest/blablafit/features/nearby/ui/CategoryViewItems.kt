package com.example.fsudouest.blablafit.features.nearby.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.CategorieItemBinding
import com.xwray.groupie.databinding.BindableItem
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.categorie_item.*


data class CategoryViewItem(@StringRes val name: Int, @DrawableRes val iconId: Int, var isSelected: Boolean = false, @ColorRes val colorId: Int) : BindableItem<CategorieItemBinding>() {
    override fun bind(viewBinding: CategorieItemBinding, position: Int) {
        viewBinding.category = this
    }

    override fun getLayout(): Int = R.layout.categorie_item
}

object CategoryViewItems {
    private fun  fullBody() = CategoryViewItem(R.string.full_body, R.drawable.icons8_weightlifting_50, false, R.color.quantum_deeporange200)
    private fun  upperBody() = CategoryViewItem(R.string.upper_body, R.drawable.icons8_torso_50, false, R.color.quantum_amber200)
    private fun  chest() = CategoryViewItem(R.string.chest, R.drawable.icons8_chest_50, false, R.color.quantum_googblue200)
    private fun  back() = CategoryViewItem(R.string.back, R.drawable.icons8_back_50, false, R.color.quantum_purple200)
    private fun  shoulders() = CategoryViewItem(R.string.shoulders, R.drawable.icons8_shoulders_50, false, R.color.quantum_orange200)
    private fun  legs() = CategoryViewItem(R.string.legs, R.drawable.icons8_leg_50, false, R.color.quantum_teal200)
    private fun  biceps() = CategoryViewItem(R.string.biceps, R.drawable.icons8_biceps_50, false, R.color.quantum_cyan200)
    private fun  triceps() = CategoryViewItem(R.string.triceps, R.drawable.icons8_triceps_50, false, R.color.quantum_googgreen200)
    private fun  arms() = CategoryViewItem(R.string.arms, R.drawable.icons8_arm_50, false, R.color.quantum_indigo200)
    private fun  yoga() = CategoryViewItem(R.string.yoga, R.drawable.icons8_yoga_50, false, R.color.quantum_brown200)
    private fun  cardio() = CategoryViewItem(R.string.cardio, R.drawable.icons8_running_50, false, R.color.quantum_bluegrey200)
    private fun  abs() = CategoryViewItem(R.string.abs, R.drawable.icons8_abs_50, false, R.color.quantum_lightgreen200)

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
