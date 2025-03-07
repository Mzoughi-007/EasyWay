package tn.esprit.controller.trajet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.models.trajet.Ligne;
import tn.esprit.models.trajet.Station;
import tn.esprit.services.trajet.ServiceLigne;
import tn.esprit.services.trajet.ServiceStation;
import tn.esprit.util.SessionManager;

public class AjoutLigne {

    @FXML
    private TextField depart, arret;

    @FXML
    private Spinner<Integer> nb_station;

    @FXML
    private VBox stationContainer;

    @FXML
    private ComboBox<String> type;

    private ServiceLigne serviceLigne = new ServiceLigne();
    private ServiceStation serviceStation = new ServiceStation();

    private int ligneId = -1;

    @FXML
    public void initialize() {
        type.getItems().addAll("Bus", "Metro", "Train");
        nb_station.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1));
        stationContainer.setVisible(false);
        stationContainer.setStyle(
                "-fx-background-color: #f4f4f4;" +       // Set background color
                        "-fx-padding: 20px;" +                     // Add padding around the VBox
                        "-fx-spacing: 10px;" +                     // Set spacing between child elements
                        "-fx-border-radius: 10px;" +               // Rounded corners for the VBox
                        "-fx-border-color: #ccc;" +                // Set border color
                        "-fx-border-width: 2px;"                   // Set border width
        );
    }

    @FXML
    public void onValiderClicked(ActionEvent event) {
        if (depart.getText().isEmpty() || arret.getText().isEmpty() || type.getValue() == null) {
            showAlert("Validation Error", "Please fill in all the fields for Ligne.");
            return;
        }

        SessionManager.getInstance().setId_user(4);
        int adminId = SessionManager.getInstance().getId_user();

        if (adminId <= 0) {
            showAlert("Validation Error", "Invalid Admin ID.");
            return;
        }

        Ligne ligne = new Ligne(depart.getText(), arret.getText(), type.getValue(), adminId);
        serviceLigne.add(ligne);  // This should set the ligne's ID now

        ligneId = ligne.getId();  // Directly use the ligne object's ID

        if (ligneId <= 0) {
            showAlert("Error", "Failed to create Ligne.");
            return;
        }

        int numberOfStations = nb_station.getValue();
        stationContainer.getChildren().clear();

        for (int i = 0; i < numberOfStations; i++) {
            VBox stationBox = createStationBox(i);
            stationContainer.getChildren().add(stationBox);
        }

        stationContainer.setVisible(true);
        showAlert("Success", "Ligne and Stations added successfully!");
    }

    private VBox createStationBox(int index) {
        VBox stationBox = new VBox(10);
        stationBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");

        TextField stationNom = new TextField();
        stationNom.setPromptText("Nom de la station");

        TextField stationLocalisation = new TextField();
        stationLocalisation.setPromptText("Localisation de la station");

        Button validerBtn = new Button("Valider");
        validerBtn.setOnAction(event -> validerStation(event, stationNom, stationLocalisation, index));

        Button modifierBtn = new Button("Modifier");
        modifierBtn.setOnAction(event -> modifierStation(event, stationNom, stationLocalisation, index));

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(event -> supprimerStation(event, stationNom, stationLocalisation, index));

        stationBox.getChildren().addAll(stationNom, stationLocalisation, validerBtn, modifierBtn, supprimerBtn);
        return stationBox;
    }

    @FXML
    private void validerStation(ActionEvent event, TextField stationNom, TextField stationLocalisation, int index) {
        if (stationNom.getText().isEmpty() || stationLocalisation.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all the fields for Station.");
            return;
        }

        if (ligneId <= 0) {
            showAlert("Error", "Invalid Ligne ID.");
            return;
        }

        // Using the ligneId obtained earlier and adding the station
        Station station = new Station(stationNom.getText(), stationLocalisation.getText(), ligneId, SessionManager.getInstance().getId_user());
        serviceStation.add(station);

        showAlert("Success", "Station added successfully!");
    }

    @FXML
    private void modifierStation(ActionEvent event, TextField stationNom, TextField stationLocalisation, int index) {
        if (stationNom.getText().isEmpty() || stationLocalisation.getText().isEmpty()) {
            showAlert("Validation Error", "Please fill in all the fields for Station.");
            return;
        }

        if (ligneId <= 0) {
            showAlert("Error", "Invalid Ligne ID.");
            return;
        }

        // Get the station ID by the index for modification
        int stationId = serviceStation.getStationIdByIndex(ligneId, index);
        if (stationId <= 0) {
            showAlert("Error", "Station not found.");
            return;
        }

        Station station = new Station(stationNom.getText(), stationLocalisation.getText(), ligneId, SessionManager.getInstance().getId_user());
        station.setId(stationId);
        serviceStation.update(station);

        showAlert("Success", "Station updated successfully!");
    }

    @FXML
    private void supprimerStation(ActionEvent event, TextField stationNom, TextField stationLocalisation, int index) {
        if (ligneId <= 0) {
            showAlert("Error", "Invalid Ligne ID.");
            return;
        }

        // Get the station ID by the index for deletion
        int stationId = serviceStation.getStationIdByIndex(ligneId, index);
        if (stationId <= 0) {
            showAlert("Error", "Station not found.");
            return;
        }

        Station station = new Station(stationNom.getText(), stationLocalisation.getText(), ligneId, SessionManager.getInstance().getId_user());
        station.setId(stationId);
        serviceStation.delete(station);

        stationContainer.getChildren().remove(index);

        showAlert("Success", "Station deleted successfully!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
