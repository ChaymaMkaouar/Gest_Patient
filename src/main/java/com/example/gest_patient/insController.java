package com.example.gest_patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class insController {

        private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        private static final String DB_URL = "jdbc:mysql://localhost:3306/gest_patient?user=root&password=";

        @FXML
        private TextField cin;

        @FXML
        private Button insc;

        @FXML
        private TextField login;

        @FXML
        private PasswordField mdp;

        @FXML
        private TextField nom;

        @FXML
        private TextField prenom;

        // Méthode pour gérer le clic sur le bouton "S'inscrire"
        @FXML
        private void sInscrire(ActionEvent event) {
                // Récupérer les valeurs des champs
                String cinValue = cin.getText();
                String loginValue = login.getText();
                String mdpValue = mdp.getText();
                String nomValue = nom.getText();
                String prenomValue = prenom.getText();

                Connection connection = null;
                try {
                        // Chargement du pilote JDBC
                        Class.forName(JDBC_DRIVER);

                        // Connexion à la base de données
                        connection = DriverManager.getConnection(DB_URL);

                        // Requête SQL pour insérer les données dans la table "patient"
                        String query = "INSERT INTO personnel (cin,nom,prenom,login,password,fonction) VALUES (?, ?, ?, ?, ?, 'hi')";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, cinValue);
                        preparedStatement.setString(2, loginValue);
                        preparedStatement.setString(3, mdpValue);
                        preparedStatement.setString(4, nomValue);
                        preparedStatement.setString(5, prenomValue);

                        // Exécuter la requête d'insertion
                        int rowsInserted = preparedStatement.executeUpdate();

                        if (rowsInserted > 0) {
                                // Afficher un message de réussite
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inscription réussie !", ButtonType.OK);
                                alert.showAndWait();
                        } else {
                                // Afficher un message d'erreur si l'insertion a échoué
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de l'inscription !", ButtonType.OK);
                                alert.showAndWait();
                        }

                        // Fermer la connexion et la déclaration
                        preparedStatement.close();
                        connection.close();

                } catch (ClassNotFoundException | SQLException e) {
                        // Afficher une erreur s'il y a un problème avec la connexion à la base de données
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur de connexion à la base de données : " + e.getMessage(), ButtonType.OK);
                        alert.showAndWait();
                } finally {
                        // Fermer la connexion
                        if (connection != null) {
                                try {
                                        connection.close();
                                } catch (SQLException e) {
                                        // Gérer l'exception de fermeture de connexion
                                        e.printStackTrace();
                                }
                        }
                }
        }
}
