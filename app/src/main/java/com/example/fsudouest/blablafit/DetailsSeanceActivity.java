package com.example.fsudouest.blablafit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DetailsSeanceActivity extends AppCompatActivity {

    TextView title,location,duration,description,date,places,auteur;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_seance);

        title = findViewById(R.id.workout_title_textview);
        location = findViewById(R.id.workout_location_textview);
        duration = findViewById(R.id.workout_length_textview);
        description = findViewById(R.id.workout_description_textview);
        date = findViewById(R.id.workout_date_textview);
        places = findViewById(R.id.workout_participants_textview);
        auteur = findViewById(R.id.workout_author_textview);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Seance seance = (Seance) intent.getSerializableExtra("seance");

        SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yy à HH:mm", new Locale("fr","FR"));
        final String dateChaine = format.format(seance.getDate());

        title.setText("Type de séance : "+seance.getTitre());
        location.setText("Lieu : "+seance.getLieu());
        date.setText("Date : "+dateChaine);
        duration.setText("Durée : "+seance.getDuree());
        description.setText("Description : "+seance.getDescription());
        places.setText("Places restantes : "+seance.getNb_participants());
        auteur.setText(b.getString("auteur"));



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
}
