package com.example.gest_patient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.example.gest_patient.data.Personnel;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientController {
    @FXML
    private Hyperlink insc;
    @FXML
    private PasswordField mdp;

    @FXML
    private TextField nom;

    @FXML
    private Button connecterButton;

    @FXML
    public void initialize() {
        // Initialisation, par exemple, désactiver le bouton jusqu'à ce que les champs soient remplis
        connecterButton.setDisable(true);

        // Ajouter des écouteurs de changement pour activer/désactiver le bouton en fonction du contenu des champs
        nom.textProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
        mdp.textProperty().addListener((observable, oldValue, newValue) -> updateButtonState());
    }
    @FXML
    public void handleInscriptionLink(ActionEvent event) {
        try {
            // Charger la vue d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("insc.fxml"));
            Parent root = loader.load();

            // Afficher la vue d'inscription dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Inscription");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleLogin() {
        String username = nom.getText();
        String password = mdp.getText();

        // Utiliser la méthode checkLogin de la classe Personnel pour vérifier les informations de connexion
        boolean loginSuccess = Personnel.checkLogin(username, password);

        if (loginSuccess) {
            try {
                // Charger la vue du tableau de bord
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                Parent root = loader.load();

                // Afficher la vue du tableau de bord dans la fenêtre principale
                Stage stage = (Stage) connecterButton.getScene().getWindow(); // Récupérer la fenêtre principale
                stage.setTitle("Tableau de bord");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Afficher un message d'erreur
            showAlert(Alert.AlertType.ERROR, "Échec de la connexion !", "Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    private void updateButtonState() {
        boolean disabled = nom.getText().isEmpty() || mdp.getText().isEmpty();
        connecterButton.setDisable(disabled);
    }

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
