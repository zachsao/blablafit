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

import java.util.ArrayList;


public class TrouverUneSeanceFragment extends Fragment {


    private View rootView;


    private SeanceAdapter mAdapter;
    private RecyclerView mList;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
    private View mProgressView;
    private ArrayList<Seance> mySeances=new ArrayList<>();;
    private TextView mEmptyStateTextView;
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
                                mySeances.add(document.toObject(Seance.class));
                            }

                            if(mySeances.isEmpty()){
                                mList.setVisibility(View.GONE);
                                mEmptyStateTextView.setVisibility(View.VISIBLE);
                                mEmptyStateTextView.setText(getString(R.string.no_seance_available));
                            }else{
                                mAdapter = new SeanceAdapter(getActivity(), mySeances);
                                mList.setAdapter(mAdapter);
                                mList.setLayoutManager(layoutManager);
                            }

                        } else {
                            mList.setVisibility(View.GONE);
                            mEmptyStateTextView.setVisibility(View.VISIBLE);
                            // Update empty state with no connection error message
                            mEmptyStateTextView.setText(getString(R.string.server_error));
                            Log.d("Seances Fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
