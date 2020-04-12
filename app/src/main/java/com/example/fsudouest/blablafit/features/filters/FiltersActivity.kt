package com.example.fsudouest.blablafit.features.filters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.features.nearby.ui.CategoryViewItem
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_filters.*
import timber.log.Timber
import javax.inject.Inject

class FiltersActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory<FiltersViewModel>

    private lateinit var section: Section

    private val viewModel by lazy { ViewModelProvider(this, factory).get(FiltersViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)

        title = getString(R.string.filters_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)

        initCategories()

        viewModel.stateLiveData().observe(this, Observer {
            render(it)
        })
    }

    private fun render(state: FiltersState) {
        when (state) {
            is FiltersState.Initial -> {
                initPlaces(state.country)
                initDate(state.data.date)
                section.update(state.data.categories)
            }
        }
    }

    private fun initCategories() {
        section = Section()
        recyclerView.apply {
            adapter = GroupAdapter<GroupieViewHolder>().apply { add(section) }
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initDate(date: String?) {
        dateButton.text = date
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.filters_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
                        // set city filter
                    }
                    override fun onError(status: Status) {
                        Timber.e("An error occurred: $status")
                    }
                })

    }
}
