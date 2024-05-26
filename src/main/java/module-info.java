module com.example.gest_patient {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.gest_patient;
    requires java.sql;
    opens com.example.gest_patient.data to javafx.base;
    opens com.example.gest_patient to javafx.fxml;
}