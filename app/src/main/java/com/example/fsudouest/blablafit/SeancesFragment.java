package com.example.fsudouest.blablafit;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;


import java.util.ArrayList;


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

    FirebaseFirestore mDatabase;
    FirebaseUser user;

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
        user = FirebaseAuth.getInstance().getCurrentUser();

        mEmptyStateTextView  = rootView.findViewById(R.id.empty_state_textView);
        mProgressView = rootView.findViewById(R.id.seances_progress);
        mList = rootView.findViewById(R.id.rv_seances);


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


    public void getSeances(){
        CollectionReference ref = mDatabase.collection("workouts");

        // Source can be CACHE, SERVER, or DEFAULT.
        Source source = Source.SERVER;

        // Get the document, forcing the SDK to use the offline cache
        ref.whereEqualTo("createur", user.getEmail())
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            showProgress(false);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                seances.add(document.toObject(Seance.class));
                            }

                            if (seances.isEmpty()){
                                mList.setVisibility(View.GONE);
                                mEmptyStateTextView.setVisibility(View.VISIBLE);
                                // Update empty state with no connection error message
                                mEmptyStateTextView.setText(getString(R.string.no_seance_available));
                            }else{
                                mAdapter = new SeanceAdapter(getActivity(), seances);
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
