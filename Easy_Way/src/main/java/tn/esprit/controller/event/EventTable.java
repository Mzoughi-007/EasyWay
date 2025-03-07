package tn.esprit.controller.event;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.Events.Evenements;
import tn.esprit.models.user.User;
import tn.esprit.services.event.ServiceEvenement;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EventTable implements Initializable {
    ServiceUser su = new ServiceUser();
    ServiceEvenement se = new ServiceEvenement();
    List<Evenements> events = se.getAll();
    @FXML
    private GridPane eventGrid;
    @FXML
    private TextField searchField;
    private ObservableList<Evenements> allEvents = FXCollections.observableArrayList();
    private ObservableList<Evenements> filteredEvents = FXCollections.observableArrayList();
    @FXML
    private Label username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = su.getById(SessionManager.getInstance().getId_user());
        username.setText(currentUser.getNom()+" "+currentUser.getPrenom());
        loadEvents(events);
        allEvents.setAll(se.getAll()); // Charger tous les événements
        filteredEvents.setAll(allEvents);

        searchField.textProperty().addListener((observable, oldValue, newValue) ->filterEvents(newValue));
    }

    private void filterEvents(String searchText) {
        filteredEvents.clear();

        if (searchText == null || searchText.isEmpty()) {
            filteredEvents.addAll(allEvents);
        } else {
            String lowerCaseSearchText = searchText.toLowerCase();

            for (Evenements event : allEvents) {
                if (event.getType_evenement().toString().toLowerCase().contains(lowerCaseSearchText) ||
                        event.getDescription().toLowerCase().contains(lowerCaseSearchText) ||
                        event.getDate_debut().toString().contains(lowerCaseSearchText) ||
                        event.getDate_fin().toString().contains(lowerCaseSearchText) ||
                        event.getStatus_evenement().toString().toLowerCase().contains(lowerCaseSearchText)) {

                    filteredEvents.add(event);
                }
            }
            loadEvents(filteredEvents);
        }
    }


    private void loadEvents(List<Evenements> events) {
        eventGrid.getChildren().clear();

        int row = 0;
        int col = 0;
        for (Evenements e : events) {
            VBox card = createEventCard(e);
            eventGrid.add(card, col, row);

            col++;
            if (col == 2) { // 2 carte par ligne
                col = 0;
                row++;
            }
        }
    }

    private VBox createEventCard(Evenements evenement) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 10px; -fx-border-color: #D32F2F; -fx-border-radius: 10px;");

        Text type = new Text("📌 Type: " + evenement.getType_evenement());
        Text description = new Text("📝 Description: " + evenement.getDescription());
        Text dateDebut = new Text("📅 Début: " + evenement.getDate_debut());
        Text dateFin = new Text("📅 Fin: " + evenement.getDate_fin());
        Text ligne = new Text("🚋 Ligne: " + se.getLigneInfo(evenement.getId_ligne_affectee()));
        Text status = new Text("\uD83D\uDEA6 Status: " + evenement.getStatus_evenement());

        Button editButton = new Button("✏ Modifier");
        editButton.setStyle("-fx-background-color: #EF9A9A; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            try {
                handleEdit(evenement);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Button deleteButton = new Button("🗑 Supprimer");
        deleteButton.setStyle("-fx-background-color: #D32F2F; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            try {
                handleDelete(evenement.getId_event());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox actions = new HBox(10, editButton, deleteButton);

        card.getChildren().addAll(type, description, dateDebut, dateFin, ligne, status, actions);
        return card;
    }

    private void handleEdit(Evenements evenements) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/editEventPage.fxml"));
            Parent root = loader.load();
            editPageController editController = loader.getController();
            editController.initData(evenements);

            Stage stage = (Stage) eventGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleDelete(int id_event) throws IOException {
        se.delete(id_event);
        editPageController epc = new editPageController();
        if(se.getById(id_event)==null)
            epc.showAlert("Success", "Événement est supprimée avec succès!");

        else
            epc.showAlert("Error","Error lors de supprission de ce Événement");

        loadEvents(events);

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
    void goToAddForm(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/AddEventForm.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void ClearAll() {
        loadEvents(events);
        searchField.clear();
    }

    @FXML
    void goToDashboard(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventDashboard.fxml"));
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
