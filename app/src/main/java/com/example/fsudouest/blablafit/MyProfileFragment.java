package com.example.fsudouest.blablafit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    SharedPreferences preferences;
    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_my_profile, container, false);

        preferences = getActivity().getSharedPreferences("My prefs",0);

        FirebaseUser user =FirebaseAuth.getInstance().getCurrentUser();

        TextView pseudo = rootview.findViewById(R.id.pseudo);
        TextView nom = rootview.findViewById(R.id.nom);
        TextView prenom = rootview.findViewById(R.id.prénom);
        TextView email = rootview.findViewById(R.id.email);

        pseudo.setText(user.getDisplayName());
        nom.setText("Nom: "+ user.getDisplayName());
        //prenom.setText("Prénom: "+preferences.getString("prénom",null));
        email.setText("Email: "+user.getEmail());

        Button deconnexion = rootview.findViewById(R.id.deco_button);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        return rootview;
    }

    private void signOut() {
        AuthUI.getInstance().signOut(getActivity());
    }

}
