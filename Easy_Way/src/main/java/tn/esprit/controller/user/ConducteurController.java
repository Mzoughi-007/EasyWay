package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.user.Conducteur;
import tn.esprit.models.user.User;
import tn.esprit.services.user.ServiceConducteur;
import tn.esprit.services.user.ServiceUser;

import java.io.IOException;

public class ConducteurController {

    @FXML
    private Label component;

    @FXML
    private TextField numeroPermisField;

    @FXML
    private TextField experienceField;

    @FXML
    private Button createAccountButton;

    private final ServiceConducteur conducteurService;
    private final ServiceUser userService;
    private User user; // Stocke l'utilisateur reçu depuis SignUpController

    public ConducteurController() {
        this.conducteurService = new ServiceConducteur();
        this.userService = new ServiceUser();
    }

    // ✅ Méthode pour recevoir l'utilisateur depuis SignUpController
    public void setUser(User user) {
        this.user = user;
        System.out.println("✅ Utilisateur reçu dans ConducteurController : ID = " + user.getId_user());
    }

    @FXML
    private void initialize() {
        createAccountButton.setOnAction(event -> {
            if (user == null) {
                afficherAlerte("Erreur", "Aucun utilisateur reçu ! Veuillez vous inscrire d'abord.");
            } else {
                ajouterConducteur();
            }
        });
    }

    private void ajouterConducteur() {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            afficherAlerte("Erreur", "L'email de l'utilisateur est invalide.");
            return;
        }

        // 🔹 1️⃣ Vérifier si l'utilisateur existe déjà
        int idUser = userService.getUserIdByEmail(user.getEmail());

        if (idUser == 0) { // Si l'utilisateur n'existe pas, on l'ajoute
            userService.add(user);
            idUser = userService.getUserIdByEmail(user.getEmail()); // Récupérer son ID
            System.out.println("✅ Utilisateur ajouté avec ID : " + idUser);
        } else {
            System.out.println("ℹ️ Utilisateur déjà existant avec ID : " + idUser);
        }

        user.setId_user(idUser); // Associer l'ID à l'utilisateur

        // 🔹 2️⃣ Vérification des champs conducteur
        String numeroPermis = numeroPermisField.getText().trim();
        String experience = experienceField.getText().trim();

        if (!validerChamps(numeroPermis, experience)) {
            return;
        }

        // 🔹 3️⃣ Création du conducteur
        Conducteur conducteur = new Conducteur(
                idUser, // Utiliser le même ID utilisateur
                user.getNom(),
                user.getPrenom(),
                user.getEmail(),
                user.getMot_de_passe(),
                user.getTelephonne(),
                user.getPhoto_profil(),
                numeroPermis,
                experience
        );

        // 🔹 4️⃣ Ajout du conducteur
        conducteurService.add(conducteur);
        System.out.println("🚀 Conducteur ajouté avec ID utilisateur : " + idUser);
        afficherAlerte("Succès", "Conducteur ajouté avec succès !");
    }

    private boolean validerChamps(String numeroPermis, String experience) {
        if (numeroPermis.isEmpty() || experience.isEmpty()) {
            afficherAlerte("Validation", "Tous les champs sont obligatoires !");
            return false;
        }

        if (!numeroPermis.matches("\\d{8}")) {
            afficherAlerte("Validation", "Le numéro de permis doit contenir exactement 8 chiffres !");
            return false;
        }

        return true;
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void RedirectToSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/SignIn.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) component.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}