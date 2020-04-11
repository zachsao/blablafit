package com.example.fsudouest.blablafit.features.workoutCreation.ui


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentAddDateDurationBinding
import com.example.fsudouest.blablafit.di.Injectable
import com.example.fsudouest.blablafit.features.workoutCreation.viewModel.WorkoutCreationViewModel
import com.example.fsudouest.blablafit.utils.ViewModelFactory
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.android.synthetic.main.fragment_add_date_duration.*
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.text.DateFormat
import java.util.*
import javax.inject.Inject
import android.text.format.DateFormat as AndroidDateFormat


class AddDateDurationFragment : Fragment(), Injectable {

    private val apiKey = BuildConfig.GOOGLE_PLACES_KEY

    private var count = 1
    private lateinit var c: Calendar
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var durationPickerDialog: TimePickerDialog
    private val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
    private val hourFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

    private lateinit var viewModel: WorkoutCreationViewModel
    @Inject
    lateinit var factory: ViewModelFactory<WorkoutCreationViewModel>

    private lateinit var binding: FragmentAddDateDurationBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddDateDurationBinding.inflate(inflater, container, false)

        viewModel = activity?.run {
            ViewModelProvider(this, factory).get(WorkoutCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.workout = viewModel.workoutLiveData.value

        viewModel.getCountry { country -> initPlaces(country) }

        //CHOIX DE LA DATE
        selectDate()
        binding.dateSelectionButton.setOnClickListener {
            datePickerDialog.show()
        }

        //CHOIX DE L'HEURE
        selectHour()
        binding.hourSelectionButton.setOnClickListener {
            timePickerDialog.show()
        }

        //Choix de la duree
        setDuration()
        binding.durationSelectionButton.setOnClickListener {
            durationPickerDialog.show()
        }

        //Choix du nombre de participant
        binding.increment.setOnClickListener { increment() }
        binding.decrement.setOnClickListener { if (count > 1) decrement() }

        binding.createButton.setOnClickListener {
            viewModel.workoutLiveData.apply {
                value?.duree = binding.durationSelectionButton.text.toString()
                value?.maxParticipants = count
                value?.description = descriptionEdit.text.toString()
            }


            if (viewModel.workoutLiveData.value?.location?.name.isNullOrEmpty()) {
                toast(getString(R.string.empty_address_toast_message))
            } else {
                viewModel.addAuthor {
                    viewModel.saveWorkout(
                            {
                                Navigation.findNavController(it)
                                        .navigate(AddDateDurationFragmentDirections.actionAddDateDurationFragmentToSeancesFragment())
                            },
                            { toast("Une erreur s'est produite") }
                    )
                }
            }
        }

        return binding.root
    }

    private fun initPlaces(country: String?) {
        Places.initialize(requireContext(), apiKey)

        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.TYPES, Place.Field.ADDRESS_COMPONENTS))
                .setHint(getString(R.string.searchViewHint))
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountry(country)
                .setOnPlaceSelectedListener(object : PlaceSelectionListener {
                    override fun onPlaceSelected(place: Place) {
                        when {
                            indoorChip.isChecked && !place.types!!.contains(Place.Type.GYM) -> toast(getString(R.string.invalid_place_gym_toast_message))
                            outdoorChip.isChecked && !place.types!!.contains(Place.Type.STREET_ADDRESS) -> toast(getString(R.string.invalid_place_address_toast_message))
                            else -> {
                                viewModel.setLocation(place.name, place.addressComponents?.asList())
                            }
                        }
                    }

                    override fun onError(status: Status) {
                        Timber.e("An error occurred: $status")
                    }
        })
        binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                indoorChip.id -> autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
                outdoorChip.id -> autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
            }
        }

    }

    private fun selectDate() {
        c = Calendar.getInstance()
        viewModel.workoutLiveData.value?.date = c.time
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        binding.dateSelectionButton.text = dateFormat.format(c.time)

        datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month)
            viewModel.workoutLiveData.value?.date = c.time
            binding.dateSelectionButton.text = dateFormat.format(c.time)
        }, year, month, day)

        val todayDate = c.timeInMillis
        datePickerDialog.datePicker.minDate = todayDate
    }

    private fun selectHour() {
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        binding.hourSelectionButton.text = hourFormat.format(c.time)

        timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minutes)
            viewModel.workoutLiveData.value?.date = c.time
            binding.hourSelectionButton.text = hourFormat.format(c.time)
        }, hour, minute, AndroidDateFormat.is24HourFormat(activity))
    }

    private fun setDuration() {
        durationPickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
            binding.durationSelectionButton.text = when {
                hourOfDay == 0 && minutes != 0 -> "$minutes min"
                hourOfDay != 0 && minutes == 0 -> "$hourOfDay h"
                hourOfDay == 0 && minutes == 0 -> "15 min"
                else -> "$hourOfDay h $minutes min"
            }
        }, 0, 30, true)
    }

    private fun increment() {
        count++
        binding.participantsSelectionButton.text = "$count"
    }

    private fun decrement() {
        count--
        binding.participantsSelectionButton.text = "$count"
    }
}
