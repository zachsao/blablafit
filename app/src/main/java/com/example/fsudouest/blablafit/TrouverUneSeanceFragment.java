package com.example.fsudouest.blablafit;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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


public class TrouverUneSeanceFragment extends Fragment {


    private View rootView;


    private SeanceAdapter mAdapter;
    private RecyclerView mList;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private View mProgressView;
    private ArrayList<Seance> mySeances=new ArrayList<>();;
    private TextView mEmptyStateTextView;
    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/seances.php?";
    private ArrayList<Seance> filteredSeances = new ArrayList<>();
    SearchView searchView;


    FirebaseFirestore mDatabase;

    public TrouverUneSeanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trouver_une_seance, container, false);

        mDatabase = FirebaseFirestore.getInstance();

        mEmptyStateTextView  = rootView.findViewById(R.id.empty_state_textView);
        mProgressView = rootView.findViewById(R.id.seances_progress);
        mList = rootView.findViewById(R.id.rv_search_seances);

        OkHttpClient client = new OkHttpClient();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Show a progress spinner, and kick off a background task
            showProgress(true);
            getSeances();
            //fetchSeances(BASE_URL,client);

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

    public void search(String query){
        for(Seance seance : mySeances){
            if(seance.getTitre().toLowerCase().contains(query)){
                filteredSeances.add(seance);
            }
        }
        mList.setLayoutManager(layoutManager);
        mAdapter = new SeanceAdapter(getActivity(),filteredSeances);

        mList.setAdapter(mAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.seance_filters, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager)
                getActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getActivity().getComponentName()));
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()>0){
                    filteredSeances.clear();
                    search(newText);
                }
                else{
                    filteredSeances.clear();
                    mList.setLayoutManager(layoutManager);
                    mAdapter = new SeanceAdapter(getActivity(),mySeances);

                    mList.setAdapter(mAdapter);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getActivity(),"déployer les filtres",Toast.LENGTH_SHORT).show();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

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
                //Log.i("SeancesFragment", jsonResponse);
                // Run view-related code back on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgress(false);
                        if (jsonResponse.length() == 0) {
                            mList.setVisibility(View.GONE);
                            mEmptyStateTextView.setVisibility(View.VISIBLE);
                            // Update empty state with no connection error message
                            mEmptyStateTextView.setText(getString(R.string.no_seance_available));
                        } else {
                            ArrayList<Seance> seances = QueryUtils.extractSeancesFromJson(jsonResponse);
                            mySeances = seances;
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
        ref.get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showProgress(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Seances Fragment", document.getId() + " => " + document.getData());
                                mySeances.add(document.toObject(Seance.class));
                            }
                            mAdapter = new SeanceAdapter(getActivity(), mySeances);
                            mList.setAdapter(mAdapter);
                            mList.setLayoutManager(layoutManager);
                        } else {
                            Log.d("Seances Fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
