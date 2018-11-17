package com.example.fsudouest.blablafit;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private ActionBar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences preferences=getSharedPreferences("My prefs",0);
        boolean logged_in = preferences.getBoolean("logged_in",false);
        if(!logged_in) {
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

        toolbar = getSupportActionBar();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AccueilFragment())
                .commit();

        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.navigation_accueil:
                        AccueilFragment accueil = new AccueilFragment();
                        openFragment(accueil);
                        toolbar.setTitle(getString(R.string.app_name));
                        break;
                    case R.id.navigation_seances:
                        SeancesFragment seances = new SeancesFragment();
                        openFragment(seances);
                        toolbar.setTitle(item.getTitle());
                        break;
                    case R.id.navigation_messages:
                        MessagesFragment messages = new MessagesFragment();
                        openFragment(messages);
                        toolbar.setTitle(item.getTitle());
                        break;
                    case R.id.navigation_profil:
                        MyProfileFragment profil = new MyProfileFragment();
                        openFragment(profil);
                        toolbar.setTitle(item.getTitle());
                        break;
                }
                item.setChecked(true);


                return false;
            }
        });

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());


    }

    public void openFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void programmerSeance(View v){
        openFragment(new NouvelleSeanceFragment());
        toolbar.setTitle("Programmer ma séance");
    }

    public void TrouverSeance(View v){
        openFragment(new TrouverUneSeanceFragment());
        toolbar.setTitle("Trouver une séance");
    }

    public void EtapeSuivante(View v){
        openFragment(new NouvelleSeanceFragment());
        toolbar.setTitle("Programmer ma séance");
    }






}
