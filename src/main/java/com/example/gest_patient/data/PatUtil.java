package com.example.gest_patient.data;

public class PatUtil {
    private static String dernierTitreErreur = "";
    private static String dernierMessageErreur = "";
    private static String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
    private static String DB_Url = "jdbc:mysql://localhost:3306/gest_patient?user=root&password=";
    public static String getDernierTitreErreur() {
        return dernierTitreErreur;
    }

    public static void setDernierTitreErreur(String dernierTitreErreur) {
        PatUtil.dernierTitreErreur = dernierTitreErreur;
    }

    public static String getDernierMessageErreur() {
        return dernierMessageErreur;
    }

    public static void setDernierMessageErreur(String dernierMessageErreur) {
        PatUtil.dernierMessageErreur = dernierMessageErreur;
    }
}
