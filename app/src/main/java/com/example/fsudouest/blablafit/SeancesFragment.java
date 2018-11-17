package com.example.fsudouest.blablafit;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/seances.php?";

    public SeancesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seances, container, false);

        mEmptyStateTextView  = rootView.findViewById(R.id.empty_state_textView);
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

            fetchSeances(BASE_URL,client);

        } else {
            mList.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(getString(R.string.no_internet_connection));
        }

        return rootView;
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

}
