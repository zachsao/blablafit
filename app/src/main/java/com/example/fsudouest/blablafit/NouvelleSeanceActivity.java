package com.example.fsudouest.blablafit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class NouvelleSeanceActivity extends AppCompatActivity {

    private String TAG = "NouvelleSeanceActivity";

    private Seance nouvelleSeance;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 25;
    private EditText picktime_et;
    PlaceAutocompleteFragment autocompleteFragment;

    public String titre;
    private String lieu;
    private String description;
    private Date dateSeance;
    private String nb_participants;
    private String duree;
    FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_seance);

        mDatabase = FirebaseFirestore.getInstance();

        final TextView tv_lieu = findViewById(R.id.textView_lieu);

        //CHOIX SEANCE EN SALLE OU EN EXTERIEUR
        RadioGroup radioGroup = findViewById(R.id.radio_group);
        checkByDefault(radioGroup, R.id.salle);

        autocompleteFragment = (PlaceAutocompleteFragment)
               getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Où allez vous faire votre séance ?");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                lieu = place.getName() +", "+place.getAddress().toString();
                tv_lieu.setText("Lieu: " +lieu);
                Log.i(TAG, "Place: " + place.getName());
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.salle:
                        autocompleteFragment.setHint("Rechercher une salle");
                        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                                .build();
                        autocompleteFragment.setFilter(typeFilter);
                        break;
                    case R.id.exterieur:
                        autocompleteFragment.setHint("Entrez une adresse");
                        AutocompleteFilter adresseFilter = new AutocompleteFilter.Builder()
                                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                                .build();
                        autocompleteFragment.setFilter(adresseFilter);
                        break;
                }
            }
        });

        //CHOIX DU TYPE DE SEANCE
        Spinner spinner_choix_seance = findViewById(R.id.choix_seance_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter_choix_seance = ArrayAdapter.createFromResource(this,
                R.array.tableau_seances, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter_choix_seance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_choix_seance.setAdapter(adapter_choix_seance);

        spinner_choix_seance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                titre = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //CHOIX DE LA DATE
        final EditText date = findViewById(R.id.date_et);

        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");

        dateSeance=c.getTime();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date.setText(dateFormat.format(c.getTime()));

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                c.set(year,month,day_of_month);
                dateSeance = c.getTime();
                date.setText(dateFormat.format(dateSeance));
            }
        }, year, month, day);

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) datePickerDialog.show();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        //CHOIX DE L'HEURE
        picktime_et = findViewById(R.id.et_heure_choisie);

        final SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        picktime_et.setText(hourFormat.format(c.getTime()));

        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE,minutes);
                dateSeance=c.getTime();
                picktime_et.setText(hourFormat.format(dateSeance));
            }
        }, hour, minute, DateFormat.is24HourFormat(this));

        picktime_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) timePickerDialog.show();
            }
        });

        picktime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });


        //CHOIX DE LA DUREE
        Spinner spinner_duree = (Spinner) findViewById(R.id.duree_spinner);
        final ArrayAdapter<CharSequence> adapter_duree = ArrayAdapter.createFromResource(this,
                R.array.tableau_durees, android.R.layout.simple_spinner_item);
        adapter_duree.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_duree.setAdapter(adapter_duree);
        spinner_duree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                duree = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //CHOIX DU NOMBRE MAXIMUM DE PARTICIPANTS
        Spinner spinner_places = (Spinner) findViewById(R.id.nbr_places_spinner);
        final ArrayAdapter<CharSequence> adapter_places = ArrayAdapter.createFromResource(this,
                R.array.tableau_nbr_places, android.R.layout.simple_spinner_item);
        adapter_places.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_places.setAdapter(adapter_places);
        spinner_places.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nb_participants = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String auteur = user.getEmail();
        final User author = new User(user.getDisplayName(),user.getEmail(),user.getPhotoUrl().toString());

        //CREATION DE LA SEANCE
        Button creer = findViewById(R.id.bouton_creer_seance);
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DocumentReference ref = mDatabase.collection("workouts").document();
                nouvelleSeance = new Seance(titre,lieu,description,dateSeance,nb_participants,auteur,duree,ref.getId());

                ref.set(nouvelleSeance).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //add a subcollection of users
                        ref.collection("users").document("auteur").set(author);
                        Toast.makeText(NouvelleSeanceActivity.this,"Nouvelle séance programmée",Toast.LENGTH_SHORT).show();
                        NavUtils.navigateUpFromSameTask(NouvelleSeanceActivity.this);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NouvelleSeanceActivity.this,"Une erreur s'est produite",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Séléctionne une séance en salle par défaut
    public void checkByDefault(RadioGroup rg,int id){
        if (rg.getCheckedRadioButtonId()==-1){
            rg.check(id);
        }
    }

}
