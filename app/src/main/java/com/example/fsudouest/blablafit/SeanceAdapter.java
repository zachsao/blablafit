package com.example.fsudouest.blablafit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SeanceAdapter extends RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder>{

    ArrayList<Seance> mData;
    Context mContext;


    public SeanceAdapter(Context context, ArrayList<Seance> data){
        mContext = context;
        mData = data;
    }


    @Override
    public SeanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        int layoutIdForListItem = R.layout.seance_item2;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        SeanceViewHolder viewHolder = new SeanceViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SeanceViewHolder holder, int position) {
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SeanceViewHolder extends RecyclerView.ViewHolder {

        TextView titre;
        TextView date;
        TextView heure;
        TextView duree;
        TextView description;
        TextView participants;
        TextView lieu;
        TextView createur;

        public SeanceViewHolder(View itemView){
            super(itemView);

            titre = itemView.findViewById(R.id.tv_titre);
            date = itemView.findViewById(R.id.tv_date);
            lieu = itemView.findViewById(R.id.tv_lieu);
            heure = itemView.findViewById(R.id.tv_heure);
            description = itemView.findViewById(R.id.tv_description);
            duree = itemView.findViewById(R.id.tv_durée);
            participants = itemView.findViewById(R.id.tv_nb_participants);
            createur = itemView.findViewById(R.id.tv_créateur);
        }


        public void bind(int position){
            titre.setText(mData.get(position).getTitre());
            date.setText(mData.get(position).getDate());
            heure.setText(mData.get(position).getHeure());
            description.setText(mData.get(position).getDescription());
            duree.setText(mData.get(position).getDuree());
            participants.setText("Places restantes: "+mData.get(position).getNb_participants());
            createur.setText("Créée par : "+mData.get(position).getCreateur());
            lieu.setText(mData.get(position).getLieu());
        }
    }
}
