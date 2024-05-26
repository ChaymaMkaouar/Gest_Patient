package com.example.gest_patient.data;

public class Patient {
    private String nom;
    private String prenom;
    private String tel;
    private int cin;
    private String sexe;

    public Patient(int cin, String nom, String prenom, String tel,String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.cin = cin;
        this.sexe = sexe;
    }



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom=nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom=prenom;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel=tel;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin=cin;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe= sexe;
    }
}
