package tn.esprit.controller.covoiturage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import tn.esprit.util.SessionManager;

import java.io.IOException;
import java.util.Optional;

public class option {

    @FXML
    private void option(ActionEvent event) {
        try {
            // Charger le fichier gestioncov.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Gestioncov.fxml"));
            Parent gestionCovRoot = loader.load();
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(gestionCovRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void offer(ActionEvent event) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/offres.fxml"));
                Parent gestionCovRoot = loader.load();
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(gestionCovRoot);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    @FXML
    void RedirectToChoix(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Choix.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void RedirectToReclamation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/ajoutReclamation.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void RedirectToLigne(ActionEvent event) {

    }

    @FXML
    void RedirectToProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserProfile.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void RedirectToTrajet(ActionEvent event) {

    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserSpace.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        }
    }
    @FXML
    private void handleCovoiturage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/option.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
