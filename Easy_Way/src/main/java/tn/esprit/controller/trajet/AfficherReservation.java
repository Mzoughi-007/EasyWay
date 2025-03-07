package tn.esprit.controller.trajet;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.trajet.Reservation;
import tn.esprit.services.trajet.ServiceReservation;

import java.io.IOException;
import java.util.List;
import javafx.scene.control.ListCell;



public class AfficherReservation {
    @FXML
    private TextField arret;

    @FXML
    private TextField depart;

    @FXML
    private TextField vehicule;

    @FXML
    private TextField nb;

    @FXML
    private AnchorPane mama_anchor;

    @FXML
    private ComboBox<Integer> id;

    @FXML
    private TextField Supprimer;

    @FXML
    private TextField Retour;

    @FXML
    Label erreurLabel;

    @FXML
    private ListView<Reservation> View;

    private ServiceReservation sr = new ServiceReservation();

    public void setReservations(List<Reservation> reservations) {
        View.getItems().clear();
        View.getItems().addAll(reservations);
    }
    //alerteee!!
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.getDialogPane().setStyle(" -fx-font-size: 14px;-fx-font-family: 'Calibri';");
        alert.showAndWait();
    }


    @FXML
    void supprimer(ActionEvent event) {
        Reservation selectedReservation = View.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            View.getItems().remove(selectedReservation);
            sr.delete(selectedReservation); // Assuming you have a deleteReservation method in your service layer
        } else {
            showError("Aucune réservation sélectionnée.");
        }
    }

    @FXML
    public void initialize() {
        List<Reservation> reservations = sr.getAll();
        View.getItems().addAll(reservations);

        View.setCellFactory(param -> new ListCell<Reservation>() {
            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);  // If the item is empty, set the text to null
                    setGraphic(null);  // Remove any graphic
                } else {
                    // Create a VBox layout for each reservation item
                    VBox cellLayout = new VBox();
                    cellLayout.setSpacing(10);  // Add space between elements
                    cellLayout.setStyle("-fx-padding: 10px; -fx-background-color: white; -fx-border-radius: 10px; -fx-border-color: #ddd;");

                    // Reservation information
                    Label departLabel = new Label("Point de départ: " + item.getDepart());
                    departLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                    Label arretLabel = new Label("Point d'arrêt: " + item.getArret());
                    arretLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

                    Label vehiculeLabel = new Label("Mode de transport: " + item.getVehicule());
                    vehiculeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

                    Label nbLabel = new Label("Nombre de places: " + item.getNb());
                    nbLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");

                    cellLayout.getChildren().addAll(departLabel, arretLabel, vehiculeLabel, nbLabel);

                    setGraphic(cellLayout);
                }
            }
        });
    }

    @FXML
    void back(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/ajoutReservation.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Transition to the next scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

