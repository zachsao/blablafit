package com.example.fsudouest.blablafit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import static androidx.navigation.Navigation.findNavController;
import static androidx.navigation.ui.NavigationUI.setupWithNavController;
import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;


public class MainActivity extends AppCompatActivity{


    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();

        /*SharedPreferences preferences=getSharedPreferences("My prefs",0);
        boolean logged_in = preferences.getBoolean("logged_in",false);
        if(!logged_in) {
            startActivity(new Intent(this,ChooseLoginActivity.class));
            finish();
        }*/

        NavController navController = findNavController(this, R.id.myNavHostFragment);
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

    @Override
    protected void onStart() {
        if(user==null){
            //if no one is signed in, start login activity
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }
        super.onStart();
    }








    private void saveUserData(FirebaseUser user) {
        SharedPreferences preferences = getSharedPreferences("My prefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("logged_in", true);
        editor.putString("nom",user.getDisplayName());
        editor.putString("email",user.getEmail());
        editor.apply();
    }
}
