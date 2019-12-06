package com.example.fsudouest.blablafit.features.accountSetup.basicinformation

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupState
import com.example.fsudouest.blablafit.features.accountSetup.AccountSetupViewModel
import com.example.fsudouest.blablafit.features.login.LoginActivity
import com.example.fsudouest.blablafit.utils.CanSelectPhotoFromGallery
import com.example.fsudouest.blablafit.utils.RC_PHOTO_PICKER
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions
import kotlinx.android.synthetic.main.basic_information_fragment.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.intentFor
import java.util.*
import javax.inject.Inject
private const val REQUEST_CODE_AUTOCOMPLETE = 1001
class BasicInformationFragment : Fragment(), Injectable, CanSelectPhotoFromGallery {

    @Inject
    lateinit var factory: ViewModelFactory<AccountSetupViewModel>
    private lateinit var viewModel: AccountSetupViewModel
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.basic_information_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this, factory)[AccountSetupViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        viewModel.stateLiveData().observe(this, Observer {
            render(it)
        })

        initDatePicker()
        profilePicture.setOnClickListener { createPhotoUpdateDialog(activity!!, this) }
        birthdayEdit.setOnClickListener { datePickerDialog.show() }
        cityEdit.setOnClickListener { launchPlacesSearchActivity() }
        next.setOnClickListener { viewModel.submitBasicInfoForm() }
        cancel.setOnClickListener {
            startActivity(intentFor<LoginActivity>().newTask().clearTask())
        }
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
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == Activity.RESULT_OK) {
            val feature = PlaceAutocomplete.getPlace(data)
            viewModel.updateCity(feature.placeName())
        } else if (requestCode == RC_PHOTO_PICKER && resultCode == Activity.RESULT_OK) {
            data?.data?.let { viewModel.updateStatePictureUri(it) }
        }
    }

    private fun render(state: AccountSetupState) {
        when(state){
            is AccountSetupState.Idle -> {
                val userName = activity?.intent?.getStringExtra("userName")
                if (userName.isNullOrEmpty()) viewModel.getUsername() else nameEdit.setText(userName)
                birthdayEdit.setText(state.data.birthday)
                cityEdit.setText(state.data.city)
                displayPicture(state.data.profilePictureUri)
            }
            is AccountSetupState.NameChanged -> nameEdit.setText(state.data.name)
            is AccountSetupState.DateUpdated -> {
                birthdayEdit.setText(state.data.birthday)
                birthdayInput.error = null
            }
            is AccountSetupState.CityUpdated -> {
                cityEdit.setText(state.data.city)
                cityInput.error = null
            }
            is AccountSetupState.BasicInfoValid -> {
                findNavController().navigate(R.id.action_basicInformationFragment2_to_genderSelectionFragment)
                viewModel.idle()
            }
            is AccountSetupState.Error -> {
                state.data.errors.forEach { error ->
                    when (error) {
                        ValidationError.BirthDateEmpty -> birthdayInput.error = getString(R.string.mandatory_field)
                        ValidationError.CityEmpty -> cityInput.error = getString(R.string.mandatory_field)
                    }
                }
            }
            is AccountSetupState.PictureUpdated -> {
                displayPicture(state.data.profilePictureUri)
            }
        }
    }

    private fun displayPicture(uri: Uri?) {
        Glide.with(activity!!)
                .load(uri)
                .fallback(R.drawable.userphoto)
                .into(profilePicture)
    }

    private fun initDatePicker() {
        val c = Calendar.getInstance()
        val currentDay = c.get(Calendar.DAY_OF_MONTH)
        val currentMonth = c.get(Calendar.MONTH)
        val currentYear = c.get(Calendar.YEAR) - 18

        c.set(currentYear, currentMonth,currentDay)

        datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { i, year, month, day_of_month ->
            val newDate = "$day_of_month/${month.inc()}/$year"
            viewModel.dateChanged(newDate)
        }, currentYear, currentMonth, currentDay )

        datePickerDialog.datePicker.maxDate = c.timeInMillis
    }

}
