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


    public static String createNewSeance(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL
        String response = null;
        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Probleme lors de la création d'une nouvelle séance", e);
        }
        return response;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
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

    public static ArrayList<Seance> extractSeancesFromJson(String jsonResponse){
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<Seance> seances = new ArrayList<Seance>();

        try {

            // Create a JSONArray from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(jsonResponse);

            // For each seance in the seanceArray, create an {@link Earthquake} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                // Get a single seance at position i within the list of seances
                JSONObject currentSeance = baseJsonResponse.getJSONObject(i);

                String titre = currentSeance.getString("titre");
                String lieu = currentSeance.getString("lieu");
                String description = currentSeance.getString("description");
                String createur = currentSeance.getString("createur");
                String date = currentSeance.getString("date");
                String heure = currentSeance.getString("heure");
                String participants = currentSeance.getString("participants");
                String duree = currentSeance.getString("duree");

                date = date.substring(8)+"/"+date.substring(5,7)+"/"+date.substring(2,4);
                heure = heure.substring(0,5);
                // Create a new {@link Seance} object from the JSON response.
                seances.add(new Seance(titre,lieu,description,date,heure,participants,createur,duree)) ;
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the seances JSON results", e);
        }
        // Return the list of seances
        return seances;
    }

}
