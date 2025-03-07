package tn.esprit.controller.user;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

public class VerifyAccountController {

    @FXML
    private TextField verificationCodeField;

    @FXML
    private Button verifyButton;

    private String email;
    private int correctCode;
    private ServiceUser serviceUser = new ServiceUser();

    public void setEmailAndCode(String email, int code) {
        this.email = email;
        this.correctCode = code;
    }

    @FXML
    void verifyAccount() {
        try {
            int enteredCode = Integer.parseInt(verificationCodeField.getText().trim());

            if (enteredCode == correctCode) {
                // Connexion réussie
                SessionManager.getInstance().setId_user(serviceUser.getUserByEmail(email).getId_user());
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Vérification réussie !");
                closeWindow();
                redirectToProfile();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Code incorrect !");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un code valide.");
        }
    }

    private void redirectToProfile() {
        try {
            Stage stage = (Stage) verifyButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UpdateProfile.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) verifyButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
