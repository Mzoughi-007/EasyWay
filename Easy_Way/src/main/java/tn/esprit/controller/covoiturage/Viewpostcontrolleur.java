package tn.esprit.controller.covoiturage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.FontWeight;

import javafx.stage.Stage;
import tn.esprit.models.covoiturage.Posts;
import tn.esprit.models.covoiturage.Commentaire;
import tn.esprit.services.covoiturage.ServicePosts;
import tn.esprit.services.covoiturage.ServiceCommentaire;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import javafx.scene.Node;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

public class Viewpostcontrolleur {

    @FXML
    private VBox postsContainer;

    private ServicePosts servicePosts = new ServicePosts();
    private ServiceCommentaire serviceCommentaire = new ServiceCommentaire();
    private ServiceUser us = new ServiceUser(); // Service pour gérer les utilisateurs
    private int idUtilisateurConnecte; // Stocke l'ID de l'utilisateur connecté

    // Méthode pour récupérer l'ID utilisateur via son email et l'affecter
    int idUserConnecte = SessionManager.getInstance().getId_user();

    @FXML
    public void initialize() {
        afficherPosts();
        afficherIds();
    }
    public void afficherIds() {
        // Récupération des IDs depuis SessionManager ou autres classes
        int idUserConnecte = SessionManager.getInstance().getId_user();


        // Affichage pour tester si les IDs sont bien récupérés
        System.out.println("ID utilisateur connecté : " + idUserConnecte);

    }


    private void afficherPosts() {
        postsContainer.getChildren().clear();
        List<Posts> postsList = servicePosts.getAll();

        for (Posts post : postsList) {
            VBox postBox = new VBox(10);
            postBox.setStyle("-fx-background-color: #F4EFE2; -fx-padding: 15px; -fx-border-color: #ccc; -fx-border-radius: 10px;");

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

            // Nouveau : Affichage du prix
            Label prixLabel = new Label("Prix : " + post.getPrix() + " TND");
            prixLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            prixLabel.setStyle("-fx-text-fill: #6b0808;");

            // Nouveau : Affichage du nombre de places
            Label placesLabel = new Label("Nombre de places : " + post.getNombreDePlaces());
            placesLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            placesLabel.setStyle("-fx-text-fill: #6b0808;");

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



            HBox postActionButtons = new HBox(10);
            postActionButtons.setAlignment(Pos.CENTER_LEFT);
            postActionButtons.setStyle("-fx-padding: 10;");

            if (post.getId_user() == idUserConnecte) {
                Button deletePostButton = new Button("Supprimer");
                deletePostButton.setStyle("-fx-background-color: #C10707; -fx-text-fill: #fff;");
                deletePostButton.setOnAction(event -> handleDeletePost(post));

                Button updatePostButton = new Button("Mettre à jour");
                updatePostButton.setStyle("-fx-background-color: #C2D4A9; -fx-text-fill: #fff; -fx-padding: 10 20;");
                updatePostButton.setOnAction(event -> handleUpdatePost(event, post));

                postActionButtons.getChildren().addAll(deletePostButton, updatePostButton);
            }

            postBox.getChildren().addAll(
                    title,
                    descriptionLabel,
                    villeDepartLabel,
                    villeArriveeLabel,
                    dateLabel,
                    prixLabel, // Ajout du prix
                    placesLabel, // Ajout du nombre de places
                    commentInputBox,
                    commentsContainer,
                    postActionButtons
            );

            postsContainer.getChildren().add(postBox);
        }
    }


    private void handleDeletePost(Posts post) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce post ?");
        alert.setContentText("Cette action est irréversible.");

