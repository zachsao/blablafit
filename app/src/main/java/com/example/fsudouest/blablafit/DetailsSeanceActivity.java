package com.example.fsudouest.blablafit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;


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


        title.setText("Type de séance : "+b.getString("titre"));
        location.setText("Lieu : "+b.getString("lieu"));
        date.setText("Date : "+b.getString("date"));
        duration.setText("Durée : "+b.getString("durée"));
        description.setText("Description : "+b.getString("description"));
        places.setText("Places restantes : "+b.getString("places"));
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
