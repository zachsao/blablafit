package com.example.fsudouest.blablafit;

import java.sql.Time;
import java.util.Date;

public class Seance {

    private String titre;
    private String lieu;
    private String description;
    private String date;
    private String heure;
    private int nb_participants;
    private String createur;
    private int duree;


    public Seance(String titre, String lieu, String description, String date, String heure, int nb_participants, String createur, int duree) {
        this.titre = titre;
        this.lieu = lieu;
        this.description = description;
        this.date = date;
        this.heure = heure;
        this.nb_participants = nb_participants;
        this.createur = createur;
        this.duree = duree;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public int getNb_participants() {
        return nb_participants;
    }

    public void setNb_participants(int nb_participants) {
        this.nb_participants = nb_participants;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
}
