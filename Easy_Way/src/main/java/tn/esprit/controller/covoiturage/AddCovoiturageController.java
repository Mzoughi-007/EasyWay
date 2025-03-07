package tn.esprit.controller.covoiturage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.covoiturage.Posts;
import tn.esprit.services.covoiturage.ServicePosts;
import tn.esprit.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddCovoiturageController implements Initializable {

    @FXML
    private ComboBox<String> departureCity;

    @FXML
    private ComboBox<String> arrivalCity;

    @FXML
    private DatePicker travelDate;

    @FXML
    private TextArea travelDetails;

    @FXML
    private TextField nombreDePlaces;

    @FXML
    private TextField prix;

    @FXML
    private Button ajouter;

    private final ServicePosts servicePosts = new ServicePosts();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (departureCity != null && arrivalCity != null) {
            departureCity.getItems().addAll("Tunis", "Sousse", "Sfax", "Monastir", "Nabeul", "Bizerte",
                    "Gabès", "Gafsa", "Kairouan", "Mahdia", "Djerba", "Tozeur", "Zarzis", "Ben Arous",
                    "Ariana", "Manouba", "Beja", "Jendouba", "Kef", "Siliana", "Kasserine", "Sidi Bouzid",
                    "Médenine", "Tataouine");
            arrivalCity.getItems().addAll(departureCity.getItems());
        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        if (departureCity.getValue() == null || arrivalCity.getValue() == null ||
                travelDate.getValue() == null || travelDetails.getText().isEmpty() ||
                nombreDePlaces.getText().isEmpty() || prix.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (departureCity.getValue().equals(arrivalCity.getValue())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La ville de départ et la ville d'arrivée ne peuvent pas être identiques !");
            return;
        }

        if (travelDate.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de départ ne peut pas être dans le passé !");
            return;
        }

        int idUserConnecte = SessionManager.getInstance().getId_user();
        if (idUserConnecte == 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Vous devez être connecté pour ajouter une annonce !");
            return;
        }

        int places;
        double prixValue;

        try {
            places = Integer.parseInt(nombreDePlaces.getText());
            prixValue = Double.parseDouble(prix.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix et le nombre de places doivent être des valeurs valides !");
            return;
        }

        if (places <= 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de places doit être supérieur à zéro !");
            return;
        }



        if (prixValue < 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le prix ne peut pas être négatif !");
            return;
        }

        Posts newPost = new Posts(
                0,
                idUserConnecte,
                departureCity.getValue(),
                arrivalCity.getValue(),
                Date.valueOf(travelDate.getValue()),
                travelDetails.getText(),
                prixValue,
                places
        );

        servicePosts.add(newPost);
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre annonce a été ajoutée !");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Viewpost.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ajouter.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("View Posts");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de la page de visualisation des annonces.");
        }

        departureCity.setValue(null);
        arrivalCity.setValue(null);
        travelDate.setValue(null);
        travelDetails.clear();
        nombreDePlaces.clear();
        prix.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void gotooffres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Choix.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
