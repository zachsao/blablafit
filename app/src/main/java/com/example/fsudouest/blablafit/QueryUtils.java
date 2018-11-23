package com.example.fsudouest.blablafit;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    public static User extractUserFromJson(String jsonResponse){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        User user=null;

        try {

            // Create a JSONArray from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(jsonResponse);

            // Get a single earthquake at position i within the list of earthquakes
            JSONObject currentUser = baseJsonResponse.getJSONObject(0);

            String nom = currentUser.getString("nom");
            String prenom = currentUser.getString("prenom");
            String pseudo = currentUser.getString("pseudo");
            String email = currentUser.getString("email");
            String mdp = currentUser.getString("mot_de_passe");
            int note = currentUser.getInt("note");

            // Create a new {@link User} object from the JSON response.
            user = new User(nom,prenom,email,pseudo,mdp,note);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the user JSON results", e);
        }

        // Return the user
        return user;
    }



}
