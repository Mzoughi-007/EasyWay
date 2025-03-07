package tn.esprit.controller.covoiturage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.covoiturage.Payment;
import tn.esprit.models.covoiturage.Posts;
import tn.esprit.services.covoiturage.APIpayment;
import tn.esprit.services.covoiturage.MailService;
import tn.esprit.services.covoiturage.ServicePayment;
import tn.esprit.services.covoiturage.ServicePosts;
import com.stripe.model.PaymentIntent;

import java.io.IOException;
import java.util.List;

public class Reservationcov {

    @FXML private Text t1;
    @FXML private Text t2;
    @FXML private TextField montant;
    @FXML private TextField emailField;
    @FXML private TextField card_id;
    @FXML private Spinner<Integer> spinnerPlaces;
    @FXML private Text error1;

    private Posts selectedPost;
    private final ServicePosts servicePosts = new ServicePosts();
    private final MailService mailService;
    private int nombreDePlacesDisponibles ;
    private boolean isUpdating = false;

    public Reservationcov() {
        this.mailService = new MailService();
    }

    @FXML
    public void initialize() {
        List<Posts> postsList = servicePosts.getAll();
        if (!postsList.isEmpty()) {
            setSelectedPost(postsList.get(postsList.size() - 1));
        } else {
            t1.setText("N/A");
            t2.setText("N/A");
            montant.setText("0.00");
        }
        if (emailField != null) {
            emailField.setDisable(false);
            emailField.setEditable(true);
        }
    }

    public void setSelectedPost(Posts post) {
        this.selectedPost = post;
        this.nombreDePlacesDisponibles = selectedPost.getNombreDePlaces();

        t1.setText(post.getVilleDepart());
        t2.setText(post.getVilleArrivee());

        if (spinnerPlaces != null) {
            SpinnerValueFactory<Integer> valueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(1, nombreDePlacesDisponibles, 1);
            spinnerPlaces.setValueFactory(valueFactory);

            spinnerPlaces.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (!isUpdating) {
                    isUpdating = true;
                    calculerMontant(post.getPrix(), newValue);
                    isUpdating = false;
                }
            });
            calculerMontant(post.getPrix(), 1);
        }
    }

    private void calculerMontant(double prixParPlace, int nombreDePlaces) {
        double total = prixParPlace * nombreDePlaces;
        String formattedTotal = String.format("%.2f", total);
        if (!montant.getText().equals(formattedTotal)) {
            montant.setText(formattedTotal);
        }
    }

    @FXML
    void Valider(ActionEvent event) {
        try {
            error1.setText("");
            double amount = Double.parseDouble(montant.getText().replace(",", "."));
            String cardNumber = card_id.getText().trim();
            String userEmail = emailField.getText().trim();
            int placesDemandées = spinnerPlaces.getValue();

            if (placesDemandées > nombreDePlacesDisponibles) {
                showAlert("❌ Nombre de places insuffisant ! Il ne reste que " + nombreDePlacesDisponibles + " places.");
                return;
            }

            if (!cardNumber.matches("^(\\d{4}[ -]){3}\\d{4}$")) {
                error1.setText("Numéro de carte invalide. Utilisez XXXX XXXX XXXX XXXX ou XXXX-XXXX-XXXX-XXXX.");
                return;
            }

            if (!userEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                error1.setText("Adresse e-mail invalide.");
                return;
            }

            if (amount <= 0) {
                error1.setText("Montant invalide.");
                return;
            }

            if (mailService == null) {
                System.out.println("❌ Erreur: MailService n'est pas initialisé !");
                return;
            }

            PaymentIntent paymentIntent = APIpayment.createPayment(amount, userEmail);
            if (paymentIntent != null) {
                String paymentIntentId = paymentIntent.getId();
                Payment paiement = new Payment();
                paiement.setAmount(amount);
                paiement.setTransactionId(paymentIntentId);
                paiement.setEmail(userEmail);
                ServicePayment servicePayment = new ServicePayment();
                servicePayment.add(paiement);
                System.out.println("✅ Email enregistré dans la base : " + userEmail);
                nombreDePlacesDisponibles -= placesDemandées;
                servicePosts.updateNombrePlaces(selectedPost.getId_post(), nombreDePlacesDisponibles);
                boolean emailSent = mailService.sendEmail(userEmail, "Confirmation de paiement",
                        "Votre paiement de " + amount + " DT a été reçu avec succès.\n"
                                + "Nombre de places réservées : " + placesDemandées + "\n"
                                + "Ville de départ : " + selectedPost.getVilleDepart() + "\n"
                                + "Ville d'arrivée : " + selectedPost.getVilleArrivee());
                if (emailSent) {
                    showSuccess("Le paiement a été effectué avec succès ! Un e-mail de confirmation a été envoyé à " + userEmail);

                } else {
                    showAlert("⚠ Le paiement a été effectué, mais l'e-mail de confirmation n'a pas été envoyé.");
                }
            } else {
                showAlert("⚠ Paiement échoué.");
            }
        } catch (NumberFormatException e) {
            showAlert("⚠ Montant invalide.");
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goToOffres(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Covoiturage/offres.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
