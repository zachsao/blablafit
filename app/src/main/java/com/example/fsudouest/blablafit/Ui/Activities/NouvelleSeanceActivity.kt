package com.example.fsudouest.blablafit.Ui.Activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.core.app.NavUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.fsudouest.blablafit.BuildConfig
import com.example.fsudouest.blablafit.model.Seance
import com.example.fsudouest.blablafit.model.User
import com.example.fsudouest.blablafit.R
import com.example.fsudouest.blablafit.databinding.ActivityNouvelleSeanceBinding

import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.android.AndroidInjection

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class NouvelleSeanceActivity : AppCompatActivity() {

    private val TAG = "NouvelleSeanceActivity"

    private var nouvelleSeance: Seance? = null
    private var picktime_et: EditText? = null
    private lateinit var autocompleteFragment: AutocompleteSupportFragment

    lateinit var titre: String
    private lateinit var lieu: String
    private val description: String? = null
    private lateinit var dateSeance: Date
    private lateinit var nb_participants: String
    private lateinit var duree: String

    @Inject
    lateinit var mDatabase: FirebaseFirestore

    lateinit var binding: ActivityNouvelleSeanceBinding

    private val apiKey = BuildConfig.GOOGLE_PLACES_KEY

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_nouvelle_seance)

        // Initialize Places.
        Places.initialize(applicationContext, apiKey)

        val tv_lieu = binding.textViewLieu



        //CHOIX SEANCE EN SALLE OU EN EXTERIEUR
        val radioGroup = binding.radioGroup
        checkByDefault(radioGroup, R.id.salle)

        autocompleteFragment = supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.ID))
        autocompleteFragment.setHint("Où allez vous faire votre séance ?")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lieu = place.name.toString() + ", " + place.address.toString()
                tv_lieu.text = "Lieu: " + lieu!!
                Log.i(TAG, "Place: " + place.name)
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })


        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.salle -> {
                    autocompleteFragment.setHint("Rechercher une salle")
                    autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT)
                }
                R.id.exterieur -> {
                    autocompleteFragment.setHint("Entrez une adresse")
                    autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS)
                }
            }
        }

        //CHOIX DU TYPE DE SEANCE
        val spinner_choix_seance = binding.choixSeanceSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter_choix_seance = ArrayAdapter.createFromResource(this,
                R.array.tableau_seances, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter_choix_seance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner_choix_seance.adapter = adapter_choix_seance

        spinner_choix_seance.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                titre = adapterView.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        //CHOIX DE LA DATE
        val date = binding.dateEt

        val c = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yy")

        dateSeance = c.time
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)
        date.setText(dateFormat.format(c.time))

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, year, month, day_of_month ->
            c.set(year, month, day_of_month)
            dateSeance = c.time
            date.setText(dateFormat.format(dateSeance))
        }, year, month, day)

        date.onFocusChangeListener = View.OnFocusChangeListener { view, b -> if (b) datePickerDialog.show() }

        date.setOnClickListener { datePickerDialog.show() }

        //CHOIX DE L'HEURE
        picktime_et = binding.etHeureChoisie

        val hourFormat = SimpleDateFormat("HH:mm")

        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        picktime_et!!.setText(hourFormat.format(c.time))

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minutes ->
            c.set(Calendar.HOUR_OF_DAY, hourOfDay)
            c.set(Calendar.MINUTE, minutes)
            dateSeance = c.time
            picktime_et!!.setText(hourFormat.format(dateSeance))
        }, hour, minute, DateFormat.is24HourFormat(this))

        picktime_et!!.onFocusChangeListener = View.OnFocusChangeListener { view, b -> if (b) timePickerDialog.show() }

        picktime_et!!.setOnClickListener { timePickerDialog.show() }


        //CHOIX DE LA DUREE
        val spinner_duree = binding.dureeSpinner
        val adapter_duree = ArrayAdapter.createFromResource(this,
                R.array.tableau_durees, android.R.layout.simple_spinner_item)
        adapter_duree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_duree.adapter = adapter_duree
        spinner_duree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                duree = adapterView.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }

        //CHOIX DU NOMBRE MAXIMUM DE PARTICIPANTS
        val spinner_places = binding.nbrPlacesSpinner
        val adapter_places = ArrayAdapter.createFromResource(this,
                R.array.tableau_nbr_places, android.R.layout.simple_spinner_item)
        adapter_places.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_places.adapter = adapter_places
        spinner_places.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                nb_participants = adapterView.getItemAtPosition(i).toString()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }


        val user = FirebaseAuth.getInstance().currentUser
        val auteur = user!!.email
        val author = User(user.displayName!!, user.email!!, if (user.photoUrl != null) user.photoUrl.toString() else "")

        //CREATION DE LA SEANCE
        val creer = binding.boutonCreerSeance
        creer.setOnClickListener {
            val ref = mDatabase.collection("workouts").document()
            nouvelleSeance = Seance(titre, lieu, description, dateSeance, nb_participants, auteur!!, duree, ref.id)

            ref.set(nouvelleSeance!!).addOnSuccessListener {
                //add a subcollection of users
                ref.collection("users").document("auteur").set(author)
                Toast.makeText(this, "Nouvelle séance programmée", Toast.LENGTH_SHORT).show()
                NavUtils.navigateUpFromSameTask(this)
            }.addOnFailureListener { Toast.makeText(this, "Une erreur s'est produite", Toast.LENGTH_SHORT).show() }
        }

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Séléctionne une séance en salle par défaut
    fun checkByDefault(rg: RadioGroup, id: Int) {
        if (rg.checkedRadioButtonId == -1) {
            rg.check(id)
        }
    }

}
