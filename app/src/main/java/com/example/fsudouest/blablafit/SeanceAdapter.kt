package com.example.fsudouest.blablafit

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class SeanceAdapter(internal var mContext: Context, internal var mData: ArrayList<Seance>) : RecyclerView.Adapter<SeanceAdapter.SeanceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeanceViewHolder {

        val context = parent.context

        val layoutIdForListItem = R.layout.seance_item2
        val inflater = LayoutInflater.from(context)
        val shouldAttachToParentImmediately = false

        val view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately)
        val viewHolder = SeanceViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: SeanceViewHolder, position: Int) {
        holder.bind(position)

        val format = SimpleDateFormat("EEE d MMM yy à HH:mm", Locale("fr", "FR"))
        val dateChaine = format.format(mData[position].date)

        holder.parent.setOnClickListener {
            //Toast.makeText(mContext,mData.get(position).getId(),Toast.LENGTH_SHORT).show();
            val intent = Intent(mContext, DetailsSeanceActivity::class.java)
            /*intent.putExtra("titre",mData.get(position).getTitre());
                intent.putExtra("lieu",mData.get(position).getLieu());
                intent.putExtra("date",dateChaine);
                intent.putExtra("durée",mData.get(position).getDuree());
                intent.putExtra("places",mData.get(position).getNb_participants());
                intent.putExtra("auteur",mData.get(position).getCreateur());
                intent.putExtra("description",mData.get(position).getDescription());*/
            intent.putExtra("seance", mData[position])
            mContext.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return mData.size
    }

    inner class SeanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var titre: TextView
        internal var date: TextView
        internal var heure: TextView
        internal var duree: TextView
        internal var description: TextView? = null
        internal var participants: TextView
        internal var lieu: TextView
        internal var createur: TextView

        internal var parent: LinearLayout

        init {

            parent = itemView.findViewById(R.id.parentLayout)
            titre = itemView.findViewById(R.id.tv_titre)
            date = itemView.findViewById(R.id.tv_date)
            lieu = itemView.findViewById(R.id.tv_lieu)
            heure = itemView.findViewById(R.id.tv_heure)
            duree = itemView.findViewById(R.id.tv_durée)
            participants = itemView.findViewById(R.id.tv_nb_participants)
            createur = itemView.findViewById(R.id.tv_créateur)
        }


        fun bind(position: Int) {
            val dateFormat = SimpleDateFormat("dd/MM/yy")
            val hourFormat = SimpleDateFormat("HH:mm")

            titre.text = mData[position].titre
            date.text = dateFormat.format(mData[position].date)
            heure.text = hourFormat.format(mData[position].date)
            duree.text = mData[position].duree
            participants.text = "Places restantes: " + mData[position].nb_participants
            createur.text = "Créée par : " + mData[position].createur
            lieu.text = mData[position].lieu
        }
    }
}
