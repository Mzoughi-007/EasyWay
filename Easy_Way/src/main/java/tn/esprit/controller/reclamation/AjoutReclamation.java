package tn.esprit.controller.reclamation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.models.reclamation.categories;
import tn.esprit.models.reclamation.reclamations;
import tn.esprit.services.reclamation.WhatsAppService;
import tn.esprit.services.reclamation.categorieService;
import tn.esprit.services.reclamation.reclamationService;
import tn.esprit.util.MyDataBase;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import tn.esprit.services.reclamation.MailService;
import tn.esprit.services.reclamation.WhatsAppService;
import tn.esprit.util.SessionManager;


public class AjoutReclamation {

    private final reclamationService reclamationService = new reclamationService();
    private final categorieService categorieService = new categorieService();

    @FXML
    private TextArea descriptionn;

    @FXML
    private TextField email;
    @FXML
    private ComboBox<categories> categorie;
    @FXML
    private TextField sujet;
    @FXML
    private DatePicker date; // Ajouter la DatePicker ici
    @FXML
    private HBox boxh; // Conteneur pour afficher les réclamations
    private int currentId;
    private Connection connection = MyDataBase.getInstance().getCnx();

    @FXML
    private Label categoriecontrol;
    @FXML
    private Label datecontrol;
    @FXML
    private Label descriptioncontrol;
    @FXML
    private Label emailcontrol;
    @FXML
    private Label sujetcontrol;
    @FXML
    private Label messagerec;
    @FXML
    private ComboBox<String> statu;




    @FXML
    public void initialize() {
        if (categorie != null) {
            // Récupérer les catégories et les ajouter
            List<categories> categoriesList = categorieService.getAll();
            categorie.getItems().addAll(categoriesList);

            // Définir le StringConverter après avoir ajouté les éléments
            categorie.setConverter(new StringConverter<categories>() {
                @Override
                public String toString(categories cat) {
                    return (cat != null) ? cat.getType() : ""; // Vérifie bien que `getType()` existe
                }

                @Override
                public categories fromString(String string) {
                    return categorie.getItems().stream()
                            .filter(cat -> cat.getType().equals(string)) // Vérifie bien `getType()`
                            .findFirst()
                            .orElse(null);
                }
            });
        } else {
            System.err.println("ComboBox categorie not initialized!");
        }

        // Ajouter les statuts
        statu.getItems().addAll("En attente");
    }





    @FXML
    void addReclamation(ActionEvent event) {
        String emailText = this.email.getText();
        categories selectedCategorie = categorie.getValue();
        String sujetText = this.sujet.getText();
        String descriptionText = this.descriptionn.getText();
        LocalDate dateIncident = date.getValue();
        this.statu.getValue();
        int user_id = getUserIdByEmail(emailText);
        SessionManager.getInstance().setId_user(user_id);
        System.out.println("ID utilisateur récupéré : " + user_id);

        // Réinitialiser les messages d'erreur
        emailcontrol.setText("");
        sujetcontrol.setText("");
        descriptioncontrol.setText("");
        categoriecontrol.setText("");
        datecontrol.setText("");
        datecontrol.setText("");
        messagerec.setText("");

        boolean isValid = true;

        // Vérifications
        if (emailText.isEmpty()) {
            emailcontrol.setText("L'email est requis.");
            isValid = false;
        } else if (!emailText.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            emailcontrol.setText("Format de l'email invalide.");
            isValid = false;
        }

        if (sujetText.isEmpty()) {
            sujetcontrol.setText("Le sujet est requis.");
            isValid = false;
        }

        if (descriptionText.isEmpty()) {
            descriptioncontrol.setText("La description est requise.");
            isValid = false;
        }

        if (selectedCategorie == null) {
            categoriecontrol.setText("La catégorie est requise.");
            isValid = false;
        }

        if (dateIncident == null) {
            datecontrol.setText("La date d'incidence est requise.");
            isValid = false;
        }

        if (isValid) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation d'ajout");
            alert.setHeaderText("Voulez-vous vraiment ajouter cette réclamation ?");
            alert.setContentText("Cliquez sur OK pour confirmer, Annuler pour annuler.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    if (currentId > 0) {
                        // Mode modification
                        reclamationService.update(new reclamations(currentId, emailText, selectedCategorie, sujetText, "En attente", descriptionText, dateIncident.toString(), user_id));
                        messagerec.setText("Réclamation mise à jour avec succès !");
                    } else {
                        // Mode ajout
                        reclamationService.add(new reclamations(emailText, selectedCategorie, sujetText, "En attente", descriptionText, dateIncident.toString(), user_id));;
                        messagerec.setText("Réclamation ajoutée avec succès !");

                        // ✅ Envoi d'un mail de confirmation
                        new Thread(() -> {
                            String subject = "Confirmation de votre réclamation";
                            String body = "Bonjour,\n\nVotre réclamation a bien été reçue.\n\nDétails :\n" +
                                    "Sujet : " + sujetText + "\n" +
                                    "Description : " + descriptionText + "\n" +
                                    "Date : " + dateIncident.toString() + "\n\nMerci de votre patience.";
                            MailService.sendMail(emailText, subject, body);

                            // Afficher la fenêtre d'information après l'envoi du mail
                            Platform.runLater(() -> {
                                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                infoAlert.setTitle("Confirmation de l'Email");
                                infoAlert.setHeaderText("Email de confirmation envoyé !");
                                infoAlert.setContentText("Votre réclamation a bien été enregistrée et un email de confirmation a été envoyé.");
                                infoAlert.showAndWait();
                            });
                        }
                        ).start();


                        // ✅ Envoi d'un message WhatsApp
                        new Thread(() -> {
                            String messageText = "Bonjour,\n\nIl y'a une nouvelle reclamations a été ajouter !.\n\nDétails :\n" +
                                    "📌 Sujet : " + sujetText + "\n" +
                                    "📝 Description : " + descriptionText + "\n" +
                                    "📅 Date : " + dateIncident.toString() + "\n\nVous pouvez le consulter dans le site EasyWay.";

                            WhatsAppService.sendWhatsAppMessage("+21656107826", messageText); // Remplace par le vrai numéro
                        }).start();

                    }

                    // ✅ Vider les champs après l'ajout
                    clearFields();

                    // ✅ Réinitialiser le message après 3 secondes
                    new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            Platform.runLater(() -> messagerec.setText(""));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getUserIdByEmail(String email) {
        // Exemple de code pour récupérer l'ID utilisateur en fonction de l'email
        String query = "SELECT id_user FROM user WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_user"); // Retourner l'ID utilisateur si trouvé
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // Retourner 0 si l'utilisateur n'existe pas
    }


    private void clearFields() {
        email.clear();
        sujet.clear();
        descriptionn.clear();
        categorie.setValue(null);
        date.setValue(null);
        statu.setValue(null);
    }

    @FXML
    void RedirectToOffers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Covoiturage/Choix.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}