package com.example.fsudouest.blablafit;

public class User {

    private int id;
    private String nomComplet;
    private String email;
    private double note;
    private String photoUrl;


    public User(String nomComplet, String email, String photoUrl) {
        this.nomComplet = nomComplet;
        this.email = email;
        this.note = 0.0;
        this.photoUrl = photoUrl;
    }

    public User(){}

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nomComplet;
    }
}
