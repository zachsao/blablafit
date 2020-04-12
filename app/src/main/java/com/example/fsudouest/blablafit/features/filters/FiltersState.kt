package com.example.fsudouest.blablafit.features.filters

sealed class FiltersState {
    abstract val data: FiltersData

    data class Initial(override val data: FiltersData, val country: String): FiltersState()
}

data class FiltersData(
        val city: String? = null,
        val date: String? = null,
        val categories: List<CategoryFilterViewItem> = emptyList()
)