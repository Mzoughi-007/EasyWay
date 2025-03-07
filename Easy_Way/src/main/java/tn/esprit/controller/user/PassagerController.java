package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.models.user.Passager;
import tn.esprit.models.user.User;
import tn.esprit.services.user.ServicePassager;

import java.io.IOException;

public class PassagerController {


    @FXML
    private Button createAccountButton;

    private ServicePassager passagerService;
    private User user; // Stocke l'utilisateur récupéré de SignUp

    public PassagerController() {
        this.passagerService = new ServicePassager();
    }

    // ✅ Méthode pour recevoir l'utilisateur depuis SignUpController
    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void initialize() {
        createAccountButton.setOnAction(event -> ajouterPassager());
    }

    private void ajouterPassager() {
        if (user == null) {
            afficherAlerte("Erreur", "Aucun utilisateur défini !");
            return;
        }

        Passager passager = new Passager(
                user.getId_user(),
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getMot_de_passe(),
                user.getTelephonne(),
                user.getPhoto_profil(),
                0 // Initialisation du nombre de trajets effectués à 0
        );

        try {
            passagerService.add(passager);
            afficherAlerte("Succès", "Passager ajouté avec succès !");
        } catch (Exception e) {
            afficherAlerte("Erreur", "L'ajout du passager a échoué : " + e.getMessage());
        }
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}