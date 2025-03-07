package tn.esprit.controller.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class VerifyCodeController {

    @FXML
    private TextField entrercode;

    @FXML
    private Button verifieremail;

    private String generatedCode;
    private String email; // ✅ Stocker l'email reçu

    public void setGeneratedCode(String code) {
        this.generatedCode = code;
    }

    public void setEmail(String email) {
        this.email = email;
        System.out.println("Email reçu dans VerifyCodeController : " + email); // ✅ Vérification
    }

    @FXML
    void checkcode(ActionEvent event) {
        String enteredCode = entrercode.getText().trim();

        if (enteredCode.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer le code reçu.");
            return;
        }

        if (enteredCode.equals(generatedCode)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Code correct ! Vous pouvez réinitialiser votre mot de passe.");

            // ✅ Transmettre l'email à la page suivante
            RedirectToNewPass(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Code incorrect. Réessayez.");
        }
    }

    private void RedirectToNewPass(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/NewPass.fxml"));
            Parent root = loader.load();

            // ✅ Passer l'email à NewPassController
            NewPassController newPassController = loader.getController();
            newPassController.setEmail(email);

            // Changer de scène
            Stage stage = (Stage) verifieremail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de réinitialisation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
