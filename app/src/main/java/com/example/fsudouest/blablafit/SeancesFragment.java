package com.example.fsudouest.blablafit;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeancesFragment extends Fragment {

    private View rootView;

    private SeanceAdapter mAdapter;
    private RecyclerView mList;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private TextView mEmptyStateTextView;
    private View mProgressView;
    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/seances.php?";

    FirebaseFirestore mDatabase;

    ArrayList<Seance> seances = new ArrayList<Seance>();
    public SeancesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seances, container, false);

        mDatabase = FirebaseFirestore.getInstance();

        mEmptyStateTextView  = rootView.findViewById(R.id.empty_state_textView);
        mProgressView = rootView.findViewById(R.id.seances_progress);
        mList = rootView.findViewById(R.id.rv_seances);

        OkHttpClient client = new OkHttpClient();
        String createur = getActivity().getSharedPreferences("My prefs",0).getString("pseudo",null);
        BASE_URL+="createur="+createur;

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Show a progress spinner, and kick off a background task
            showProgress(true);
            //fetchSeances(BASE_URL,client);
            getSeances();
        } else {
            mList.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));
        }

        return rootView;
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mList.setVisibility(show ? View.GONE : View.VISIBLE);
            mList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mList.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void fetchSeances(String requestUrl, OkHttpClient client) {
        Request myGetRequest = new Request.Builder()
                .url(requestUrl)
                .build();

        client.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("SeancesFragment", e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        mList.setVisibility(View.GONE);
                        mEmptyStateTextView.setVisibility(View.VISIBLE);
                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(getString(R.string.server_error));
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonResponse = response.body().string();
                Log.i("SeancesFragment", response.message());
                // Run view-related code back on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        if (jsonResponse.length()==0){
                            mList.setVisibility(View.GONE);
                            mEmptyStateTextView.setVisibility(View.VISIBLE);
                            // Update empty state with no connection error message
                            mEmptyStateTextView.setText(getString(R.string.no_seance_available));
                        }else{
                            ArrayList<Seance> seances = QueryUtils.extractSeancesFromJson(jsonResponse);
                            mAdapter = new SeanceAdapter(getActivity(), seances);
                            mList.setAdapter(mAdapter);
                            mList.setLayoutManager(layoutManager);
                        }
                    }
                });

            }
        });
    }

    public void getSeances(){
        CollectionReference ref = mDatabase.collection("workouts");

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.SERVER;

        // Get the document, forcing the SDK to use the offline cache
        ref.whereEqualTo("createur", "zakaria.sao@gmail.com")
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showProgress(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Seances Fragment", document.getId() + " => " + document.getData());
                                seances.add(document.toObject(Seance.class));
                            }
                            mAdapter = new SeanceAdapter(getActivity(), seances);
                            mList.setAdapter(mAdapter);
                            mList.setLayoutManager(layoutManager);
                        } else {
                            Log.d("Seances Fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
