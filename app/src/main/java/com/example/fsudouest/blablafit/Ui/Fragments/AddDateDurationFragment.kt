package com.example.fsudouest.blablafit.Ui.Fragments


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentAddDateDurationBinding
import java.text.SimpleDateFormat
import java.util.*


class AddDateDurationFragment : Fragment() {

    private lateinit var dateSeance: Date
    private lateinit var date_button: Button
    private lateinit var hour_button: Button
    private lateinit var duration_button: Button
    private lateinit var c: Calendar
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog : TimePickerDialog
    private lateinit var durationPickerDialog : TimePickerDialog
    private val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.FRENCH)
    private val hourFormat = SimpleDateFormat("HH:mm", Locale.FRENCH)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDateDurationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_date_duration, container, false)

        //CHOIX DE LA DATE
        date_button = binding.dateSelectionButton

        selectDate()
        date_button.setOnClickListener {
            datePickerDialog.show()
        }

        //CHOIX DE L'HEURE
        hour_button = binding.hourSelectionButton

        selectHour()
        hour_button.setOnClickListener {
            timePickerDialog.show()
        }

        //Choix de la durée
        duration_button = binding.durationSelectionButton

        setDuration()
        duration_button.setOnClickListener {
            durationPickerDialog.show()
        }

        return binding.root
    }

    fun selectDate(){
        c = Calendar.getInstance()
        dateSeance = c.time
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        date_button.text = dateFormat.format(c.time)

        datePickerDialog = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month)
            dateSeance = c.time
            date_button.text = dateFormat.format(dateSeance)
        }, year, month, day)

        val todayDate = c.timeInMillis
        datePickerDialog.datePicker.minDate = todayDate
    }

    fun selectHour(){
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        hour_button.text = hourFormat.format(c.time)

        timePickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minutes)
            dateSeance = c.time
            hour_button.text = hourFormat.format(dateSeance)
        }, hour, minute, DateFormat.is24HourFormat(activity))
    }

    fun setDuration(){
        durationPickerDialog = TimePickerDialog(activity, TimePickerDialog.OnTimeSetListener{_, hourOfDay, minutes ->
            duration_button.text = when(hourOfDay){
                0 -> "$minutes min"
                else -> "$hourOfDay h $minutes min"
            }
        },0,30,true)
    }
}
