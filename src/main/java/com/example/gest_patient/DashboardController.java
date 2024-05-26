package com.example.gest_patient;

import com.example.gest_patient.data.Medicament;
import com.example.gest_patient.data.Patient;
import com.example.gest_patient.data.PatientMed;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gest_patient?user=root&password=";
    @FXML
    private Label nb_pat , nb_med , nb_aff;
    @FXML
    private Button ajout_aff;
    @FXML
    private ComboBox<String> cin_pat;
    @FXML
    private TableColumn<Patient, Integer> col_cinpat;
    @FXML
    private TableColumn<Medicament, Integer> col_refmed;

    @FXML
    private ComboBox<String> ref_med;
    @FXML
    private TextField searchaff;
    @FXML
    private Button supp_aff;
    @FXML
    private TableView<PatientMed> tab_aff;

    @FXML
    private TextField ref;
    @FXML
    private TextField libelle;
    @FXML
    private TextField prix;
    @FXML
    private Button ajout_med;

    @FXML
    private Button modif_med;

    @FXML
    private Button supp_med;
    @FXML
    private TableColumn<Medicament, Integer> col_ref;
    @FXML
    private TableColumn<Medicament, String> col_lib;
    @FXML
    private TableColumn<Medicament, Double> col_prix;
    @FXML
    private Button ajout;

    @FXML
    private Button btnaffecter;

    @FXML
    private Button btnhome;

    @FXML
    private Button btnlogout;

    @FXML
    private Button btnmedicament;

    @FXML
    private Button btnpatient;

    @FXML
    private TextField cin;

    @FXML
    private TableColumn<Patient, String> col_nom;

    @FXML
    private TableColumn<Patient, String> col_pren;

    @FXML
    private TableColumn<Patient, String> col_tel;
    @FXML
    private TableColumn<Patient, Integer> col_cin;
    @FXML
    private TableColumn<Patient, String> col_sexe;

    @FXML
    private Button modifier;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField search;

    @FXML
    private TextField sexe;

    @FXML
    private Button supprimer;

    @FXML
    private TableView<Patient> tab_pat;
    @FXML
    private TableView<Medicament> tab_med;
    @FXML
    private TextField tel;

    @FXML
    private AnchorPane interfAffect;

    @FXML
    private AnchorPane interfHome;

    @FXML
    private AnchorPane interfMed;

    @FXML
    private AnchorPane interfPat;
    @FXML
    private TextField searchMed;
    private ObservableList<Patient> originalPatientList;
    private ObservableList<Medicament> originalMedicamentList;
    private ObservableList<PatientMed> originalPatientMedList;

    public int getNombreTotalPatients() {
        int nombrePatients = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM patient");
            if (rs.next()) {
                nombrePatients = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombrePatients;
    }

    public int getNombreTotalMedicaments() {
        int nombreMedicaments = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM medicament");
            if (rs.next()) {
                nombreMedicaments = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreMedicaments;
    }

    public int getNombreTotalAffectations() {
        int nombreAffectations = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM patientmed");
            if (rs.next()) {
                nombreAffectations = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreAffectations;
    }
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_pren.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        col_sexe.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        col_cin.setCellValueFactory(new PropertyValueFactory<>("cin"));
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPatients(newValue);
        });
        col_ref.setCellValueFactory(new PropertyValueFactory<>("ref"));
        col_lib.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        col_prix.setCellValueFactory(new PropertyValueFactory<>("prix"));

        searchMed.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMedicaments(newValue);
        });
        loadMedicamentData();
        col_refmed.setCellValueFactory(new PropertyValueFactory<>("refMed"));
        col_cinpat.setCellValueFactory(new PropertyValueFactory<>("cinPat"));
        loadPatientDataIntoComboBox();
        loadMedicamentDataIntoComboBox();
        loadAffecterData();
        searchaff.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAffecter(newValue);
        });

        int nombreAffectations = getNombreTotalAffectations();
        int nombreMedicaments = getNombreTotalMedicaments();
        int nombrePatients = getNombreTotalPatients();

        nb_aff.setText(String.valueOf(nombreAffectations));
        nb_med.setText(String.valueOf(nombreMedicaments));
        nb_pat.setText(String.valueOf(nombrePatients));


    }
    private void filterAffecter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            tab_aff.setItems(originalPatientMedList); // Réinitialiser la TableView avec les données complètes
        } else {
            try {
                int searchValue = Integer.parseInt(keyword);

                ObservableList<PatientMed> filteredList = originalPatientMedList.filtered(patientMed ->
                        patientMed.getRefMed() == searchValue || patientMed.getCinPat() == searchValue
                );

                tab_aff.setItems(filteredList);
            } catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }
    }
    private void loadAffecterData() {
        ObservableList<PatientMed> affecterList = FXCollections.observableArrayList();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "SELECT refMed, cinPat FROM patientmed";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                PatientMed affecter = new PatientMed(
                        rs.getInt("refMed"),
                        rs.getInt("cinPat")
                );
                affecterList.add(affecter);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        originalPatientMedList = FXCollections.observableArrayList(affecterList);
        tab_aff.setItems(affecterList);
    }
    private void loadPatientDataIntoComboBox() {
        ObservableList<String> patientCinList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT cin FROM patient");
            while (rs.next()) {
                patientCinList.add(String.valueOf(rs.getInt("cin")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cin_pat.setItems(patientCinList);
        System.out.println("Loaded Patient CINs into ComboBox: " + patientCinList); // Debugging message
    }
    private void loadMedicamentDataIntoComboBox() {
        ObservableList<String> medicamentRefList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT ref FROM medicament");
            while (rs.next()) {
                medicamentRefList.add(String.valueOf(rs.getInt("ref")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ref_med.setItems(medicamentRefList);
        System.out.println("Loaded Medicament Refs into ComboBox: " + medicamentRefList);
    }
    @FXML
    private void handleAjouterAffectation(ActionEvent event) {
        String selectedRefMed = ref_med.getValue();
        String selectedCinPat = cin_pat.getValue();

        if (selectedRefMed != null && selectedCinPat != null) {
            addAffectationToDatabase(selectedRefMed, selectedCinPat);
            loadAffecterData();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un médicament et un patient.");
        }
    }

    private void addAffectationToDatabase(String selectedRefMed, String selectedCinPat) {
        try {
            int refMed = Integer.parseInt(selectedRefMed);
            int cinPat = Integer.parseInt(selectedCinPat);

            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                String sql = "INSERT INTO patientmed (refMed, cinPat) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, refMed);
                pstmt.setInt(2, cinPat);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Affectation ajoutée avec succès !");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout de l'affectation.");
                }

                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'exécution de la requête SQL.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner une valeur valide.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }








    private void filterMedicaments(String searchText) {
        ObservableList<Medicament> filteredList = FXCollections.observableArrayList();
        if (searchText.isEmpty()) {
            tab_med.setItems(originalMedicamentList);
        } else {
            for (Medicament medicament : originalMedicamentList) {
                if (medicament.getLibelle().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(medicament);
                }
            }
            tab_med.setItems(filteredList);
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        Stage stage = (Stage) btnlogout.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBtnHome(ActionEvent event) {
        showPane(interfHome);
    }

    @FXML
    private void handleBtnPatient(ActionEvent event) {
        showPane(interfPat);
        loadPatientData();
        cin.requestFocus();
        modifier.setDisable(true);
        supprimer.setDisable(true);
    }

    @FXML
    private void handleBtnMedicament(ActionEvent event) {
        showPane(interfMed);
        loadMedicamentData();
        ref.requestFocus();
        modif_med.setDisable(true);
        supp_med.setDisable(true);

    }

    @FXML
    private void handleBtnAffecter(ActionEvent event) throws SQLException {
        showPane(interfAffect);
        ref_med.requestFocus();
        supp_aff.setDisable(true);
    }

    @FXML
    private void handleAjout(ActionEvent event) {
        String patientCin = cin.getText();
        String patientNom = nom.getText();
        String patientPrenom = prenom.getText();
        String patientSexe = sexe.getText();
        String patientTel = tel.getText();

        if (!patientCin.isEmpty() && !patientNom.isEmpty() && !patientPrenom.isEmpty() && !patientSexe.isEmpty() && !patientTel.isEmpty()) {
            addPatientToDatabase(patientCin, patientNom, patientPrenom, patientSexe, patientTel);
            loadPatientData();

            cin.clear();
            nom.clear();
            prenom.clear();
            sexe.clear();
            tel.clear();
        } else {
            System.out.println("Veuillez remplir tous les champs.");
        }
    }

    @FXML
    private void handlePatientSelection() {
        Patient selectedPatient = tab_pat.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            this.cin.setText(String.valueOf(selectedPatient.getCin()));
            this.nom.setText(selectedPatient.getNom());
            this.prenom.setText(selectedPatient.getPrenom());
            this.sexe.setText(selectedPatient.getSexe());
            this.tel.setText(selectedPatient.getTel());
            modifier.setDisable(false);
            supprimer.setDisable(false);
        }
    }
    @FXML
    private void handleMedicamentSelection() {
        Medicament selectedMedicament = tab_med.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            this.ref.setText(String.valueOf(selectedMedicament.getRef()));
            this.libelle.setText(selectedMedicament.getLibelle());
            this.prix.setText(String.valueOf(selectedMedicament.getPrix()));
            modif_med.setDisable(false);
            supp_med.setDisable(false);
        }
    }
    @FXML
    private void handleAffecterSelection() {
        PatientMed selectedPatientMed = tab_aff.getSelectionModel().getSelectedItem();
        if (selectedPatientMed != null) {
            ref_med.setValue(String.valueOf(selectedPatientMed.getRefMed()));
            cin_pat.setValue(String.valueOf(selectedPatientMed.getCinPat()));
            supp_aff.setDisable(false);
        }
    }


    private void showPane(AnchorPane pane) {
        interfHome.setVisible(false);
        interfPat.setVisible(false);
        interfMed.setVisible(false);
        interfAffect.setVisible(false);
        pane.setVisible(true);
    }

    private void loadPatientData() {
        ObservableList<Patient> patientList = FXCollections.observableArrayList();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "SELECT cin, nom, prenom, tel  , sexe FROM patient";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("cin"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tel"),
                        rs.getString("sexe")
                );
                patientList.add(patient);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        originalPatientList = FXCollections.observableArrayList(patientList);
        tab_pat.setItems(patientList);
    }

    private void addPatientToDatabase(String cin, String nom, String prenom, String sexe, String tel) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "INSERT INTO patient (cin, nom, prenom, sexe, tel) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cin);
            pstmt.setString(2, nom);
            pstmt.setString(3, prenom);
            pstmt.setString(4, sexe);
            pstmt.setString(5, tel);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleModifier(ActionEvent event) {
        Patient selectedPatient = tab_pat.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            selectedPatient.setNom(nom.getText());
            selectedPatient.setPrenom(prenom.getText());
            selectedPatient.setSexe(sexe.getText());
            selectedPatient.setTel(tel.getText());

            tab_pat.refresh();

            updatePatientInDatabase(selectedPatient);
            nom.clear();
            prenom.clear();
            sexe.clear();
            tel.clear();
            cin.clear();
        }
    }


    private void updatePatientInDatabase(Patient patient) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "UPDATE patient SET nom = ?, prenom = ?, sexe = ?, tel = ? WHERE cin = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, patient.getNom());
            pstmt.setString(2, patient.getPrenom());
            pstmt.setString(3, patient.getSexe());
            pstmt.setString(4, patient.getTel());
            pstmt.setInt(5, patient.getCin());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSupprimer(ActionEvent event) {
        Patient selectedPatient = tab_pat.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce patient ?");
            alert.setContentText("Cette action est irréversible.");

            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    deletePatient(selectedPatient);
                }
            });
        }
    }

    private void deleteAffect(PatientMed patientmed) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM patientmed WHERE refMed = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, patientmed.getRefMed());
            pstmt.executeUpdate();

            tab_aff.getItems().remove(patientmed);

            pstmt.close();
            conn.close();
            ref_med.getSelectionModel().clearSelection();
            cin_pat.getSelectionModel().clearSelection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleSupprimerAff(ActionEvent event) {
        PatientMed selectedPatientMed = tab_aff.getSelectionModel().getSelectedItem();
        if (selectedPatientMed != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce patient ?");
            alert.setContentText("Cette action est irréversible.");

            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    deleteAffect(selectedPatientMed);
                }
            });
        }
    }

    private void deletePatient(Patient patient) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM patient WHERE cin = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, patient.getCin());
            pstmt.executeUpdate();

            tab_pat.getItems().remove(patient);

            pstmt.close();
            conn.close();
            cin.clear();
            nom.clear();
            prenom.clear();
            sexe.clear();
            tel.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterPatients(String searchText) {
        ObservableList<Patient> filteredList = FXCollections.observableArrayList();
        if (searchText.isEmpty()) {
            tab_pat.setItems(originalPatientList);
        } else {
            for (Patient patient : originalPatientList) {
                if (patient.getNom().toLowerCase().contains(searchText.toLowerCase()) ||
                        patient.getPrenom().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(patient);
                }
            }
            tab_pat.setItems(filteredList);
        }
    }
    private void loadMedicamentData() {
        ObservableList<Medicament> medicamentList = FXCollections.observableArrayList();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "SELECT ref, libelle, prix FROM medicament";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Medicament medicament = new Medicament(
                        rs.getInt("ref"),
                        rs.getString("libelle"),
                        rs.getDouble("prix")
                );
                medicamentList.add(medicament);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        originalMedicamentList = FXCollections.observableArrayList(medicamentList);
        tab_med.setItems(medicamentList);
    }



    @FXML
    private void handleAjoutMedicament(ActionEvent event) {
        String medicamentRef = ref.getText();
        String medicamentLibelle = libelle.getText();
        String medicamentPrix = prix.getText();

        if (!medicamentRef.isEmpty() && !medicamentLibelle.isEmpty() && !medicamentPrix.isEmpty()) {
            addMedicamentToDatabase(medicamentRef, medicamentLibelle, medicamentPrix);
            loadMedicamentData();

            ref.clear();
            libelle.clear();
            prix.clear();
        } else {
            System.out.println("Veuillez remplir tous les champs.");
        }
    }

    private void addMedicamentToDatabase(String ref, String libelle, String prix) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "INSERT INTO medicament (ref, libelle, prix) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ref);
            pstmt.setString(2, libelle);
            pstmt.setString(3, prix);
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifierMedicament(ActionEvent event) {
        Medicament selectedMedicament = tab_med.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            selectedMedicament.setLibelle(libelle.getText());
            selectedMedicament.setPrix(Double.parseDouble(prix.getText()));

            tab_med.refresh();

            updateMedicamentInDatabase(selectedMedicament);

            ref.clear();
            libelle.clear();
            prix.clear();
        }
    }

    private void updateMedicamentInDatabase(Medicament medicament) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "UPDATE medicament SET libelle = ?, prix = ? WHERE ref = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, medicament.getLibelle());
            pstmt.setDouble(2, medicament.getPrix());
            pstmt.setInt(3, medicament.getRef());
            pstmt.executeUpdate();

            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimerMedicament(ActionEvent event) {
        Medicament selectedMedicament = tab_med.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Voulez-vous vraiment supprimer ce médicament ?");
            alert.setContentText("Cette action est irréversible.");

            ButtonType buttonTypeYes = new ButtonType("Oui");
            ButtonType buttonTypeNo = new ButtonType("Non");
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    deleteMedicament(selectedMedicament);
                }
            });
        }
    }

    private void deleteMedicament(Medicament medicament) {
        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL);
            String sql = "DELETE FROM medicament WHERE ref = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, medicament.getRef());
            pstmt.executeUpdate();

            tab_med.getItems().remove(medicament);

            pstmt.close();
            conn.close();
            ref.clear();
            libelle.clear();
            prix.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
