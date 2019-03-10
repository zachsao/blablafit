package com.example.fsudouest.blablafit.Ui.Fragments


import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.FragmentAddDateDurationBinding
import java.text.SimpleDateFormat
import java.util.*


class AddDateDurationFragment : Fragment() {

    private lateinit var dateSeance: Date

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDateDurationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_date_duration, container, false)

        //CHOIX DE LA DATE
        val date_button = binding.dateSelectionButton

        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE dd MMM", Locale.FRENCH)
        val todaysDate = c.timeInMillis

        dateSeance = c.time
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        date_button.text = dateFormat.format(c.time)

        val datePickerDialog = DatePickerDialog(activity!!, DatePickerDialog.OnDateSetListener { _, year, month, day_of_month ->
            c.set(year, month, day_of_month)
            dateSeance = c.time
            date_button.text = dateFormat.format(dateSeance)
        }, year, month, day)

        datePickerDialog.datePicker.minDate = todaysDate

        date_button.setOnClickListener {
            datePickerDialog.show()
        }


        return binding.root
    }


}
