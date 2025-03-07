package tn.esprit.controller.covoiturage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import tn.esprit.models.covoiturage.Commentaire;
import tn.esprit.models.covoiturage.Posts;
import tn.esprit.services.covoiturage.ServiceCommentaire;
import tn.esprit.services.covoiturage.ServicePosts;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

import java.io.IOException;

import java.util.List;
import java.util.Optional;

public class Offers {
    @FXML
    private VBox postsContainer;
    @FXML
    private TextField searchField;


    private final ServicePosts servicePosts = new ServicePosts();
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private ServiceUser us = new ServiceUser(); // Service pour gérer les utilisateurs
    private int idUtilisateurConnecte; // Stocke l'ID de l'utilisateur connecté

    // Méthode pour récupérer l'ID utilisateur via son email et l'affecter
    int idUserConnecte = SessionManager.getInstance().getId_user();
    @FXML
    private Button reserverButton;


    @FXML
    public void initialize() {
        afficherPosts();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerParLieuArrivee(newValue);
            searchField.setPrefWidth(200);
            searchField.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        });
    }
    private void filtrerParLieuArrivee(String lieuArrivee) {
        postsContainer.getChildren().clear(); // Nettoyer l'affichage actuel

        List<Posts> filteredPosts;

        if (lieuArrivee == null || lieuArrivee.trim().isEmpty()) {
            // Si le champ est vide, afficher tous les posts
            filteredPosts = servicePosts.getAll();
        } else {
            // Sinon, filtrer en fonction du lieu d'arrivée
            filteredPosts = servicePosts.getAll().stream()
                    .filter(post -> post.getVilleArrivee().toLowerCase().contains(lieuArrivee.toLowerCase()))
                    .toList();
        }

        // Afficher les posts filtrés
        for (Posts post : filteredPosts) {
            VBox postBox = new VBox(10);
            postBox.setStyle("-fx-background-color: #F4EFE2; -fx-padding: 15px; -fx-border-color: #ccc; -fx-border-radius: 10px;");

            Label title = new Label("Offre Covoiturage");
            title.setFont(Font.font("System", FontWeight.BOLD, 18));
            title.setStyle("-fx-text-fill: #000000;");
            title.setMaxWidth(Double.MAX_VALUE);
            title.setAlignment(Pos.CENTER);

            Label villeDepartLabel = new Label("Lieu de départ : " + post.getVilleDepart());
            villeDepartLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            villeDepartLabel.setStyle("-fx-text-fill: #6b0808;");

            Label villeArriveeLabel = new Label("Lieu d’arrivée : " + post.getVilleArrivee());
            villeArriveeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            villeArriveeLabel.setStyle("-fx-text-fill: #6b0808;");

            Label dateLabel = new Label("Date: " + post.getDate().toString());
            dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            dateLabel.setStyle("-fx-text-fill: #6b0808;");

            Label prixLabel = new Label("Prix : " + post.getPrix() + " TND");
            prixLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            prixLabel.setStyle("-fx-text-fill: #6b0808;");

            Label placesLabel = new Label("Nombre de places : " + post.getNombreDePlaces());
            placesLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            placesLabel.setStyle("-fx-text-fill: #6b0808;");

            Button reserverButton = new Button("Réserver");
            reserverButton.setStyle("-fx-background-color: #AA1010; -fx-text-fill: white;");
            reserverButton.setFont(Font.font("System Bold", 12));
            reserverButton.setOnAction(event -> {
                if (post.getNombreDePlaces() <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Champ vide", "nombre des places indisponibles");
                    return;
                }

                // Appel de la méthode Valider pour vérifier le paiement et réserver
//                reservercov cov = new reservercov();
//                cov.Valider(event);

                // Après un paiement réussi, changer de scène
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/reservecov.fxml"));
                try {
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            postBox.getChildren().addAll(
                    title,
                    villeDepartLabel,
                    villeArriveeLabel,
                    dateLabel,
                    prixLabel,
                    placesLabel,
                    reserverButton
            );

            postsContainer.getChildren().add(postBox);
        }
    }


    private void afficherPosts() {
        postsContainer.getChildren().clear();
        List<Posts> postsList = servicePosts.getAll();

        for (Posts post : postsList) {
            VBox postBox = new VBox(10);
            postBox.setStyle("-fx-background-color: #F4EFE2; -fx-padding: 15px; -fx-border-color: #A11010; -fx-border-radius: 30px;");

            Label title = new Label("Offre Covoiturage");
            title.setFont(Font.font("System", FontWeight.BOLD, 18));
            title.setStyle("-fx-text-fill: #000000;");
            title.setMaxWidth(Double.MAX_VALUE);
            title.setAlignment(Pos.CENTER);

            Label descriptionLabel = new Label("Description du trajet: " + post.getMessage());
            descriptionLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            descriptionLabel.setStyle("-fx-text-fill: #6b0808;");
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(600);

            Label villeDepartLabel = new Label("Lieu de départ : " + post.getVilleDepart());
            villeDepartLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            villeDepartLabel.setStyle("-fx-text-fill: #6b0808;");

            Label villeArriveeLabel = new Label("Lieu d’arrivée : " + post.getVilleArrivee());
            villeArriveeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            villeArriveeLabel.setStyle("-fx-text-fill: #6b0808;");

            Label dateLabel = new Label("Date: " + post.getDate().toString());
            dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            dateLabel.setStyle("-fx-text-fill: #6b0808;");

            Label prixLabel = new Label("Prix : " + post.getPrix() + " TND");
            prixLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            prixLabel.setStyle("-fx-text-fill: #6b0808;");

            // Nouveau : Affichage du nombre de places
            Label placesLabel = new Label("Nombre de places : " + post.getNombreDePlaces());
            placesLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            placesLabel.setStyle("-fx-text-fill: #6b0808;");
// Bouton de réservation
            Button reserverButton = new Button("Réserver");
            reserverButton.setStyle("-fx-background-color: #AA1010; -fx-text-fill: white;");
            reserverButton.setFont(Font.font("System Bold", 12));
            reserverButton.setOnAction(event -> {
                if (post.getNombreDePlaces() <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Champ vide", "nombre des places indisponibles");
                    return;
                }

//                // Appel de la méthode Valider pour vérifier le paiement et réserver
//                reservercov cov = new reservercov();
//                cov.Valider(event);

                // Après un paiement réussi, changer de scène
                else {FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/reservecov.fxml"));
                try {
                    Parent root = loader.load();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }}
            });



            // Zone de saisie pour ajouter un commentaire
            TextArea commentInput = new TextArea();
            commentInput.setPromptText("Ajouter un commentaire...");
            commentInput.setWrapText(true);
            commentInput.setPrefHeight(60);
            commentInput.setPrefWidth(639);

            Button submitCommentButton = new Button("Envoyer");
            submitCommentButton.setStyle("-fx-background-color: #E5FEB3; -fx-text-fill: #333;");
            submitCommentButton.setFont(Font.font("System", FontWeight.BOLD, 12));
            submitCommentButton.setOnAction(event -> handleAddComment(event, post.getId_post(), commentInput));

            VBox commentInputBox = new VBox(10, commentInput, submitCommentButton);

            // Conteneur des commentaires
            VBox commentsContainer = new VBox();
            commentsContainer.setSpacing(5);
            commentsContainer.setStyle("-fx-padding: 10px; -fx-background-color: #f4f4f4; -fx-border-color: #bbb; -fx-border-radius: 5px;");

            Label commentsTitle = new Label("Commentaires :");
            commentsTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
            commentsTitle.setStyle("-fx-text-fill: #333;");
            commentsContainer.getChildren().add(commentsTitle);

            List<Commentaire> commentaires = serviceCommentaire.getCommentsByPostId(post.getId_post());
            if (commentaires.isEmpty()) {
                Label noCommentsLabel = new Label("Aucun commentaire pour ce post.");
                noCommentsLabel.setStyle("-fx-text-fill: #888;");
                commentsContainer.getChildren().add(noCommentsLabel);
            } else {
                for (Commentaire commentaire : commentaires) {
                    HBox commentRow = new HBox(10);
                    commentRow.setStyle("-fx-padding: 5px; -fx-alignment: center-left;");

                    Label userNameLabel = new Label(commentaire.getNom());
                    userNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                    userNameLabel.setStyle("-fx-text-fill: black;");

                    String dateString = (commentaire.getDate_creat() != null) ? commentaire.getDate_creat().toString() : "Date inconnue";
                    Label dateLabelComment = new Label("(" + dateString + ")");
                    dateLabelComment.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                    dateLabelComment.setStyle("-fx-text-fill: #888;");

                    Label commentLabel = new Label(commentaire.getContenu());
                    commentLabel.setWrapText(true);
                    commentLabel.setMaxWidth(400);
                    commentLabel.setStyle("-fx-text-fill: #555; -fx-padding: 5px;");

                    HBox commentHeader = new HBox(5, userNameLabel, dateLabelComment);
                    HBox.setHgrow(commentHeader, Priority.ALWAYS); // Étend l'espace disponible

                    VBox commentBox = new VBox(3, commentHeader, commentLabel, new Separator());
                    System.out.println("Utilisateur du commentaire : " + commentaire.getId_user() +
                            " | Utilisateur connecté : " + idUserConnecte);
                    if (commentaire.getId_user() == idUserConnecte) {
                        System.out.println("Ajout des boutons pour l'utilisateur connecté.");

                        Button deleteCommentButton = new Button("❌");
                        deleteCommentButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red;");
                        deleteCommentButton.setOnAction(event -> handleDeleteComment(String.valueOf(commentaire.getId_com()), postBox));

                        Button updateCommentButton = new Button("✏️");
                        updateCommentButton.setStyle("-fx-background-color: transparent; -fx-text-fill: blue;");
                        updateCommentButton.setOnAction(event -> handleUpdateComment(commentaire));

                        Region spacer = new Region();
                        HBox.setHgrow(spacer, Priority.ALWAYS); // Pousse les boutons à droite

                        commentHeader.getChildren().addAll(spacer, updateCommentButton, deleteCommentButton);
                    }


                    commentRow.getChildren().add(commentBox);
                    commentsContainer.getChildren().add(commentRow);
                }
            }


            // Boutons d'actions pour les posts (conditionnés par l'ID utilisateur)
            HBox postActionButtons = new HBox(10);
            postActionButtons.setAlignment(Pos.CENTER_LEFT);
            postActionButtons.setStyle("-fx-padding: 10;");



            // Ajout des éléments au conteneur principal du post

            postBox.getChildren().addAll(
                    title,
                    descriptionLabel,
                    villeDepartLabel,
                    villeArriveeLabel,
                    dateLabel,
                    prixLabel, // Ajout du prix
                    placesLabel, // Ajout du nombre de places
                    reserverButton, // Ajout du bouton Réserver ici
                    commentInputBox,
                    commentsContainer,
                    postActionButtons
            );


            postsContainer.getChildren().add(postBox);
        }

    }

    private void handleDeleteComment(String commentId, VBox postBox) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce commentaire ?");
        alert.setContentText("Cette action est irréversible.");

        ButtonType buttonYes = new ButtonType("Oui");
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            try {
                int id_com = Integer.parseInt(commentId); // Convertir l'ID en entier
                serviceCommentaire.deleteComment(id_com); // Utiliser la bonne méthode
                afficherPosts(); // Rafraîchir l'affichage
                System.out.println("Commentaire supprimé avec succès !");
            } catch (NumberFormatException e) {
                System.out.println("Erreur : ID du commentaire invalide.");
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }


    private void handleAddComment(ActionEvent event, int postId, TextArea commentaireField) {
        String commentaireText = commentaireField.getText();

        if (commentaireText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer un commentaire.");
            return;
        }

        if (!Commentaire.isSafe(commentaireText)) {
            showAlert(Alert.AlertType.ERROR, "Commentaire inapproprié", "Votre commentaire contient des mots interdits. Veuillez le modifier.");
            return;
        }

        // Récupération de l'utilisateur connecté
        int id_user = SessionManager.getInstance().getId_user();
        String nom= SessionManager.getInstance().getUsername(); // Ajout du username
        java.sql.Date date_creat = new java.sql.Date(System.currentTimeMillis());

        // Création de l'objet commentaire avec username
        Commentaire newComment = new Commentaire(postId, id_user, commentaireText, date_creat, nom);
        serviceCommentaire.add(newComment); // Modification pour prendre en compte le username

        commentaireField.clear();
        afficherPosts();

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès !");
    }


    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void handleUpdateComment(Commentaire commentaire) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier le commentaire");

        VBox dialogVbox = new VBox(10);
        dialogVbox.setStyle("-fx-padding: 20px;");

        TextField commentField = new TextField(commentaire.getContenu());

        dialogVbox.getChildren().add(commentField);
        dialog.getDialogPane().setContent(dialogVbox);

        ButtonType updateButton = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButton, cancelButton);

        dialog.setResultConverter(button -> {
            if (button == updateButton) {
                return commentField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newContent -> {
            commentaire.setContenu(newContent);
            serviceCommentaire.update(commentaire);  // Remplacer 'updateComment' par 'update'
            afficherPosts(); // Rafraîchir l'affichage
            System.out.println("Commentaire modifié avec succès !");
        });
    }





//    @FXML
//
//    private void handleReserverClick(ActionEvent event, int postId) {
//        try {
//            // Charger le fichier FXML de la réservation
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/reservecov.fxml"));
//            Parent reserverView = loader.load();
//
//            // Récupérer le contrôleur de la page de réservation
//            reservercov reserverController = loader.getController();
//            // Assuming you have a method to get a post by ID in your service class
//            ServicePosts servicePosts = new ServicePosts();
//            Posts selectedPost = servicePosts.getPostById(postId);
//
//            if (selectedPost != null) {
//                reserverController.setSelectedPost(selectedPost); // ✅ Pass the full object
//            } else {
//                System.out.println("Erreur : Post introuvable !");
////            }
//// Passer l'ID du post au contrôleur
//
//            // Obtenir la scène actuelle et la mettre à jour
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            Scene scene = new Scene(reserverView);
//            stage.setScene(scene);
//            stage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//private void openReservationPage(Posts post) {
//    try {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservecov.fxml"));
//        Parent root = loader.load();
//
//        reservercov controller = loader.getController();
//        controller.initData(post);
//
//        Stage stage = new Stage();
//        stage.setScene(new Scene(root));
//        stage.showAndWait(); // Attend la fermeture de la fenêtre avant de rafraîchir
//
//        post.setNombreDePlaces(post.getNombreDePlaces() - 1);
//        afficherPosts(); // Rafraîchir l'affichage après la réservation
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
//}
//
//
//    private void handleReservationButton(Posts post) {
//        if (post.getNombreDePlaces() == 0) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Réservation impossible");
//            alert.setHeaderText(null);
//            alert.setContentText("Il n'y a plus de places disponibles pour ce covoiturage.");
//            alert.showAndWait();
//        } else {
//            openReservationPage(post);
//        }
//    }

    @FXML
    private void gotooffres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Choix.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}





