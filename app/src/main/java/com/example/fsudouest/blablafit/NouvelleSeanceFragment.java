package com.example.fsudouest.blablafit;


import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.LoaderManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NouvelleSeanceFragment extends Fragment {

    private NewSeanceTask mAuthTask = null;

    public String titre;
    private String lieu;
    private String description;
    private String dateSeance;
    private String heure;
    private String nb_participants;
    private String createur;
    private String duree;

    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/nouvelleSeance.php?";

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 25;

    /** TextView that is displayed when an error occured */
    private TextView mEmptyStateTextView;

    private LinearLayout layout;
    private EditText picktime_et;

    View rootView;

    public NouvelleSeanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_nouvelle_seance, container, false);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        layout = rootView.findViewById(R.id.linearLayout);

        final TextView tv_lieu = rootView.findViewById(R.id.textView_lieu);

        //CHOIX SEANCE EN SALLE OU EN EXTERIEUR
        RadioGroup radioGroup = rootView.findViewById(R.id.radio_group);
        checkByDefault(radioGroup, R.id.salle);

        final PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setHint("Où allez vous faire votre séance ?");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                lieu = place.getName().toString();
                tv_lieu.setText(lieu);
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
        Spinner spinner_choix_seance = (Spinner) rootView.findViewById(R.id.choix_seance_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        final ArrayAdapter<CharSequence> adapter_choix_seance = ArrayAdapter.createFromResource(getActivity(),
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
        final EditText date = rootView.findViewById(R.id.date_et);
        
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        date.setText(day+"/"+(month+1)+"/"+year);
        dateSeance = year+"-"+(month+1)+"-"+day;
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day_of_month) {
                date.setText(day_of_month+"/"+(month+1)+"/"+year);
                dateSeance = year+"-"+(month+1)+"-"+day_of_month;
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
         picktime_et = rootView.findViewById(R.id.et_heure_choisie);


        int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        picktime_et.setText(hour+":"+minute);
        heure = picktime_et.getText().toString();
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                String min = minutes < 10 ? "0"+minutes : ""+minutes;
                picktime_et.setText(hourOfDay+":"+min);
                heure = picktime_et.getText().toString();
            }
        }, hour, minute, DateFormat.is24HourFormat(getActivity()));

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
        Spinner spinner_duree = (Spinner) rootView.findViewById(R.id.duree_spinner);
        final ArrayAdapter<CharSequence> adapter_duree = ArrayAdapter.createFromResource(getActivity(),
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
        Spinner spinner_places = (Spinner) rootView.findViewById(R.id.nbr_places_spinner);
        final ArrayAdapter<CharSequence> adapter_places = ArrayAdapter.createFromResource(getActivity(),
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


        SharedPreferences prefs = getActivity().getSharedPreferences("My prefs",0);
        createur = prefs.getString("pseudo",null);
        //CREATION DE LA SEANCE
        Button creer = rootView.findViewById(R.id.bouton_creer_seance);
        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get a reference to the ConnectivityManager to check state of network connectivity
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().
                        getSystemService(Context.CONNECTIVITY_SERVICE);

                // Get details on the currently active default data network
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                // If there is a network connection, fetch data
                if (networkInfo != null && networkInfo.isConnected()) {
                    mAuthTask = new NewSeanceTask();
                    mAuthTask.execute((Void) null);
                } else {
                    // Otherwise, display error
                    // First, hide loading indicator so error message will be visible
                    //View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
                    //loadingIndicator.setVisibility(View.GONE);

                    // Update empty state with no connection error message
                    layout.setVisibility(View.GONE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText(R.string.no_internet_connection);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //Séléctionne une séance en salle par défaut
    public void checkByDefault(RadioGroup rg,int id){
        if (rg.getCheckedRadioButtonId()==-1){
            rg.check(id);
        }
    }




    public class NewSeanceTask extends AsyncTask<Void, Void, String> {


        private String mUrl;

        private NewSeanceTask() {
            mUrl = BASE_URL + "titre="+titre +"&description="+description+"&lieu="+lieu+"&date="+dateSeance+"&heure="+heure
                    +"&participants="+nb_participants+"&duree="+duree+"&createur="+createur;

        }

        @Override
        protected String doInBackground(Void... params) {
            String seance = QueryUtils.createNewSeance(mUrl);
            return seance;
        }

        @Override
        protected void onPostExecute(final String seance) {
            mAuthTask = null;
            Toast.makeText(getActivity(),seance,Toast.LENGTH_LONG).show();
            if (seance.equals("1")) {
                //SharedPreferences preferences=getActivity().getSharedPreferences("My prefs",0);
                //SharedPreferences.Editor editor = preferences.edit();

                startActivity(new Intent(getActivity(),SeanceValideeActivity.class));
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(),
                        seance,
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }



}

