package com.example.fsudouest.blablafit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupWithNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;


public class MainActivity extends AppCompatActivity{

    private ActionBar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavController navController = findNavController(this, R.id.myNavHostFragment);

        SharedPreferences preferences=getSharedPreferences("My prefs",0);
        boolean logged_in = preferences.getBoolean("logged_in",false);
        if(!logged_in) {
            startActivity(new Intent(this,ChooseLoginActivity.class));
            finish();
        }

        //toolbar = getSupportActionBar();
        setupActionBarWithNavController(this,navController);

        final BottomNavigationView navigation =findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        setupWithNavController(navigation,navController);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

    }


    @Override
    public boolean onSupportNavigateUp() {
        return findNavController(this, R.id.myNavHostFragment).navigateUp();
    }
}
