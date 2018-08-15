package com.example.fsudouest.blablafit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences=getSharedPreferences("My prefs",0);
        boolean logged_in = preferences.getBoolean("logged_in",false);
        if(logged_in) {
            startActivity(new Intent(this,MainActivity.class));
        }else{
            startActivity(new Intent(this,LoginActivity.class));
        }
        finish();
    }
}
