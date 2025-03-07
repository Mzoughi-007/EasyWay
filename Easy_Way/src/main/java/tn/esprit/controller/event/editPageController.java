package tn.esprit.controller.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import tn.esprit.models.Events.Evenements;
import tn.esprit.models.Events.StatusEvenement;
import tn.esprit.models.Events.TypeEvenement;
import tn.esprit.models.user.User;
import tn.esprit.services.event.ServiceEvenement;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class editPageController {
    Stage stage;
    Scene scene;
    Parent root;
    @FXML
    private DatePicker dateDebutPicker, dateFinPicker;

    @FXML
    private TextArea descText;

    @FXML
    private ImageView imageProfile;
    @FXML
    private Label username;
    @FXML
    private ChoiceBox<StatusEvenement> statusChoiceBox;

    @FXML
    private ChoiceBox<TypeEvenement> typeChoiceBox;

    @FXML
    private TextField ligneField;
    ServiceUser su = new ServiceUser();
    ServiceEvenement se = new ServiceEvenement();
    Evenements currentEvent = se.getById(27);

    @FXML
    public void initialize() {
        User u =  su.getById(SessionManager.getInstance().getId_user());
        username.setText(u.getNom()+" "+u.getPrenom());
        imageProfile.setImage(new Image(new File(u.getPhoto_profil()).toURI().toString()));
        typeChoiceBox.getItems().setAll(TypeEvenement.values());
        statusChoiceBox.getItems().setAll(StatusEvenement.values());
    }

    public void initData(Evenements event) {
        this.currentEvent = event;

        descText.setText(event.getDescription());
        typeChoiceBox.setValue(event.getType_evenement());
        statusChoiceBox.setValue(event.getStatus_evenement());
        ligneField.setText(String.valueOf(event.getId_ligne_affectee()));
        dateDebutPicker.setValue(event.getDate_debut().toLocalDate());
        dateFinPicker.setValue(event.getDate_fin().toLocalDate());

    }




    @FXML
    void clearAll() {
        typeChoiceBox.getItems().clear();
        statusChoiceBox.getItems().clear();
        ligneField.setText("");
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        descText.setText("");
    }

    @FXML
    void goToEventList(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventTable.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void updateEvent() {
        if(dateDebutPicker.getValue().isAfter(dateFinPicker.getValue())){
            showAlert("Error", "Date invalid!");
            return;
        }
        if ( descText.getText().isEmpty() || typeChoiceBox.getValue() == null || ligneField.getText()==null || dateDebutPicker == null || statusChoiceBox.getValue() == null) {
            showAlert("Error", "Veuillez remplir tous les champs.");
            return;
        }

        Evenements updatedEvent = new Evenements(
                currentEvent.getId_event(),
                typeChoiceBox.getValue(),
                Integer.parseInt(ligneField.getText()),
                descText.getText(),
                java.sql.Date.valueOf(dateDebutPicker.getValue()),
                java.sql.Date.valueOf(dateFinPicker.getValue()),
                statusChoiceBox.getValue(),4);

        se.update(updatedEvent);
        showAlert("Success", "Événement mettre a jour avec succès!");
    }
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventTable.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) descText.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    void RedirectToVehicules(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vehicule/test.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void RedirectToUsers(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UsersList.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void RedirectToLigne(ActionEvent event) {

    }

    @FXML
    void RedirectToReclamation(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/CardView.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void RedirectToTrajet(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserSpace.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

}
