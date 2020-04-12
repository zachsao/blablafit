package com.example.fsudouest.blablafit.features.filters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItems
import com.example.fsudouest.blablafit.service.LocationService
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import javax.inject.Inject

class FiltersViewModel @Inject constructor(private val locationService: LocationService) : ViewModel() {

    private val stateLiveData = MutableLiveData<FiltersState>()

    fun stateLiveData(): LiveData<FiltersState> = stateLiveData

    init {
        getCountry()
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