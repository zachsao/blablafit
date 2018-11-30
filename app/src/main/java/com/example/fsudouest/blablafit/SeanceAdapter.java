package com.example.fsudouest.blablafit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.Distribution;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.navigation.Navigation;

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
    public void onBindViewHolder(@NonNull SeanceViewHolder holder, final int position) {
        holder.bind(position);

        SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yy à HH:mm", new Locale("fr","FR"));
        final String dateChaine = format.format(mData.get(position).getDate());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,mData.get(position).getId(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,DetailsSeanceActivity.class);
                /*intent.putExtra("titre",mData.get(position).getTitre());
                intent.putExtra("lieu",mData.get(position).getLieu());
                intent.putExtra("date",dateChaine);
                intent.putExtra("durée",mData.get(position).getDuree());
                intent.putExtra("places",mData.get(position).getNb_participants());
                intent.putExtra("auteur",mData.get(position).getCreateur());
                intent.putExtra("description",mData.get(position).getDescription());*/
                intent.putExtra("seance",mData.get(position));
                mContext.startActivity(intent);
            }
        });

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

        LinearLayout parent;

        public SeanceViewHolder(View itemView){
            super(itemView);

            parent = itemView.findViewById(R.id.parentLayout);
            titre = itemView.findViewById(R.id.tv_titre);
            date = itemView.findViewById(R.id.tv_date);
            lieu = itemView.findViewById(R.id.tv_lieu);
            heure = itemView.findViewById(R.id.tv_heure);
            duree = itemView.findViewById(R.id.tv_durée);
            participants = itemView.findViewById(R.id.tv_nb_participants);
            createur = itemView.findViewById(R.id.tv_créateur);
        }


        public void bind(int position){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

            titre.setText(mData.get(position).getTitre());
            date.setText(dateFormat.format(mData.get(position).getDate()));
            heure.setText(hourFormat.format(mData.get(position).getDate()));
            duree.setText(mData.get(position).getDuree());
            participants.setText("Places restantes: "+mData.get(position).getNb_participants());
            createur.setText("Créée par : "+mData.get(position).getCreateur());
            lieu.setText(mData.get(position).getLieu());
        }
    }
}
