package com.example.fsudouest.blablafit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private EditText mPseudoView;
    private EditText mNomView;
    private EditText mPrenomView;

    private String email;
    private String password;
    private String confirmPassword ;
    private String nom ;
    private String prenom ;
    private String pseudo ;

    private View mProgressView;
    private View mRegisterFormView;

    private TextView mEmptyStateTextView;

    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/register.php?";
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mRegisterFormView = findViewById(R.id.registerFormView);
        mProgressView = findViewById(R.id.register_progress);
        mEmptyStateTextView  = findViewById(R.id.empty_view);

        mEmailView = findViewById(R.id.mail_et);
        mNomView = findViewById(R.id.nom_edittext);
        mPrenomView = findViewById(R.id.prenom_et);
        mPseudoView = findViewById(R.id.pseudo_et);
        mPasswordView = findViewById(R.id.pwd_et);
        mConfirmPasswordView = findViewById(R.id.conf_pwd_et);

        Button confirmRegister = (Button) findViewById(R.id.confirm_register_button);
        confirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mNomView.setError(null);
        mPrenomView.setError(null);
        mPseudoView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the registration attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        confirmPassword = mConfirmPasswordView.getText().toString();
        nom = mNomView.getText().toString();
        prenom = mPrenomView.getText().toString();
        pseudo = mPseudoView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && password.length()<6) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //Check for same password, if entered
        if (!TextUtils.isEmpty(confirmPassword) && !passwordsMatch(password,confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_mismatch_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        //check for non empty pseudo
        if (TextUtils.isEmpty(pseudo)) {
            mPseudoView.setError(getString(R.string.error_field_required));
            focusView = mPseudoView;
            cancel = true;
        }

        //check for non empty last name
        if (TextUtils.isEmpty(nom)) {
            mNomView.setError(getString(R.string.error_field_required));
            focusView = mNomView;
            cancel = true;
        }

        //check for non empty first name
        if (TextUtils.isEmpty(prenom)) {
            mPrenomView.setError(getString(R.string.error_field_required));
            focusView = mPrenomView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // Get a reference to the ConnectivityManager to check state of network connectivity
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            // Get details on the currently active default data network
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            // If there is a network connection, fetch data
            if (networkInfo != null && networkInfo.isConnected()) {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                sendRegistrationData(BASE_URL,client);
            } else {
                mRegisterFormView.setVisibility(View.GONE);
                mEmptyStateTextView.setVisibility(View.VISIBLE);
                // Update empty state with no connection error message
                mEmptyStateTextView.setText("No internet connection!");
            }

        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean passwordsMatch(String pwd, String confirmation){
        return confirmation.equals(pwd);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void sendRegistrationData(String requestUrl, OkHttpClient client) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(requestUrl).newBuilder();
        urlBuilder.addQueryParameter("nom", nom);
        urlBuilder.addQueryParameter("prenom", prenom);
        urlBuilder.addQueryParameter("pseudo", pseudo);
        urlBuilder.addQueryParameter("pwd", password);
        urlBuilder.addQueryParameter("email", email);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Login Activity", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String request = response.body().string();
                final Boolean success = !request.contains("Duplicate entry");
                Log.i("Login Activity", request);
                RegisterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        if (success) {
                            saveUserData();
                            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                            finish();
                        } else {
                            mPseudoView.setError("Ce pseudo existe déjà, veuillez en choisir un autre");
                            mPseudoView.requestFocus();
                        }

                    }
                });

            }
        });
    }

    public void saveUserData(){
        SharedPreferences preferences=getSharedPreferences("My prefs",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("logged_in", true);
        editor.putString("nom",nom);
        editor.putString("prénom",prenom);
        editor.putString("email",email);
        editor.putString("pseudo",pseudo);
        editor.apply();
    }
}
