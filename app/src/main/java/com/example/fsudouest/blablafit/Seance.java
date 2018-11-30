package com.example.fsudouest.blablafit;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Seance implements Serializable {

    private String titre;
    private String lieu;
    private String description;
    private Date date;
    private String nb_participants;
    private String createur;
    private String duree;
    private String id;


    public Seance(String titre, String lieu, String description, Date date, String nb_participants, String createur, String duree,String id) {
        this.titre = titre;
        this.lieu = lieu;
        this.description = description;
        this.date = date;
        this.nb_participants = nb_participants;
        this.createur = createur;
        this.duree = duree;
        this.id=id;
    }

    public Seance(){}

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

    public Date getDate() {
        return date;
    }

    public String getNb_participants() {
        return nb_participants;
    }

    public void setNb_participants(String nb_participants) {
        this.nb_participants = nb_participants;
    }

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
