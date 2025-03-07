package tn.esprit.controller.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
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


public class AddEventController {
    @FXML
    private ImageView imageProfile;
    @FXML
    private Label username;
    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private ChoiceBox<String> lineChoiceBox;

    @FXML
    private TextArea descText;
    @FXML
    private ChoiceBox<StatusEvenement> statusChoiceBox;

    @FXML
    private ChoiceBox<TypeEvenement> typeCB;
    private final ServiceEvenement se = new ServiceEvenement();
    private final ServiceUser su = new ServiceUser();

    @FXML
    public void initialize() {

        User u=su.getById(SessionManager.getInstance().getId_user());
        username.setText(u.getNom()+" "+u.getPrenom());
        imageProfile.setImage(new Image(new File(u.getPhoto_profil()).toURI().toString()));

        // Populate ChoiceBoxes with Enum values
        typeCB.getItems().setAll(TypeEvenement.values());
        statusChoiceBox.getItems().setAll(StatusEvenement.values());
        for (String info: se.getAllLineInfo())
            lineChoiceBox.getItems().add(info);
    }
    @FXML
    void addEventToDB(ActionEvent event) {
        String description = descText.getText();
        TypeEvenement type = typeCB.getValue();
        String depart= lineChoiceBox.getValue().split(" - ")[0];
        String arret = lineChoiceBox.getValue().split(" - ")[1];
        int ligne = se.getLineIdByDepartArret(depart, arret);

        java.time.LocalDate localDateDebut = dateDebutPicker.getValue();
        java.sql.Date dateDebut = java.sql.Date.valueOf(localDateDebut);

        java.time.LocalDate localDateFin = dateFinPicker.getValue();
        if (localDateFin == null) {
            java.sql.Date dateFin = null;
        }
        java.sql.Date dateFin = java.sql.Date.valueOf(localDateFin);

        StatusEvenement status = statusChoiceBox.getValue();

        if ( description.isEmpty() || type == null || lineChoiceBox.getValue().isEmpty() || dateDebut == null || status == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }else if(dateDebut.after(dateFin)){
            showAlert("Erreur", "Il faut que la date debut soit avant la date fin.");
            return;
        }
        Evenements newEvent = new Evenements(0,
                type,
                ligne,
                description,
                dateDebut,
                dateFin,
                status,
                4);
        se.add(newEvent);
        showAlert("Succès", "Événement ajouté avec succès!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void clearAll(ActionEvent event) {
        typeCB.getItems().clear();
        statusChoiceBox.getItems().clear();
        lineChoiceBox.getItems().clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        descText.setText("");
    }
    @FXML
    void goToEventList(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventTable.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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