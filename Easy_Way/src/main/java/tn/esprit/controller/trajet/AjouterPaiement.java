package tn.esprit.controller.trajet;

import com.stripe.model.PaymentIntent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tn.esprit.models.trajet.Paiement;
import tn.esprit.models.trajet.PaiementAPI;
import tn.esprit.services.trajet.ServicePaiement;
import tn.esprit.util.SessionManager;

import java.io.IOException;

public class AjouterPaiement {

    @FXML
    private TextField montant;

    @FXML
    private TextField card_id;

    @FXML
    private Text error1;

    @FXML
    private Text t1;

    @FXML
    private Text t2;

    private int reservation_id;
    public void set_reservation_id(int reservation_id){
        this.reservation_id = reservation_id;
    }
    public void setDepartArret(String departValue, String arretValue) {
        t1.setText(departValue);
        t2.setText(arretValue);
    }

    void setMontant(int numberOfPlaces, String vehicleType) {
        double pricePerSeat = 0.0;

        if (vehicleType == null || vehicleType.isEmpty()) {
            montant.setText("Aucune vehicule selectionnée");
            return;
        }

        if (vehicleType.equals("Bus")) {
            pricePerSeat = 3.5;
        } else if (vehicleType.equals("Metro")) {
            pricePerSeat = 3.0;
        } else if (vehicleType.equals("Train")) {
            pricePerSeat = 2.5;
        } else if (vehicleType.equals("Voiture")) {
            pricePerSeat = 5.0;
        } else {
            montant.setText("Type invalide.");
            return;
        }

        double totalAmount = numberOfPlaces * pricePerSeat;
        montant.setText(String.format("%.2f", totalAmount));
    }



    ServicePaiement paiementService = new ServicePaiement();

    @FXML
    void Valider(ActionEvent event) {
        double amount = Double.parseDouble(montant.getText());
        String cardId = card_id.getText();
        int reservation_id = this.reservation_id;
        System.out.println("ahawa" + reservation_id);
        int user_id = SessionManager.getInstance().getId_user();

        if (!cardId.matches("^(\\d{4}\\s){3}\\d{4}$")) {
            error1.setText("Invalide. Utilisez XXXX XXXX XXXX XXXX.");
            return;
        }

        error1.setText("");

        PaymentIntent paymentIntent = PaiementAPI.createPayment(amount);

        if (paymentIntent != null) {
            String paymentIntentId = paymentIntent.getId();

            Paiement paiement = new Paiement();
            paiement.setMontant(amount);
            paiement.setPay_id(paymentIntentId);
            paiement.setRes_id(reservation_id);
            paiement.setUser_id(user_id);


            paiementService.add(paiement);
            System.out.println("succès.");
            try {
                FXMLLoader load = new FXMLLoader(getClass().getResource("/trajet/AfficherPaiement.fxml"));
                Parent root = load.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Paiement échoué.");
        }
    }



    @FXML
    void page_affichage(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/afficherReservation.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void page_ajout(ActionEvent event) {
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/ajoutReservation.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

