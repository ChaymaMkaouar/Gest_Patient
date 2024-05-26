package com.example.gest_patient.data;

public class PatientMed {
    private int refMed;
    private int cinPat;
    public PatientMed(int refMed , int cinPat){
        this.refMed=refMed;
        this.cinPat=cinPat;
    }



    // Getters and setters
    public int getRefMed() {
        return refMed;
    }

    public void setRedMed(int refMed) {
        this.refMed = refMed;
    }

    public int getCinPat() {
        return cinPat;
    }

    public void setCinPat(int cinPat) {
        this.cinPat = cinPat;
    }
}
