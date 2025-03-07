package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.models.user.User;
import tn.esprit.services.user.ServiceUser;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextField EmailField, MotDePasseField, NomField, PrenomField, confirmMdpField, telephonneField;

    @FXML
    private ImageView PhotoProfil, logo;

    @FXML
    private Button createAccountButton;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().addAll("Passager", "Conducteur");
        roleChoiceBox.setValue("Passager"); // Valeur par défaut
    }

    @FXML
    void SignUp(ActionEvent event) {
        ServiceUser su = new ServiceUser();


        String nom = NomField.getText().trim();
        String prenom = PrenomField.getText().trim();
        String email = EmailField.getText().trim();
        String mdp = MotDePasseField.getText();
        String confirmMdp = confirmMdpField.getText();
        String telephoneText = telephonneField.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confirmMdp.isEmpty() || telephoneText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'email n'est pas valide.");
            return;
        }

        if (!isValidPassword(mdp)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le mot de passe doit contenir au moins 8 caractères, une majuscule et un chiffre.");
            return;
        }

        if (!mdp.equals(confirmMdp)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        if (!isValidPhoneNumber(telephoneText)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le numéro de téléphone est invalide.");
            return;
        }

        String selectedRole = roleChoiceBox.getValue();
        if (selectedRole == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un rôle.");
            return;
        }

        User.Role role = selectedRole.equals("Passager") ? User.Role.Passager : User.Role.Conducteur;
        int telephone = Integer.parseInt(telephoneText);
        String hashedMdp = PasswordHash(mdp);

        // Création du user
        User newUser = new User(nom, prenom, email, hashedMdp, telephone, "photo", role);
        su.add(newUser);

        // Récupération de l'ID après insertion
        int idUser = su.getLastInsertedId();
        newUser.setId_user(idUser); // ✅ Fixer l'ID ici

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès !");

        // Redirection
        redirectToRolePage(newUser, event);
    }

    private void redirectToRolePage(User user, ActionEvent event) {
        try {
            String fxmlPage = user.getRole() == User.Role.Conducteur ? "/user/Conducteur.fxml" : "/user/Passager.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPage));
            Parent root = loader.load();

            if (user.getRole() == User.Role.Conducteur) {
                ConducteurController conducteurController = loader.getController();
                conducteurController.setUser(user); // ✅ Correction ici
            } else {
                PassagerController controller = loader.getController();
                controller.setUser(user);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}$";
        return Pattern.matches(passwordRegex, password);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8}");
    }

    public static String PasswordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : rbt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    void RedirectToSignIn(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/SignIn.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}