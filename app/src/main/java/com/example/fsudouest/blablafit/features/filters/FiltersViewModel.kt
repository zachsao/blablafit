package com.example.fsudouest.blablafit.features.filters

import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.service.LocationService
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

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
        val workoutFilter = WorkoutFilter(
                previousData().city,
                previousData().date,
                previousData().categories.map { it.category.name }
        )
        stateLiveData.value = FiltersState.FiltersSaved(previousData(), workoutFilter)
    }

    private fun getCountry() {
        locationService.getCountryFromLastLocation { country ->
            val today = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(LocalDate.now())
            country?.let {
                stateLiveData.value = FiltersState.Initial(previousData().copy(categories = CategoryViewItems.getCategoryViewItems().map { CategoryFilterViewItem(it) }, date = today), country)
            }
        }
    }

    private fun previousData() = stateLiveData.value?.data ?: FiltersData()

}

data class WorkoutFilter(
        val city: String?,
        val date: String?,
        val categories: List<Int>
): Serializable