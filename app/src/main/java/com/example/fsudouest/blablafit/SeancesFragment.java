package com.example.fsudouest.blablafit;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeancesFragment extends Fragment {

    private View rootView;

    private final int NUMBER_OF_ITEMS = 6;

    private SeanceAdapter mAdapter;
    private RecyclerView mList;

    private List<Seance> seances = new ArrayList<>();

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

        for(int i = 0; i<=10; i++){
            seances.add(new Seance("Jambes","FitnessPark","rien de spé t'a vu","21/10","15h00",3,"Vous",2));
        }
        mAdapter = new SeanceAdapter(getActivity(),seances);

        mList.setAdapter(mAdapter);


        return rootView;
    }

}
