package com.example.fsudouest.blablafit.features.filters

import android.content.Intent

sealed class FiltersState {
    abstract val data: FiltersData

    data class Initial(override val data: FiltersData, val country: String): FiltersState()
    data class DateUpdated(override val data: FiltersData): FiltersState()
    data class CityUpdated(override val data: FiltersData): FiltersState()
    data class FiltersSaved(override val data: FiltersData, val intent: Intent) : FiltersState()
}

data class FiltersData(
        val city: String? = null,
        val date: String? = null,
        val categories: List<CategoryFilterViewItem> = emptyList()
)