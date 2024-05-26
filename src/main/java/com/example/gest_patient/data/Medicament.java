package com.example.gest_patient.data;

public class Medicament {
    private int ref;
    private String libelle;
    private double prix;
    public Medicament(int ref , String libelle,double prix){
        this.ref=ref;
        this.libelle=libelle;
        this.prix=prix;
    }

    // Getters and setters
    public int getRef() {
        return ref;
    }

    public void setRef(int ref) {
        this.ref = ref;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }
}
