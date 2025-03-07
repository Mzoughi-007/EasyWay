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
import tn.esprit.services.user.EmailService;

import java.io.IOException;
import java.security.SecureRandom;

public class ResetPassController {

    @FXML
    private TextField emailfield;

    @FXML
    private Button envoyercode;

    private final EmailService emailService = new EmailService();
    private String generatedCode;

    /** Générer un code aléatoire à 6 chiffres */
    private String generateRandomCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @FXML
    private void EnvoyerCode(ActionEvent event) {
        String recipient = emailfield.getText().trim();

        if (recipient.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer votre adresse email.");
            return;
        }

        generatedCode = generateRandomCode();
        String subject = "Code de réinitialisation";
        String body = "Votre code de réinitialisation est : " + generatedCode;

        try {
            emailService.sendEmail(recipient, subject, body);
            showAlert(Alert.AlertType.INFORMATION, "Code Envoyé", "Un code a été envoyé à : " + recipient);
            RedirectToVerifyCode(event, recipient); // ✅ Passer email et code
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur d'envoi", "Impossible d'envoyer l'email.");
        }
    }

    private void RedirectToVerifyCode(ActionEvent event, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/VerifyCode.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur et passer les valeurs
            VerifyCodeController verifyController = loader.getController();
            verifyController.setGeneratedCode(generatedCode);
            verifyController.setEmail(email); // ✅ Passer l'email

            // Changer de scène
            Stage stage = (Stage) emailfield.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de vérification.");
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
