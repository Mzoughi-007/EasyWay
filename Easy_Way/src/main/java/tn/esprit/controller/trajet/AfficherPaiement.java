package tn.esprit.controller.trajet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.models.trajet.Paiement;
import tn.esprit.models.trajet.Reservation;
import tn.esprit.services.trajet.ServicePaiement;
import tn.esprit.services.trajet.ServiceReservation;

import java.util.List;
import java.util.Collections;

public class AfficherPaiement {

    @FXML
    private ListView<Paiement> view;

    private ServicePaiement sp = new ServicePaiement();
    private ServiceReservation sr = new ServiceReservation();

    // Sets the list of payments
    public void setPaiement(List<Paiement> paiements) {
        view.getItems().clear();
        view.getItems().addAll(paiements);
    }

    // Shows error message in case of issues
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.getDialogPane().setStyle(" -fx-font-size: 14px;-fx-font-family: 'Calibri';");
        alert.showAndWait();
    }

    // Handles removal of a payment
    @FXML
    void supprimer(ActionEvent event) {
        Paiement selectedPay = view.getSelectionModel().getSelectedItem();
        if (selectedPay != null) {
            view.getItems().remove(selectedPay);
            sp.delete(selectedPay);  // Assumes this removes the payment from the database
        } else {
            showError("Aucune paiement sélectionné.");
        }
    }

    // Initializes the controller and loads the payments into the list view
    @FXML
    public void initialize() {
        // Fetch all payments
        List<Paiement> paiements = sp.getAll();
        // Retrieve all reservation IDs linked to payments
        List<Integer> reservationIds = sp.getReservationIds();
        // Retrieve all reservations for these IDs
        List<Reservation> reservations = sr.getReservationsByIds(reservationIds);

        // Add paiements to the ListView
        view.getItems().addAll(paiements);

        // Custom cell factory to display payment details along with related reservation
        view.setCellFactory(param -> new ListCell<Paiement>() {
            @Override
            protected void updateItem(Paiement item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox cellLayout = new VBox();
                    cellLayout.setSpacing(10);
                    cellLayout.setStyle("-fx-padding: 10px; -fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #ddd;");

                    // Payment information
                    Label montantLabel = new Label("Montant: " + item.getMontant() + " TND");
                    montantLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                    Label paiementIdLabel = new Label("ID Paiement: " + item.getPay_id());
                    paiementIdLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

                    // Find the corresponding reservation for this paiement using res_id
                    Reservation relatedReservation = null;
                    for (Reservation reservation : reservations) {
                        if (reservation.getId() == item.getRes_id()) {
                            relatedReservation = reservation;
                            break;
                        }
                    }

                    if (relatedReservation != null) {
                        // Reservation details
                        Label departLabel = new Label("Départ: " + relatedReservation.getDepart());
                        departLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

                        Label arretLabel = new Label("Arrêt: " + relatedReservation.getArret());
                        arretLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

                        Label vehiculeLabel = new Label("Véhicule: " + relatedReservation.getVehicule());
                        vehiculeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

                        Label nbLabel = new Label("Nb: " + relatedReservation.getNb());
                        nbLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

                        // Adding reservation details to the VBox
                        cellLayout.getChildren().addAll(departLabel, arretLabel, vehiculeLabel, nbLabel);
                    }

                    // Add payment-specific information (after reservation details)
                    cellLayout.getChildren().addAll(montantLabel, paiementIdLabel);
                    setGraphic(cellLayout);
                }
            }
        });
    }

    // When clicking the "details" button, display a new window with reservation details
    @FXML
    void details(ActionEvent event) {
        Paiement selectedPaiement = view.getSelectionModel().getSelectedItem();
        if (selectedPaiement != null) {
            // Retrieve the res_id for the selected payment
            int resId = selectedPaiement.getRes_id();

            // Create a list with the resId (as getReservationsByIds expects a list of IDs)
            List<Integer> reservationIds = Collections.singletonList(resId);

            List<Reservation> relatedReservations = sr.getReservationsByIds(reservationIds);
            if (!relatedReservations.isEmpty()) {
                Reservation relatedReservation = relatedReservations.get(0);

                // Create a new window (Stage) to display reservation details
                Stage detailsStage = new Stage();
                VBox vbox = new VBox();
                vbox.setSpacing(10);
                vbox.setStyle("-fx-padding: 10px; -fx-background-color: white;");

                // Adding reservation details to the VBox
                Label departLabel = new Label("Départ: " + relatedReservation.getDepart());
                departLabel.setStyle("-fx-font-size: 16px;");

                Label arretLabel = new Label("Arrêt: " + relatedReservation.getArret());
                arretLabel.setStyle("-fx-font-size: 16px;");

                Label vehiculeLabel = new Label("Véhicule: " + relatedReservation.getVehicule());
                vehiculeLabel.setStyle("-fx-font-size: 16px;");

                Label nbLabel = new Label("Nb: " + relatedReservation.getNb());
                nbLabel.setStyle("-fx-font-size: 16px;");

                Button closeButton = new Button("Close");
                closeButton.setOnAction(e -> detailsStage.close());

                vbox.getChildren().addAll(departLabel, arretLabel, vehiculeLabel, nbLabel, closeButton);

                Scene scene = new Scene(vbox, 300, 250);
                detailsStage.setScene(scene);
                detailsStage.setTitle("Reservation Details");
                detailsStage.show();
            } else {
                showError("Reservation not found for this payment.");
            }
        } else {
            showError("Please select a payment first.");
        }
    }
}
