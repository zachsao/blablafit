package com.example.fsudouest.blablafit;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new AccueilFragment())
                .commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
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



    }

    public void openFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
