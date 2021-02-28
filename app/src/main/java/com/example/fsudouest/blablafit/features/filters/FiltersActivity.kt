package com.example.fsudouest.blablafit.features.filters

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.R
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_filters.*
import kotlinx.android.synthetic.main.category_filter_item.view.*
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import timber.log.Timber

const val FILTERS_KEY = "WORKOUT_FILTERS"

@AndroidEntryPoint
class FiltersActivity : AppCompatActivity() {

    private lateinit var section: Section
    private lateinit var datePickerDialog: DatePickerDialog

    private val viewModel: FiltersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)

        title = getString(R.string.filters_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        initCategories()
        initDatePicker()

        viewModel.stateLiveData().observe(this, Observer {
            render(it)
        })

        dateButton.setOnClickListener { datePickerDialog.show() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                viewModel.saveFilters()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filters_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()
        return true
    }

    private fun render(state: FiltersState) {
        when (state) {
            is FiltersState.Initial -> {
                initPlaces(state.country)
                section.update(state.data.categories)
            }
            is FiltersState.DateUpdated -> dateButton.text = state.data.date
            is FiltersState.FiltersSaved -> {
                setResult(Activity.RESULT_OK, state.intent)
                finish()
            }
        }
    }

    private fun initCategories() {
        section = Section()
        recyclerView.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply {
                add(section)
                setOnItemClickListener { item, view ->
                    view.checkbox.isChecked = !view.checkbox.isChecked
                    (item as CategoryFilterViewItem).category.isSelected = view.checkbox.isChecked
                }
            }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initPlaces(country: String?) {
        Places.initialize(this, BuildConfig.GOOGLE_PLACES_KEY)

        val autocompleteFragment = place_autocomplete as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME))
                .setHint(getString(R.string.city))
                .setTypeFilter(TypeFilter.CITIES)
                .setCountry(country)
                .setOnPlaceSelectedListener(object : PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place) {
                        place.name?.let { viewModel.updateCity(it) }
                    }
                    override fun onError(status: Status) {
                        Timber.e("An error occurred: $status")
                    }
                })

    }

    private fun initDatePicker() {
        val today = LocalDate.now()
        datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            viewModel.updateDate(year, month.inc(), day_of_month)
        }, today.year, today.monthValue.dec(), today.dayOfMonth)

        datePickerDialog.datePicker.minDate = Instant.now().toEpochMilli()
    }
}
