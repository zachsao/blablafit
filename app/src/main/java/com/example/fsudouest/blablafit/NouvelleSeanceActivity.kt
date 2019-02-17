package com.example.fsudouest.blablafit

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
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast

import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class NouvelleSeanceActivity : AppCompatActivity() {

    private val TAG = "NouvelleSeanceActivity"

    private var nouvelleSeance: Seance? = null
    private var picktime_et: EditText? = null
    private lateinit var autocompleteFragment: PlaceAutocompleteFragment

    lateinit var titre: String
    private var lieu: String? = null
    private val description: String? = null
    private var dateSeance: Date? = null
    private var nb_participants: String? = null
    private var duree: String? = null
    lateinit var mDatabase: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nouvelle_seance)

        mDatabase = FirebaseFirestore.getInstance()

        val tv_lieu = findViewById<TextView>(R.id.textView_lieu)

        //CHOIX SEANCE EN SALLE OU EN EXTERIEUR
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)
        checkByDefault(radioGroup, R.id.salle)

        autocompleteFragment = fragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        autocompleteFragment.setHint("Où allez vous faire votre séance ?")
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                lieu = place.name.toString() + ", " + place.address!!.toString()
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
                    val typeFilter = AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                            .build()
                    autocompleteFragment.setFilter(typeFilter)
                }
                R.id.exterieur -> {
                    autocompleteFragment.setHint("Entrez une adresse")
                    val adresseFilter = AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build()
                    autocompleteFragment.setFilter(adresseFilter)
                }
            }
        }

        //CHOIX DU TYPE DE SEANCE
        val spinner_choix_seance = findViewById<Spinner>(R.id.choix_seance_spinner)
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
        val date = findViewById<EditText>(R.id.date_et)

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
        picktime_et = findViewById(R.id.et_heure_choisie)

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
        val spinner_duree = findViewById<View>(R.id.duree_spinner) as Spinner
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
        val spinner_places = findViewById<View>(R.id.nbr_places_spinner) as Spinner
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
        val creer = findViewById<Button>(R.id.bouton_creer_seance)
        creer.setOnClickListener {
            val ref = mDatabase.collection("workouts").document()
            nouvelleSeance = Seance(titre, lieu!!, description!!, dateSeance!!, nb_participants!!, auteur!!, duree!!, ref.id)

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

    companion object {
        private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 25
    }

}
