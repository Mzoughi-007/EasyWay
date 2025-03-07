package tn.esprit.controller.vehicule;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.vehicules.Etat;
import tn.esprit.models.vehicules.Train;
import tn.esprit.models.vehicules.TypeVehicule;
import tn.esprit.services.VehiculeService.ServiceVehicule;

public class AjouterTrainController {

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
    private TextField NombreWagonsField;
    @FXML
    private TextField proprietaireField;
    @FXML
    private TextField longuerReseauField;
    @FXML
    private TextField nombreLignesField;
    @FXML
    private TextField vitesseField;

    @FXML
    private void initialize() {

        etatComboBox.getItems().setAll(Etat.values());
    }

    private ServiceVehicule vehiculeService = new ServiceVehicule();

    @FXML
    private void handleAjouter() {

        String immatricule = immatriculeField.getText().trim();
        String capaciteText = capaciteField.getText().trim();
        Etat etat = etatComboBox.getValue();
        String nomConducteur = nomConducteurField.getText().trim();
        String prenomConducteur = prenomConducteurField.getText().trim();
        String depart = DepartField.getText().trim();
        String arret = ArretField.getText().trim();
        String longueurReseauText = longuerReseauField.getText().trim();
        String nombreLignesText = nombreLignesField.getText().trim();
        String nombreWagonsText = NombreWagonsField.getText().trim();
        String proprietaire = proprietaireField.getText().trim();
        String vitesseText = vitesseField.getText().trim();


        if (immatricule.isEmpty() || capaciteText.isEmpty() || etat == null ||
                nomConducteur.isEmpty() || prenomConducteur.isEmpty() || depart.isEmpty() ||
                arret.isEmpty() || longueurReseauText.isEmpty() || nombreLignesText.isEmpty() ||
                nombreWagonsText.isEmpty() || proprietaire.isEmpty() || vitesseText.isEmpty()) {
            showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires !");
            return;
        }

        int capacite, nombreLignes, nombreWagons;
        double longueurReseau, vitesse;

        try {
            capacite = Integer.parseInt(capaciteText);
            nombreLignes = Integer.parseInt(nombreLignesText);
            nombreWagons = Integer.parseInt(nombreWagonsText);
            longueurReseau = Double.parseDouble(longueurReseauText);
            vitesse = Double.parseDouble(vitesseText);

            if (capacite <= 0 || nombreLignes <= 0 || nombreWagons <= 0 || longueurReseau <= 0 || vitesse <= 0) {
                showAlert("Erreur de saisie", "Les valeurs numériques doivent être positives !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Veuillez entrer des nombres valides pour la capacité, le nombre de lignes, le nombre de wagons, la longueur du réseau et la vitesse !");
            return;
        }


        int idConducteur = vehiculeService.getConducteurId(nomConducteur, prenomConducteur);
        if (idConducteur == -1) {
            showAlert("Erreur de saisie", "Le conducteur spécifié n'existe pas !");
            return;
        }


        int idTrajet = vehiculeService.getTrajetId(depart, arret);
        if (idTrajet == -1) {
            showAlert("Erreur de saisie", "Le trajet spécifié n'existe pas !");
            return;
        }


        Train train = new Train();
        train.setImmatriculation(immatricule);
        train.setCapacite(capacite);
        train.setEtat(etat);
        train.setTypeVehicule(TypeVehicule.TRAIN);
        train.setIdConducteur(idConducteur);
        train.setIdTrajet(idTrajet);
        train.setLongueurReseau(longueurReseau);
        train.setNombreLignes(nombreLignes);
        train.setNombreWagons(nombreWagons);
        train.setProprietaire(proprietaire);
        train.setVitesseMaximale(vitesse);


        vehiculeService.add(train);
        refreshParentView();

        Stage stage = (Stage) immatriculeField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleAnnuler() {
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
    private TextField immatriculationField; // ou n'importe quel autre contrôle FXML

    private Runnable onTrainAdded;

    public void setOnTrainAdded(Runnable callback) {
        this.onTrainAdded = callback;
    }

    private void refreshParentView() {
        if (onTrainAdded != null) {
            onTrainAdded.run();
        }
    }
}