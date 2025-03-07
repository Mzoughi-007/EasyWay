package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.user.User;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Button changePhotoButton;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private ImageView profileImageView;

    @FXML
    private Button saveButton;

    @FXML
    private TextField telephoneField;

    private final ServiceUser userService = new ServiceUser();
    private User currentUser;
    @FXML
    private ImageView image;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Charger l'utilisateur actuel (supposons que l'ID est 1 pour l'exemple)
        currentUser = userService.getById(SessionManager.getInstance().getId_user());
        System.out.println(currentUser);
        if (currentUser != null) {
            loadUserData();
        }


    }

    private void loadUserData() {
        nomField.setText(currentUser.getNom());
        prenomField.setText(currentUser.getPrenom());
        emailField.setText(currentUser.getEmail());
        telephoneField.setText(String.valueOf(currentUser.getTelephonne())); // Conversion sécurisée en String

        // Charger l'image de profil
        if (currentUser.getPhoto_profil() != null && !currentUser.getPhoto_profil().isEmpty()) {
            profileImageView.setImage(new Image(new File(currentUser.getPhoto_profil()).toURI().toString()));
        }
    }

    @FXML
    void changeProfilePhoto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));
            currentUser.setPhoto_profil(selectedFile.getAbsolutePath()); // Sauvegarde du chemin de l'image
        }
    }

    @FXML
    void saveProfileChanges(ActionEvent event) {
        // Vérification des champs obligatoires
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérification du mot de passe si un nouveau mot de passe est fourni
        if (!currentPasswordField.getText().isEmpty() || !newPasswordField.getText().isEmpty()) {
            if (currentPasswordField.getText().isEmpty() || newPasswordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer à la fois l'ancien et le nouveau mot de passe.");
                return;
            }

            if (!currentUser.getMot_de_passe().equals(currentPasswordField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe actuel est incorrect.");
                return;
            }
            // Mise à jour du mot de passe
            currentUser.setMot_de_passe(newPasswordField.getText());
        }

        // Mise à jour des autres informations
        currentUser.setNom(nomField.getText());
        currentUser.setPrenom(prenomField.getText());
        currentUser.setEmail(emailField.getText());

        // Validation du numéro de téléphone (doit être un entier)
        try {
            currentUser.setTelephonne(Integer.parseInt(telephoneField.getText()));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone doit être un nombre valide.");
            return;
        }

        // Mise à jour du profil dans la base de données
        try {
            userService.update(currentUser);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Profil mis à jour avec succès !");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour du profil. Veuillez réessayer.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void RedirectToSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Choix.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}