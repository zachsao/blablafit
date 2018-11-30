package com.example.fsudouest.blablafit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailsSeanceActivity extends AppCompatActivity {

    TextView title,location,duration,description,date,places,auteur;
    CircleImageView photo;

    FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_seance);

        mDatabase = FirebaseFirestore.getInstance();

        title = findViewById(R.id.workout_title_textview);
        location = findViewById(R.id.workout_location_textview);
        duration = findViewById(R.id.workout_length_textview);
        description = findViewById(R.id.workout_description_textview);
        date = findViewById(R.id.workout_date_textview);
        places = findViewById(R.id.workout_participants_textview);
        auteur = findViewById(R.id.workout_author_textview);
        photo = findViewById(R.id.imageView3);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        Seance seance = (Seance) intent.getSerializableExtra("seance");

        SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yy à HH:mm", new Locale("fr","FR"));
        String dateChaine = format.format(seance.getDate());

        title.setText("Type de séance : "+seance.getTitre());
        location.setText("Lieu : "+seance.getLieu());
        date.setText("Date : "+dateChaine);
        duration.setText("Durée : "+seance.getDuree());
        description.setText("Description : "+seance.getDescription());
        places.setText("Places restantes : "+seance.getNb_participants());
        auteur.setText(seance.getCreateur());

        DocumentReference auteurRef = mDatabase.collection("workouts")
                .document(seance.getId()).collection("users").document("auteur");
        auteurRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    User user = task.getResult().toObject(User.class);
                    Glide.with(DetailsSeanceActivity.this).load(user.getPhotoUrl()).into(photo);
                }
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
}