        ButtonType buttonYes = new ButtonType("Oui");
        ButtonType buttonNo = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            servicePosts.delete(post);
            afficherPosts();
            System.out.println("Post supprimé avec succès !");
        } else {
            System.out.println("Suppression annulée.");
        }
    }

    @FXML
    private void handleUpdatePost(ActionEvent event, Posts post) {
        Dialog<Posts> updateDialog = new Dialog<>();
        updateDialog.setTitle("Modifier le post");

        VBox dialogVbox = new VBox(10);
        dialogVbox.setStyle("-fx-padding: 20px;");

        // Champ pour le message
        TextField messageField = new TextField(post.getMessage());

        // Création des ComboBox pour les villes (gouvernorats de Tunisie)
        ComboBox<String> villeDepartCombo = new ComboBox<>();
        ComboBox<String> villeArriveeCombo = new ComboBox<>();

        // Liste des 24 gouvernorats
        List<String> gouvernorats = List.of(
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba",
                "Kairouan", "Kasserine", "Kef", "Mahdia", "Manouba", "Medenine", "Monastir",
                "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine", "Tozeur",
                "Tunis", "Zaghouan", "Kebili"
        );

        villeDepartCombo.getItems().addAll(gouvernorats);
        villeArriveeCombo.getItems().addAll(gouvernorats);

        // Sélectionner la valeur actuelle du post si elle figure dans la liste
        if (gouvernorats.contains(post.getVilleDepart())) {
            villeDepartCombo.setValue(post.getVilleDepart());
        }
        if (gouvernorats.contains(post.getVilleArrivee())) {
            villeArriveeCombo.setValue(post.getVilleArrivee());
        }

        // DatePicker pour la date du post
        DatePicker dateField = new DatePicker(post.getDate().toLocalDate());

        // Ajout des éléments au layout du dialogue
        dialogVbox.getChildren().addAll(
                new Label("Message"), messageField,
                new Label("Lieu de départ"), villeDepartCombo,
                new Label("Lieu d’arrivée"), villeArriveeCombo,
                new Label("Date"), dateField
        );

        updateDialog.getDialogPane().setContent(dialogVbox);

        ButtonType updateButtonType = new ButtonType("Mettre à jour", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        updateDialog.getDialogPane().getButtonTypes().addAll(updateButtonType, cancelButtonType);

        // Récupération et désactivation initiale du bouton de mise à jour
        Node updateButton = updateDialog.getDialogPane().lookupButton(updateButtonType);
        updateButton.setDisable(true);

        // Ajout des écouteurs pour la validation en temps réel
        messageField.textProperty().addListener((obs, oldVal, newVal) ->
                validateUpdateFields(messageField, villeDepartCombo, villeArriveeCombo, dateField, updateButton));
        villeDepartCombo.valueProperty().addListener((obs, oldVal, newVal) ->
                validateUpdateFields(messageField, villeDepartCombo, villeArriveeCombo, dateField, updateButton));
        villeArriveeCombo.valueProperty().addListener((obs, oldVal, newVal) ->
                validateUpdateFields(messageField, villeDepartCombo, villeArriveeCombo, dateField, updateButton));
        dateField.valueProperty().addListener((obs, oldVal, newVal) ->
                validateUpdateFields(messageField, villeDepartCombo, villeArriveeCombo, dateField, updateButton));

        updateDialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                post.setMessage(messageField.getText());
                post.setVilleDepart(villeDepartCombo.getValue());
                post.setVilleArrivee(villeArriveeCombo.getValue());
                post.setDate(java.sql.Date.valueOf(dateField.getValue()));

                servicePosts.update(post);
                afficherPosts();
                System.out.println("Post mis à jour avec succès !");
                return post;
            }
            return null;
        });

        updateDialog.showAndWait();
    }

    /**
     * Méthode de validation pour le formulaire de mise à jour.
     * Désactive le bouton de mise à jour si :
     * - Le message est vide,
     * - Une des villes n'est pas sélectionnée,
     * - Les villes de départ et d'arrivée sont identiques,
     * - La date est nulle ou dans le passé.
     */
    private void validateUpdateFields(TextField messageField, ComboBox<String> villeDepartCombo, ComboBox<String> villeArriveeCombo, DatePicker dateField, Node updateButton) {
        boolean disable = messageField.getText().trim().isEmpty() ||
                villeDepartCombo.getValue() == null ||
                villeArriveeCombo.getValue() == null ||
                villeDepartCombo.getValue().equals(villeArriveeCombo.getValue()) ||
                dateField.getValue() == null ||
                dateField.getValue().isBefore(LocalDate.now());
        updateButton.setDisable(disable);
    }


    private void handleDeleteComment(String commentId, VBox postBox) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
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
            showAlert(AlertType.WARNING, "Champ vide", "Veuillez entrer un commentaire.");
            return;
        }

        if (!Commentaire.isSafe(commentaireText)) {
            showAlert(AlertType.ERROR, "Commentaire inapproprié", "Votre commentaire contient des mots interdits. Veuillez le modifier.");
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

        showAlert(AlertType.INFORMATION, "Succès", "Commentaire ajouté avec succès !");
    }

    // Méthode pour afficher des alertes
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void goToGestionCov(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/Gestioncov.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }private void handleUpdateComment(Commentaire commentaire) {
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


}
