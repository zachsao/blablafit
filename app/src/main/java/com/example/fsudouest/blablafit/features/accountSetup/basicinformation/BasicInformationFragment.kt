package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.basic_information_fragment.*
import java.util.*
import javax.inject.Inject
private const val REQUEST_CODE_AUTOCOMPLETE = 1001
class BasicInformationFragment : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory
    private lateinit var viewModel: AccountSetupViewModel
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.basic_information_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(AccountSetupViewModel::class.java)
        viewModel.stateLiveData().observe(this, Observer {
            render(it)
        })


        initDatePicker()
        birthdayEdit.setOnClickListener { datePickerDialog.show() }
        cityEdit.setOnClickListener { launchPlacesSearchActivity() }
        next.setOnClickListener { viewModel.submitBasicInfoForm() }
    }

    private fun launchPlacesSearchActivity() {
        val options = PlaceOptions.builder()
                .backgroundColor(resources.getColor(R.color.white))
                .geocodingTypes("place")
                .build()
        PlaceAutocomplete.clearRecentHistory(requireContext())

        val intent = PlaceAutocomplete.IntentBuilder()
                .accessToken(BuildConfig.MAPBOX_PLACES_KEY)
                .placeOptions(options)
                .build(activity)
        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            val feature = PlaceAutocomplete.getPlace(data)
            viewModel.updateCity(feature.placeName())
        }
    }

    private fun render(state: BasicInformationState) {
        when(state){
            is BasicInformationState.Idle -> {
                val userName = activity?.intent?.getStringExtra("userName") ?: "Zacharia Sao"
                nameEdit.setText(userName)
            }
            is BasicInformationState.DateUpdated -> {
                birthdayEdit.setText(state.data.birthday)
                birthdayInput.error = null
            }
            is BasicInformationState.CityUpdated -> {
                cityEdit.setText(state.data.city)
                cityInput.error = null
            }
            is BasicInformationState.Error -> {
                state.data.errors.forEach { error ->
                    when (error) {
                        ValidationError.BirthDateEmpty -> birthdayInput.error = getString(R.string.mandatory_field)
                        ValidationError.CityEmpty -> cityInput.error = getString(R.string.mandatory_field)
                    }
                }
            }
        }
    }

    private fun initDatePicker() {
        val c = Calendar.getInstance()
        val currentDay = c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val currentYear = c.get(Calendar.YEAR)

        datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { i, year, month, day_of_month ->
            val newDate = "$day_of_month/${month.inc()}/$year"
            viewModel.dateChanged(newDate)
        }, currentYear, currentMonth, currentDay )

        val todayDate = c.timeInMillis
        datePickerDialog.datePicker.maxDate = todayDate
    }

}
