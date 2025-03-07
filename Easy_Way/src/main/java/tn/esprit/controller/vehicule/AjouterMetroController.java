
package tn.esprit.controller.vehicule;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.vehicules.Etat;
import tn.esprit.models.vehicules.Metro;
import tn.esprit.models.vehicules.TypeVehicule;
import tn.esprit.services.VehiculeService.ServiceVehicule;

public class AjouterMetroController {
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
    private TextField longuerReseauField;
    @FXML
    private TextField nombreLignesField;
    @FXML
    private TextField ramesField;
    @FXML
    private TextField ProprieteField;

    private ServiceVehicule serviceVehicule = new ServiceVehicule();

    @FXML
    private void initialize() {
        etatComboBox.getItems().setAll(Etat.values());
    }

    @FXML
    private void handleAjouter() {
        String immatricule = immatriculeField.getText().trim();
        String capaciteText = capaciteField.getText().trim();
        Etat etat = etatComboBox.getValue();
        String nomConducteur = nomConducteurField.getText().trim();
        String prenomConducteur = prenomConducteurField.getText().trim();
        String lieuDepart = DepartField.getText().trim();
        String lieuArrivee = ArretField.getText().trim();
        String longueurReseauText = longuerReseauField.getText().trim();
        String nombreLignesText = nombreLignesField.getText().trim();
        String ramesText = ramesField.getText().trim();
        String propriete = ProprieteField.getText().trim();

        if (immatricule.isEmpty() || capaciteText.isEmpty() || etat == null ||
                nomConducteur.isEmpty() || prenomConducteur.isEmpty() || lieuDepart.isEmpty() ||
                lieuArrivee.isEmpty() || longueurReseauText.isEmpty() || nombreLignesText.isEmpty() ||
                ramesText.isEmpty() || propriete.isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires !");
            return;
        }

        int capacite, nombreLignes, rames;
        double longueurReseau;

        try {
            capacite = Integer.parseInt(capaciteText);
            nombreLignes = Integer.parseInt(nombreLignesText);
            rames = Integer.parseInt(ramesText);
            longueurReseau = Double.parseDouble(longueurReseauText);

            if (capacite <= 0 || nombreLignes <= 0 || rames <= 0 || longueurReseau <= 0) {
                showAlert("Erreur de saisie", "Les valeurs numériques doivent être positives !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer des nombres valides pour la capacité, le nombre de lignes, les rames et la longueur du réseau !");
            return;
        }

        int idConducteur = serviceVehicule.getConducteurId(nomConducteur, prenomConducteur);
        if (idConducteur == -1) {
            showAlert("Erreur de saisie", "Le conducteur spécifié n'existe pas !");
            return;
        }

        int idTrajet = serviceVehicule.getTrajetId(lieuDepart, lieuArrivee);
        if (idTrajet == -1) {
            showAlert("Erreur de saisie", "Le trajet spécifié n'existe pas !");
            return;
        }

        Metro metro = new Metro();
        metro.setImmatriculation(immatricule);
        metro.setCapacite(capacite);
        metro.setEtat(etat);
        metro.setIdConducteur(idConducteur);
        metro.setIdTrajet(idTrajet);
        metro.setTypeVehicule(TypeVehicule.METRO);
        metro.setLongueurReseau(longueurReseau);
        metro.setNombreLignes(nombreLignes);
        metro.setNombreRames(rames);
        metro.setProprietaire(propriete);

        serviceVehicule.add(metro);
        refreshParentView();
        Stage stage = (Stage) immatriculeField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAnnuler() {
        Stage stage = (Stage) etatComboBox.getScene().getWindow();
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
    private TextField immatriculationField; // ou n'importe quel autre contrôle FXML

    private Runnable onMetroAdded;

    public void setOnMetroAdded(Runnable callback) {
        this.onMetroAdded = callback;
    }

    private void refreshParentView() {
        if (onMetroAdded != null) {
            onMetroAdded.run();
        }
    }


}
