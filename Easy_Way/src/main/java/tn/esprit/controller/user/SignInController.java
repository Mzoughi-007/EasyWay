package tn.esprit.controller.user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.models.user.User;
import tn.esprit.services.user.ServiceUser;

import java.io.IOException;
import tn.esprit.util.SessionManager;



public class SignInController {

    @FXML
    private PasswordField MdpField;

    @FXML
    private TextField emailField;

    @FXML
    private Button signInButton;

    private ServiceUser serviceUser = new ServiceUser();

    private String email;

    public void setEmail(String email) {
        this.email = email;
        System.out.println("Email reçu dans SignInController : " + email);
    }

    @FXML
    void SignIn(ActionEvent event) {
        String email = emailField.getText().trim();
        String mdp = MdpField.getText().trim();

        if (email.isEmpty() || mdp.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.", Alert.AlertType.ERROR); // Utilisation d'AlertType.ERROR
            return;
        }

        ServiceUser serviceUser = new ServiceUser();
        User myUser = serviceUser.getUserByEmail(email);

        if (myUser == null) {
            showAlert("Erreur", "Utilisateur non trouvé. Vérifiez votre email.", Alert.AlertType.ERROR); // Utilisation d'AlertType.ERROR
        } else if (!myUser.getMot_de_passe().equals(mdp)) {
            showAlert("Erreur", "Mot de passe incorrect.", Alert.AlertType.ERROR); // Utilisation d'AlertType.ERROR
        } else {
            showAlert("Succès", "Connexion réussie !", Alert.AlertType.INFORMATION); // Utilisation d'AlertType.INFORMATION pour succès
            // Stocker l'utilisateur dans la session
            SessionManager.getInstance().setId_user(myUser.getId_user());
            System.out.println(myUser);
            try {
                if(myUser.getRole()== User.Role.Admin){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UsersList.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) signInButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Choix.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) signInButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                showAlert("Erreur", "Impossible de charger l'interface utilisateur.", Alert.AlertType.ERROR); // Utilisation d'AlertType.ERROR
            }
        }
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type); // Utilisation du type passé en paramètre
        alert.setTitle(title);
        alert.setHeaderText(null); // Supprime l'en-tête
        alert.setContentText(content); // Définit le message de contenu
        alert.showAndWait(); // Affiche l'alerte et attend la fermeture
    }


    @FXML
    void RedirectToSignUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void RedirectToResetPass(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/ResetPass.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}