package com.example.fsudouest.blablafit.features.filters

import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import java.util.*

sealed class FiltersState {
    abstract val data: FiltersData
}

data class FiltersData(
        val city: String? = null,
        val date: Date? = null,
        val categories: List<CategoryViewItem> = emptyList()
)