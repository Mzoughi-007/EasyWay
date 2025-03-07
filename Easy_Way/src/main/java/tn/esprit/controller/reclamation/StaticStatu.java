package tn.esprit.controller.reclamation;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.models.reclamation.categories;
import tn.esprit.util.MyDataBase;
import tn.esprit.controller.reclamation.ModifierReclamation;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class StaticStatu implements Initializable {

    @FXML
    private PieChart piechat;
    @FXML
    private ListView<String> listdesstatu;
    @FXML
    private Label statutLabel;  // Ajouter un label pour afficher le statut cliqué

    private Connection connection = MyDataBase.getInstance().getCnx();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPieChartData();
        setupPieChartClickListener();
    }

    private void loadPieChartData() {
        try {
            // Récupérer le nombre total de réclamations
            String totalQuery = "SELECT COUNT(*) FROM reclamation";
            PreparedStatement totalStmt = connection.prepareStatement(totalQuery);
            ResultSet totalRs = totalStmt.executeQuery();
            int totalReclamations = totalRs.next() ? totalRs.getInt(1) : 1; // Éviter division par zéro

            // Récupérer le nombre de réclamations par statut
            String query = "SELECT statu, COUNT(*) FROM reclamation GROUP BY statu";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            // Ajouter les données au PieChart
            while (rs.next()) {
                String statut = rs.getString("statu");
                int count = rs.getInt(2);
                double percentage = (count * 100.0) / totalReclamations;

                PieChart.Data slice = new PieChart.Data(statut + " (" + String.format("%.1f", percentage) + "%)", count);
                piechat.getData().add(slice);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des données du PieChart !");
        }
    }

    @FXML
    private void setupPieChartClickListener() {
        // Ajouter un événement de clic sur le PieChart
        for (PieChart.Data data : piechat.getData()) {
            data.getNode().setOnMouseClicked(event -> {
                String label = data.getName();
                String statut = label.split("\\(")[0].trim();  // Extraire uniquement le statut avant la parenthèse
                updateLabelAndLoadEmails(statut);  // Mettre à jour le label et charger les emails
            });
        }
    }

    private void updateLabelAndLoadEmails(String statut) {
        statutLabel.setText("Les Emails du statu: " + statut);  // Mettre à jour le texte du label
        loadEmailsByStatus(statut);  // Charger les emails pour le statut
    }



    private void loadEmailsByStatus(String statut) {
        ObservableList<String> emailList = FXCollections.observableArrayList();

        try {
            // Requête pour récupérer les emails des réclamations selon le statut
            String query = "SELECT email FROM reclamation WHERE statu = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, statut);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String email = rs.getString("email");
                emailList.add(email); // Ajouter directement les emails
            }

            // Mettre à jour la ListView avec les emails
            listdesstatu.setItems(emailList);

            listdesstatu.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                // Créer un HBox pour structurer l'affichage
                                HBox hbox = new HBox(10);  // Espacement entre les éléments

                                // Créer un Label avec une largeur fixe
                                Label emailLabel = new Label(item);
                                emailLabel.setMinWidth(200); // Largeur minimale pour éviter les décalages
                                emailLabel.setMaxWidth(200); // Largeur maximale pour uniformiser
                                emailLabel.setStyle("-fx-border-color: transparent; -fx-padding: 5px;"); // Style

                                // Créer un espaceur flexible pour pousser les boutons à droite
                                Region spacer = new Region();
                                HBox.setHgrow(spacer, Priority.ALWAYS); // Le spacer prend l'espace restant

                                // Créer le bouton "Modifier"
                                Button modifyButton = new Button("Modifier");
                                // Style pour le bouton "Modifier"
                                modifyButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                modifyButton.setOnMouseEntered(e -> modifyButton.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
                                modifyButton.setOnMouseExited(e -> modifyButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;"));

                                modifyButton.setOnAction(e -> {
                                    try {
                                        // Récupérer les détails de la réclamation associée à l'email sélectionné
                                        String query = "SELECT r.id, r.sujet, r.description, r.date_creation, r.statu, c.id AS categorieId " +
                                                "FROM reclamation r " +
                                                "JOIN categorie c ON r.categorieId = c.id " +
                                                "WHERE r.email = ?";

                                        PreparedStatement pstmt = connection.prepareStatement(query);
                                        pstmt.setString(1, item);
                                        ResultSet rs = pstmt.executeQuery();

                                        if (rs.next()) {
                                            int id = rs.getInt("id");
                                            String sujet = rs.getString("sujet");
                                            String description = rs.getString("description");
                                            int categorieId = rs.getInt("categorieId");
                                            String date_creation = rs.getString("date_creation");
                                            String statu = rs.getString("statu");

                                            // Charger la fenêtre ModifierReclamation.fxml
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/ModifierReclamation.fxml"));
                                            Parent root = loader.load();

                                            // Récupérer le contrôleur et passer les données
                                            ModifierReclamation controller = loader.getController();
                                            controller.setReclamationDetails(id, sujet, description, categorieId, date_creation, statu);

                                            // Afficher la nouvelle fenêtre
                                            Stage stage = new Stage();
                                            stage.setScene(new Scene(root));
                                            stage.setTitle("Modifier Réclamation");
                                            stage.show();
                                        }
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                });



                                // Créer le bouton "Afficher"
                                Button showButton = new Button("Afficher");
                                showButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                                showButton.setOnMouseEntered(e -> showButton.setStyle("-fx-background-color: #1e7e34; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
                                showButton.setOnMouseExited(e -> showButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;"));
                                showButton.setOnAction(e -> {
                                    // Affichage des détails de l'email
                                    Platform.runLater(() -> showEmailDetails(item));
                                });

                                // Ajouter les éléments dans le HBox
                                hbox.getChildren().addAll(emailLabel, spacer, showButton, modifyButton);
                                hbox.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 5px;");

                                setGraphic(hbox); // Appliquer le HBox à la cellule
                            }
                        }
                    };
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des emails !");
        }
    }

    private void showEmailDetails(String email) {
        try {
            // Requête corrigée pour récupérer les détails de la réclamation associée à l'email
            String query = "SELECT r.sujet, r.description, r.date_creation, r.statu, " +
                    "c.id AS categorieId, c.type AS categorieType " +
                    "FROM reclamation r " +
                    "JOIN categorie c ON r.categorieId = c.id " +
                    "WHERE r.email = ?";

            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Supposons qu'il y ait une seule réclamation par email
                String sujet = rs.getString("sujet");
                String description = rs.getString("description");
                String categorieType = rs.getString("categorieType");  // Le type de la catégorie
                String date_creation = rs.getString("date_creation");
                String statu = rs.getString("statu");

                // Création de l'objet categorie
                int categorieId = rs.getInt("categorieId");  // Récupération de l'ID de la catégorie
                categories cat = new categories(categorieId, categorieType);  // Création de l'objet catégories

                // Vérification dans la console
                System.out.println("Catégorie ID : " + categorieId);
                System.out.println("Catégorie Type : " + categorieType);
                System.out.println("Sujet : " + sujet);
                System.out.println("Description : " + description);
                System.out.println("Date : " + date_creation);
                System.out.println("Statut : " + statu);
                System.out.println("Catégorie : " + cat.toString());

                // Construire le message à afficher
                String details = "📌   Sujet : " + sujet + "\n"
                        + "📝   Description : " + description + "\n"
                        + "📂   Catégorie : " + categorieType + "\n"
                        + "📅   Date : " + date_creation + "\n"
                        + "⏳    Statut : " + statu;

                // Afficher une boîte de dialogue avec les informations
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Détails de la Réclamation");
                alert.setHeaderText("Informations sur la réclamation de l'email : " + email);
                alert.setContentText(details);

                // Personnalisation de l'Alert
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/reclamation/css/grid.css").toExternalForm());

                alert.showAndWait();
            } else {
                // Aucun résultat trouvé
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Réclamation introuvable");
                alert.setHeaderText(null);
                alert.setContentText("Aucune réclamation trouvée pour cet email.");

                // Personnalisation de l'Alert
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("/reclamation/css/grid.css").toExternalForm());

                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération des détails de la réclamation !");
        }
    }


    public void gotoliste(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/reclamation/CardView.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}


