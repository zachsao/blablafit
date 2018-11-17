package com.example.fsudouest.blablafit;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccueilFragment extends Fragment {


    public AccueilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =   inflater.inflate(R.layout.fragment_accueil, container, false);

        Button nouvelle_seance = rootView.findViewById(R.id.programmer_button);
        Button trouver_seance = rootView.findViewById(R.id.button_trouver);
        nouvelle_seance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accueilFragment_to_nouvelleSeanceFragment);
            }
        });

        trouver_seance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_accueilFragment_to_trouverUneSeanceFragment);
            }
        });

        return rootView;

    }



}
