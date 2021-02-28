package com.example.fsudouest.blablafit.features.filters

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.service.LocationService
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(private val locationService: LocationService) : ViewModel() {

    private val stateLiveData = MutableLiveData<FiltersState>()

    fun stateLiveData(): LiveData<FiltersState> = stateLiveData

    init {
        getCountry()
    }

    fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        val newDate = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(LocalDate.of(year, month, dayOfMonth))
        stateLiveData.value = FiltersState.DateUpdated(previousData().copy(date = newDate))
    }

    fun updateCity(name: String) {
        stateLiveData.value = FiltersState.CityUpdated(previousData().copy(city = name))
    }

    fun saveFilters() {
        val workoutFilters = WorkoutFilters(
                previousData().city,
                previousData().date,
                getSelectedCategories()
        )
        val intent = Intent().putExtra(FILTERS_KEY, workoutFilters)
        stateLiveData.value = FiltersState.FiltersSaved(previousData(), intent)
    }

    private fun getCountry() {
        locationService.getCountryFromLastLocation { country ->
            country?.let {
                stateLiveData.value = FiltersState.Initial(previousData().copy(categories = CategoryViewItems.getCategoryViewItems().map { CategoryFilterViewItem(it) }), country)
            }
        }
    }

    private fun getSelectedCategories(): List<Int> {
        return previousData().categories.filter { it.category.isSelected }.map { it.category.name }
    }

    private fun previousData() = stateLiveData.value?.data ?: FiltersData()

}

data class WorkoutFilters(
        val city: String?,
        val date: String?,
        val categories: List<Int>
): Serializable