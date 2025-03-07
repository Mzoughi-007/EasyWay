
package tn.esprit.controller.vehicule;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.vehicules.*;
import tn.esprit.services.VehiculeService.ServiceVehicule;


public class AjouterBusController {

    @FXML
    private TextField immatriculeField;
    @FXML
    private TextField capaciteField;
    @FXML
    private ComboBox<Etat> etatComboBox;
    @FXML
    private TextField nomConducteurField;
    @FXML
    private TextField prenomConducteurField;
    @FXML
    private TextField DepartField;
    @FXML
    private TextField ArretField;
    @FXML
    private TextField nombrePortesField;
    @FXML
    private ComboBox<TypeService> typeServiceComboBox;
    @FXML
    private TextField placesField;
    @FXML
    private TextField compagnieField;
    @FXML
    private CheckBox climatisationCheckBox;

    private ServiceVehicule vehiculeService = new ServiceVehicule();

    @FXML
    private void initialize() {
        typeServiceComboBox.getItems().setAll(TypeService.values());
        etatComboBox.getItems().setAll(Etat.values());
    }

    @FXML
    private void handleAjouter() {
        String immatricule = immatriculeField.getText().trim();
        String capaciteText = capaciteField.getText().trim();
        Etat etat = etatComboBox.getValue();
        String nomConducteur = nomConducteurField.getText().trim();
        String prenomConducteur = prenomConducteurField.getText().trim();
        String departTrajet = DepartField.getText().trim();
        String arretTrajet = ArretField.getText().trim();
        String nombrePortesText = nombrePortesField.getText().trim();
        TypeService typeService = typeServiceComboBox.getValue();
        String placesText = placesField.getText().trim();
        String compagnie = compagnieField.getText().trim();

        if (immatricule.isEmpty() || capaciteText.isEmpty() || etat == null ||
                nomConducteur.isEmpty() || prenomConducteur.isEmpty() || departTrajet.isEmpty() ||
                arretTrajet.isEmpty() || nombrePortesText.isEmpty() || typeService == null ||
                placesText.isEmpty() || compagnie.isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires !");
            return;
        }

        int capacite, nombrePortes, places;
        try {
            capacite = Integer.parseInt(capaciteText);
            nombrePortes = Integer.parseInt(nombrePortesText);
            places = Integer.parseInt(placesText);

            if (capacite <= 0 || nombrePortes <= 0 || places <= 0) {
                showAlert("Erreur de saisie", "Les valeurs numériques doivent être positives !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer des nombres valides pour la capacité, les portes et les places !");
            return;
        }

        int idConducteur = vehiculeService.getConducteurId(nomConducteur, prenomConducteur);
        if (idConducteur == -1) {
            showAlert("Erreur de saisie", "Le conducteur spécifié n'existe pas !");
            return;
        }

        int idTrajet = vehiculeService.getTrajetId(departTrajet, arretTrajet);
        if (idTrajet == -1) {
            showAlert("Erreur de saisie", "Le trajet spécifié n'existe pas !");
            return;
        }

        boolean climatisation = climatisationCheckBox.isSelected();

        Bus bus = new Bus();
        bus.setImmatriculation(immatricule);
        bus.setCapacite(capacite);
        bus.setEtat(etat);
        bus.setIdConducteur(idConducteur);
        bus.setIdTrajet(idTrajet);
        bus.setTypeVehicule(TypeVehicule.BUS);
        bus.setNombrePortes(nombrePortes);
        bus.setTypeService(typeService);
        bus.setNombreDePlaces(places);
        bus.setCompagnie(compagnie);
        bus.setClimatisation(climatisation);

        vehiculeService.add(bus);
        refreshParentView();

        Stage stage = (Stage) immatriculeField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) immatriculeField.getScene().getWindow();
        stage.close();
    }
    private Runnable onBusAdded;

    public void setOnBusAdded(Runnable callback) {
        this.onBusAdded = callback;
    }

    // Call this method after successfully adding a bus
    private void refreshParentView() {
        if (onBusAdded != null) {
            onBusAdded.run();
        }
    }


}
