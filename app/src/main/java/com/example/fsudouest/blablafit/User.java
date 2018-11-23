package com.example.fsudouest.blablafit;

public class User {

    private String nomComplet;
    private String email;
    private double note;


    public User(String nomComplet, String email) {
        this.nomComplet = nomComplet;
        this.email = email;
        this.note = 0.0;
    }

    public String getNom() {
        return nomComplet;
    }

    public void setNom(String nom) {
        this.nomComplet = nom;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
