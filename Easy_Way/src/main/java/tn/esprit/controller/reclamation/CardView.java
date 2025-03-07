package tn.esprit.controller.reclamation;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.itextpdf.kernel.colors.DeviceRgb;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.models.reclamation.reclamations;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import tn.esprit.models.user.User;
import tn.esprit.services.reclamation.reclamationService;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.MyDataBase;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.Document;
import tn.esprit.util.SessionManager;


public class CardView {

    @FXML
    private ImageView profileImage;
    public Button suppbtn;
    public Button modfbtn;
    private final Connection connection = MyDataBase.getInstance().getCnx();
    @FXML
    private Label lblMessage;
    @FXML
    private GridPane gridPaneReclamations;
    @FXML
    private Button trier;
    @FXML
    private TextField txtChercher;  // Champ de texte pour la recherche
    @FXML
    private Button btnChercher;
    @FXML
    private ComboBox<String> comboBoxTrier;
    @FXML
    private ComboBox<String> comboBoxChercher;
    @FXML
    private Button stqButton;
    @FXML
    private Label username;
    @FXML
    private Button pdfButton;
    private final reclamationService reclamationService = new reclamationService(); // ✅ Ajout de cette ligne
    private ServiceUser su = new ServiceUser();


    public void gotoAjoutReclamation(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/reclamation/ajoutReclamation.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void gotoStatestique(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/reclamation/staticStatu.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
        User currentUser = su.getById(SessionManager.getInstance().getId_user());
        profileImage.setImage(new Image(new File(currentUser.getPhoto_profil()).toURI().toString()));
        username.setText(currentUser.getNom()+" "+currentUser.getPrenom());
        System.out.println("Initialisation de l'interface...");
        afficherReclamations(); // Appel automatique de l'affichage des réclamations
        comboBoxTrier.getItems().addAll("email", "sujet", "description", "date");
        comboBoxChercher.getItems().addAll("email", "sujet", "description", "date");
        comboBoxTrier.setOnAction(event -> trierReclamations());

        // Ajouter un listener au champ de recherche
        txtChercher.textProperty().addListener((observable, oldValue, newValue) -> {
            chercherReclamation(new ActionEvent()); // Appel automatique de la fonction
        });

        // Ajouter un listener au ComboBox pour actualiser si l'utilisateur change de critère
        comboBoxChercher.valueProperty().addListener((observable, oldValue, newValue) -> {
            chercherReclamation(new ActionEvent()); // Appel automatique de la fonction
        });
    }


    @FXML
    private void afficherReclamations() {
        List<reclamations> reclamations = reclamationService.getAllReclamationsSansId();
        remplirGridPane(reclamations);

    }


    // Assurez-vous que cette variable est bien reliée à votre FXML

    public void remplirGridPane(List<reclamations> reclamations) {
        gridPaneReclamations.getChildren().clear(); // Nettoyer les anciennes données

        // 🔹 Création de la ligne d'en-tête
        Label headerEmail = new Label("Email");
        Label headerSujet = new Label("Sujet");
        Label headerDescription = new Label("Description");
        Label headerCategorie = new Label("Catégorie");
        Label headerDate = new Label("Date de création");
        Label headerStatut = new Label("Statut");
        Label headerAction = new Label("Action");

        // 🔹 Appliquer un style en gras pour les titres
        String headerStyle = "-fx-font-weight: bold; -fx-font-size: 14px;";
        headerEmail.setStyle(headerStyle);
        headerSujet.setStyle(headerStyle);
        headerDescription.setStyle(headerStyle);
        headerCategorie.setStyle(headerStyle);
        headerDate.setStyle(headerStyle);
        headerStatut.setStyle(headerStyle);
        headerAction.setStyle(headerStyle);
        // 🔹 Ajouter la ligne d'en-tête à la première ligne du `GridPane`
        gridPaneReclamations.add(headerEmail, 0, 0);
        gridPaneReclamations.add(headerSujet, 1, 0);
        gridPaneReclamations.add(headerDescription, 2, 0);
        gridPaneReclamations.add(headerCategorie, 3, 0);
        gridPaneReclamations.add(headerDate, 4, 0);
        gridPaneReclamations.add(headerStatut, 5, 0);
        gridPaneReclamations.add(headerAction, 6, 0);

        int row = 1; // Ligne de départ après l'en-tête

        for (reclamations rec : reclamations) {
            Label labelEmail = new Label(rec.getEmail());
            Label labelSujet = new Label(rec.getSujet());
            Label labelDescription = new Label(rec.getDescription());
            Label labelCategorie = new Label(rec.getCategorie().getType());
            Label labelDate = new Label(rec.getDate_creation());
            Label labelStatut = new Label(rec.getStatu());

            System.out.println("ID de la réclamation récupérée : " + rec.getId());

            // 🔴 Bouton de suppression
            Button suppbtn = new Button("Supprimer");
            suppbtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 3px 7px;");
            suppbtn.setUserData(rec.getId());

            // 🔵 Bouton de modification
            Button modfbtn = new Button("Modifier");
            modfbtn.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 3px 7px;");
            modfbtn.setUserData(rec.getId());

            suppbtn.setOnAction(e -> {
                System.out.println("Bouton Supprimer cliqué pour ID : " + rec.getId());
                confirmerSuppression(rec.getId());
            });

// ✅ Correction ici
            modfbtn.setOnAction(e -> {
                System.out.println("Bouton Modifier cliqué pour ID : " + rec.getId());
                modifierReclamation(e);  // Passez l'événement ici
            });


            // 🔹 Ajouter les éléments au GridPane
            gridPaneReclamations.add(labelEmail, 0, row);
            gridPaneReclamations.add(labelSujet, 1, row);
            gridPaneReclamations.add(labelDescription, 2, row);
            gridPaneReclamations.add(labelCategorie, 3, row);
            gridPaneReclamations.add(labelDate, 4, row);
            gridPaneReclamations.add(labelStatut, 5, row);

            HBox buttonBox = new HBox(6);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(suppbtn, modfbtn);
            gridPaneReclamations.add(buttonBox, 6, row);

            row++; // Passer à la ligne suivante
        }
    }




    public void supprimerReclamation(ActionEvent actionEvent) {
        // Récupérer le bouton qui a déclenché l'événement
        Button supprimerButton = (Button) actionEvent.getSource();

        // Récupérer l'ID de la réclamation à partir de l'attribut 'userData' du bouton
        Integer reclamationId = (Integer) supprimerButton.getUserData();
        if (reclamationId == null) {
            lblMessage.setText("Aucune réclamation sélectionnée.");
            return;
        }

        // Création de l'alerte de confirmation avant de supprimer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette réclamation ?");
        alert.setContentText("Cette action est irréversible.");

        // Affichage de l'alerte et gestion de la réponse
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur confirme, on tente de supprimer la réclamation
                supprimerReclamationDeBase(reclamationId);  // Utiliser l'ID pour supprimer
            } else {
                // Si l'utilisateur annule, on affiche un message de cancellation
                lblMessage.setText("Suppression annulée.");
            }
        });
    }



    // 🔴 Confirmation de suppression avant d'exécuter la suppression
    private void confirmerSuppression(int id) {
        System.out.println("🟡 Confirmation de suppression pour ID : " + id);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cette réclamation ?");
        alert.setContentText("Cette action est irréversible.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("✅ Suppression confirmée pour ID : " + id);
                supprimerReclamationDeBase(id);  // Appeler supprimerReclamationDeBase directement avec l'ID
            } else {
                System.out.println("Suppression annulée.");
            }
        });
    }


    // Méthode qui effectue la suppression de la réclamation dans la base de données
    private void supprimerReclamationDeBase(int id) {
        String deleteQuery = "DELETE FROM reclamation WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);

            // Exécution de la requête et vérification du nombre de lignes affectées
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                lblMessage.setText("Réclamation supprimée avec succès.");
                // Vous pouvez aussi mettre à jour l'affichage de la liste des réclamations ici
                afficherReclamations();  // Recharger les réclamations après suppression
            } else {
                lblMessage.setText("Aucune réclamation trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            lblMessage.setText("Erreur lors de la suppression de la réclamation.");
        }
    }


    public void modifierReclamation(ActionEvent actionEvent) {
        // Récupérer l'ID de la réclamation à partir du bouton cliqué
        Button clickedButton = (Button) actionEvent.getSource();
        int reclamationId = (int) clickedButton.getUserData(); // L'ID stocké dans le bouton

        try {
            // Récupérer les détails de la réclamation depuis la base de données
            String query = "SELECT id, categorieId, email, sujet, description, statu, date_creation FROM reclamation WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, reclamationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Charger la page ModifierReclamation.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/ModifierReclamation.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur de la page de modification
                ModifierReclamation controller = loader.getController();

                // Envoyer les données récupérées au contrôleur
                controller.setReclamationDetails(
                        reclamationId,
                        rs.getString("sujet"),
                        rs.getString("description"),
                        rs.getInt("categorieId"),
                        rs.getString("date_creation"),
                        rs.getString("statu")
                );

                // Afficher la nouvelle scène
                Stage stage = (Stage) gridPaneReclamations.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                lblMessage.setText("Réclamation non trouvée !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            lblMessage.setText("Erreur avec la base de données !");
        } catch (IOException e) {
            e.printStackTrace();
            lblMessage.setText("Erreur de chargement de la page !");
        }
    }


    @FXML
    public void trierReclamations() {
        String critere = comboBoxTrier.getValue();

        if (critere == null) {
            System.out.println("Veuillez sélectionner un critère de tri.");
            return;
        }

        List<reclamations> reclamationsList = reclamationService.getAllReclamationsSansId();

        for (reclamations obj : reclamationsList) {
            System.out.println("Type de l'objet : " + obj.getClass().getName());
        }

        Comparator<reclamations> comparator = switch (critere) {
            case "email" -> Comparator.comparing(reclamations::getEmail);
            case "sujet" -> Comparator.comparing(reclamations::getSujet);
            case "description" -> Comparator.comparing(reclamations::getDescription);
            case "catégorie" -> Comparator.comparing(r -> r.getCategorie().getType());
            case "date" -> Comparator.comparing(r -> LocalDate.parse(r.getDate_creation()));
            default -> null;
        };

        if (comparator != null) {
            Collections.sort(reclamationsList, comparator);
            remplirGridPane(reclamationsList);
        } else {
            System.out.println("Critère de tri non valide.");
        }
    }



    @FXML
    public void chercherReclamation(ActionEvent event) {
        String searchText = txtChercher.getText().trim().toLowerCase();
        String critere = comboBoxChercher.getValue();

        List<reclamations> reclamationsList = reclamationService.getAllReclamationsSansId();

        // Si la recherche est vide ou aucun critère sélectionné, afficher toutes les réclamations
        if (searchText.isEmpty() || critere == null) {
            remplirGridPane(reclamationsList);
            return;
        }

        // Filtrer les réclamations en fonction du critère choisi
        List<reclamations> filteredReclamations = reclamationsList.stream()
                .filter(r -> {
                    switch (critere) {
                        case "email":
                            return r.getEmail().toLowerCase().contains(searchText);
                        case "sujet":
                            return r.getSujet().toLowerCase().contains(searchText);
                        case "description":
                            return r.getDescription().toLowerCase().contains(searchText);
                        case "categorie":
                            return r.getCategorie() != null && r.getCategorie().getType() != null &&
                                    r.getCategorie().getType().toLowerCase().contains(searchText);

                        case "date":
                            return r.getDate_creation() != null && r.getDate_creation().toLowerCase().contains(searchText);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());

        // Mettre à jour l'affichage des réclamations avec les résultats filtrés
        remplirGridPane(filteredReclamations);
    }



    @FXML
    private void refreshTable(ActionEvent event) {
        System.out.println("Rafraîchissement de la table...");
        afficherReclamations();  // Appel de la méthode pour réafficher les réclamations
    }


    public void generatePDF(ActionEvent actionEvent) {
        // Ouvrir le FileChooser pour que l'utilisateur choisisse l'emplacement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            String filePath = file.getAbsolutePath(); // Récupérer le chemin du fichier sélectionné

            // Récupérer toutes les réclamations de la base de données
            List<reclamations> reclamationsList = reclamationService.getAllReclamationsSansId();

            try {
                // Créer un PdfWriter pour écrire le fichier PDF
                PdfWriter writer = new PdfWriter(filePath);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Ajouter un titre
                document.add(new Paragraph("Liste des Réclamations")
                        .setFont(PdfFontFactory.createFont())
                        .setFontSize(16)
                        .setBold()
                        .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER));

                // Créer un tableau avec 5 colonnes (pas d'Action)
                float[] columnWidths = {2, 2, 2, 4, 2};  // On a supprimé la colonne "Action"
                Table table = new Table(columnWidths);

                // Ajouter les en-têtes du tableau (pas d'Action)
                table.addCell(new Cell().setBackgroundColor(new DeviceRgb(169, 169, 169))  // Gris clair
                        .setFontColor(new DeviceRgb(255, 255, 255))  // Blanc
                        .add(new Paragraph("Email")));

                table.addCell(new Cell().setBackgroundColor(new DeviceRgb(169, 169, 169))  // Gris clair
                        .setFontColor(new DeviceRgb(255, 255, 255))  // Blanc
                        .add(new Paragraph("Catégorie")));

                table.addCell(new Cell().setBackgroundColor(new DeviceRgb(169, 169, 169))  // Gris clair
                        .setFontColor(new DeviceRgb(255, 255, 255))  // Blanc
                        .add(new Paragraph("Sujet")));

                table.addCell(new Cell().setBackgroundColor(new DeviceRgb(169, 169, 169))  // Gris clair
                        .setFontColor(new DeviceRgb(255, 255, 255))  // Blanc
                        .add(new Paragraph("Description")));

                table.addCell(new Cell().setBackgroundColor(new DeviceRgb(169, 169, 169))  // Gris clair
                        .setFontColor(new DeviceRgb(255, 255, 255))  // Blanc
                        .add(new Paragraph("Date")));

                // Ajouter les données des réclamations (pas d'Action)
                for (reclamations rec : reclamationsList) {
                    table.addCell(new Cell().add(new Paragraph(rec.getEmail())));
                    table.addCell(new Cell().add(new Paragraph(rec.getCategorie().getType())));
                    table.addCell(new Cell().add(new Paragraph(rec.getSujet())));
                    table.addCell(new Cell().add(new Paragraph(rec.getDescription())));
                    table.addCell(new Cell().add(new Paragraph(rec.getDate_creation())));
                    // Pas d'ajout pour la cellule "Action"
                }

                // Ajouter le tableau au document
                document.add(table);

                // Fermer le document PDF
                document.close();

                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PDF généré");
                alert.setHeaderText("Téléchargement terminé");
                alert.setContentText("Le fichier PDF a été généré avec succès et est disponible ici : " + filePath);
                alert.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de la création du PDF.");

                // Afficher une alerte en cas d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Problème de création PDF");
                alert.setContentText("Une erreur est survenue lors de la création du fichier PDF.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void RedirectToEvent(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventTable.fxml"));
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
    void RedirectToTrajet(ActionEvent event) {

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
    void RedirectToVehicule(ActionEvent event) throws IOException {
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
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

        // 🔹 Afficher l'alerte et attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // 🔹 Si l'utilisateur clique sur "Oui", on procède à la déconnexion
            SessionManager.getInstance().logout();

            // Charger la nouvelle interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserSpace.fxml"));
            Parent root = loader.load();

            // Obtenir la scène et changer l'interface
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }



}


