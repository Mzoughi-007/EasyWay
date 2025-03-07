package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import tn.esprit.services.user.ServiceUser;

import java.io.IOException;

public class NewPassController {

    @FXML
    private PasswordField confirmpass;

    @FXML
    private PasswordField newpasswd;

    @FXML
    private Button resetmdp;

     ServiceUser  userService = new ServiceUser();

    private String email; // Stocke l'email récupéré de la page précédente

    // Méthode pour définir l'email avant d'afficher la scène
    public void setEmail(String email) {
        this.email = email;
    }

    @FXML
    public void initialize() {
        resetmdp.setOnAction(event -> updatePassword(event));
    }

    private void updatePassword(ActionEvent event) {
        String newPassword = newpasswd.getText();
        String confirmPassword = confirmpass.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        // Vérifier si l'email a été bien transmis
        if (email == null || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur interne : email introuvable.");
            return;
        }

        // Récupération de l'ID utilisateur via l'email
        int id_user = userService.getUserIdByEmail(email);
        if (id_user == -1) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Utilisateur introuvable.");
            return;
        }

        // Mise à jour du mot de passe
        boolean success = userService.updatePassword(id_user, newPassword);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe mis à jour avec succès.");

            // Redirection vers SignIn.fxml
            redirectToSignIn(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour.");
        }
    }

    public void redirectToSignIn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/SignIn.fxml"));
            Scene signInScene = new Scene(loader.load());

            SignInController controller = loader.getController();
            controller.setEmail(email); // Vérifie que SignInController a bien cette méthode

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(signInScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de SignIn.fxml : " + e.getMessage());
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
