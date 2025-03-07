package tn.esprit.controller.reclamation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import tn.esprit.models.reclamation.categories;
import tn.esprit.models.reclamation.categories;
import tn.esprit.services.reclamation.categorieService;
import tn.esprit.util.MyDataBase;
import tn.esprit.controller.reclamation.StaticStatu;
import java.io.IOException;
import java.sql.ResultSet;
import tn.esprit.controller.reclamation.CardView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ModifierReclamation {

    @FXML
    private Label messageRecModifier;

    @FXML
    private Button modifierRec;

    @FXML
    private ComboBox<categories> nouvCategorie;
    @FXML
    private DatePicker nouvDate;
    @FXML
    private TextArea nouvDescriptionn;
    @FXML
    private ComboBox<String> nouvStatu;
    @FXML
    private Button gogo;
    @FXML
    private TextField nouvSujet;

    private Connection connection = MyDataBase.getInstance().getCnx();
    private final tn.esprit.services.reclamation.categorieService categorieService = new categorieService();

    private int currentId; // Pour garder l'ID de la réclamation en cours

    @FXML
    public void initialize() {
        if (nouvCategorie != null) {
            // Récupérer les catégories et les ajouter
            List<categories> categoriesList = categorieService.getAll();
            nouvCategorie.getItems().addAll(categoriesList);

            // Définir le StringConverter après avoir ajouté les éléments
            nouvCategorie.setConverter(new StringConverter<categories>() {
                @Override
                public String toString(categories cat) {
                    return (cat != null) ? cat.getType() : ""; // Vérifie bien que `getType()` existe
                }

                @Override
                public categories fromString(String string) {
                    return nouvCategorie.getItems().stream()
                            .filter(cat -> cat.getType().equals(string)) // Vérifie bien `getType()`
                            .findFirst()
                            .orElse(null);
                }
            });
        } else {
            System.err.println("ComboBox categorie not initialized!");
        }

        // Ajouter les statuts
        nouvStatu.getItems().addAll("En attente", "Traité", "Refusé");
    }


    public void setReclamationDetails(int id, String sujet, String description, int categorieId, String date, String statut) {
        this.nouvSujet.setText(sujet);
        this.nouvDescriptionn.setText(description);
        this.nouvDate.setValue(LocalDate.parse(date));
        nouvStatu.setValue(statut); // ✅ Corrigé ici

        // Sélectionner la catégorie dans le ComboBox
        for (categories cat : nouvCategorie.getItems()) { // ✅ Utilisation correcte
            if (cat.getId() == categorieId) {
                nouvCategorie.setValue(cat);
                break;
            }
        }

        // Stocker l'ID pour l'utiliser lors de la mise à jour
        this.currentId = id;
    }

    @FXML
    void gotoCardView1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/reclamation/CardView.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void modifierReclamation(ActionEvent event) {
        try {
            // Validation des champs avant mise à jour
            if (nouvSujet.getText().isEmpty() || nouvDescriptionn.getText().isEmpty()) {
                messageRecModifier.setText("Le sujet et la description sont obligatoires !");
                return;
            }

            // Vérifier si la catégorie est sélectionnée
            if (nouvCategorie.getValue() == null) {
                messageRecModifier.setText("Veuillez sélectionner une catégorie !");
                return;
            }

            // Vérifier si la date est sélectionnée
            if (nouvDate.getValue() == null) {
                messageRecModifier.setText("Veuillez sélectionner une date !");
                return;
            }

            // Mettre à jour la réclamation dans la base de données
            String query = "UPDATE reclamation SET sujet = ?, description = ?, categorieId = ?, statu = ?, date_creation = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, nouvSujet.getText());
            pstmt.setString(2, nouvDescriptionn.getText());
            categories selectedCategory = nouvCategorie.getValue();
            pstmt.setInt(3, selectedCategory.getId());
            pstmt.setString(4, nouvStatu.getValue()); // ✅ Correct si c'est un ComboBox<String>
            pstmt.setString(5, nouvDate.getValue().toString()); // Utilisation de LocalDate.toString()
            pstmt.setInt(6, currentId);


            // ✅ Vider les champs après l'ajout hellooooo
            clearFields();

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                messageRecModifier.setText("Réclamation mise à jour avec succès !");
                System.out.println("Réclamation mise à jour avec succès !");
            } else {
                messageRecModifier.setText("Erreur lors de la mise à jour de la réclamation !");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messageRecModifier.setText("Erreur lors de la mise à jour de la réclamation !");
        }
    }

    public void gogotoStatestique(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/reclamation/staticStatu.fxml")));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    private void clearFields() {
        nouvSujet.clear();
        nouvDescriptionn.clear();
        nouvCategorie.setValue(null);
        nouvDate.setValue(null);
        nouvStatu.setValue(null);
    }
}


