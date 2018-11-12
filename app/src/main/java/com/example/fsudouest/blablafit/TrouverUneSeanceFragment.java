package com.example.fsudouest.blablafit;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TrouverUneSeanceFragment extends Fragment {


    private View rootView;

    private final int NUMBER_OF_ITEMS = 6;

    private SeanceAdapter mAdapter;
    private RecyclerView mList;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

    private List<Seance> seances = new ArrayList<>();
    private List<Seance> filteredSeances = new ArrayList<>();
    SearchView searchView;

    public TrouverUneSeanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_trouver_une_seance, container, false);

        mList = rootView.findViewById(R.id.rv_search_seances);
        mList.setLayoutManager(layoutManager);

        //mList.setHasFixedSize(true);


        seances.add(new Seance("FullBody","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("Jambes","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("Abdos","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("HalfBody","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("FullBody","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("Pecs","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("FullBody","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("Dos-Bi","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("FullBody","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("Bras","BasicFit","","21/10","15h00",3,"Vous",2));
        seances.add(new Seance("FullBody","BasicFit","","21/10","15h00",3,"Vous",2));

        mAdapter = new SeanceAdapter(getActivity(),seances);

        mList.setAdapter(mAdapter);



        return rootView;
    }

    public void search(String query){
        for(Seance seance : seances){
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
                    mAdapter = new SeanceAdapter(getActivity(),seances);

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
}
