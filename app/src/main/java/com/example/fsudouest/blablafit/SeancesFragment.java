package com.example.fsudouest.blablafit;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private final int NUMBER_OF_ITEMS = 6;

    private SeanceAdapter mAdapter;
    private RecyclerView mList;

    private List<Seance> seances = new ArrayList<>();
    private static ArrayList<Seance> mySeances = new ArrayList<>();

    private String BASE_URL = "https://zakariasao.000webhostapp.com/blablafit/seances.php?";

    public SeancesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_seances, container, false);


        mList = rootView.findViewById(R.id.rv_seances);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(layoutManager);

        mList.setHasFixedSize(true);


        OkHttpClient client = new OkHttpClient();
        String createur = getActivity().getSharedPreferences("My prefs",0).getString("pseudo",null);
        BASE_URL+="createur="+createur;
        mAdapter = new SeanceAdapter(getActivity(),fetchSeances(BASE_URL,client));

        mList.setAdapter(mAdapter);


        return rootView;
    }

    public static ArrayList<Seance> fetchSeances(String requestUrl, OkHttpClient client) {

        Request myGetRequest = new Request.Builder()
                .url(requestUrl)
                .build();

        client.newCall(myGetRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("SeancesFragment", e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonResponse = response.body().string();
                Log.i("SeancesFragment", jsonResponse);
                mySeances = QueryUtils.extractSeancesFromJson(jsonResponse);
            }
        });
        return mySeances;
    }

}
