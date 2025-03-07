package tn.esprit.controller.trajet;


import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.models.trajet.Map;
import tn.esprit.models.trajet.Reservation;
import tn.esprit.services.trajet.ServiceMap;
//import tn.esprit.services.trajet.ServicePaiement;
import tn.esprit.services.trajet.ServiceReservation;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.SpinnerValueFactory;
import tn.esprit.util.SessionManager;

public class AjoutReservation {

    @FXML
    private TextField arret;

    @FXML
    private ComboBox<String> vehicule;

    @FXML
    private TextField depart;

    @FXML
    private Spinner<Integer> nb;

    @FXML
    private Button reserver;

    @FXML
    private ImageView loc;

    @FXML
    private Label control1;

    @FXML
    private Label control2;

    @FXML
    private Label control3;

    @FXML
    private Label control4;

    @FXML
    private WebView map;

    @FXML
    public void initialize() {
        // spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1); //min=0 max=100
        nb.setValueFactory(valueFactory);
        loc.setVisible(true);
        ServiceMap serviceMap = new ServiceMap();
        serviceMap.initializeMap(map);
    }

    @FXML
    void key_pressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            String address = depart.getText().trim();
            if (!address.isEmpty()) {
                double[] coordinates = Map.getCoordinates(address);
                if (coordinates[0] != 0 && coordinates[1] != 0) {
                    String placeName = Map.getPlaceNameFromCoordinates(coordinates[0], coordinates[1]);
                    depart.setText(placeName);
                }
            }
        }
    }

    @FXML
    void payer(ActionEvent event) {
        ServiceReservation sp = new ServiceReservation();
        List<Reservation> reservations = sp.getAll();
        //setting up the stage
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/AjouterPaiement.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Transition to the next scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


//magic link

    @FXML
    void showReservation(ActionEvent event) {
        ServiceReservation sp = new ServiceReservation();
        List<Reservation> reservations = sp.getAll();
        //setting up the stage
        Stage stage;
        Scene scene;
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/afficherReservation.fxml"));
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AfficherReservation c = fxmlLoader.getController();
        c.setReservations(reservations);

        // Transition to the next scene
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void entered(MouseEvent event) {
        reserver.setStyle(reserver.getStyle() + "-fx-background-color: #228B22;");
    }

    @FXML
    void exited(MouseEvent event) {
        reserver.setStyle(reserver.getStyle() + "-fx-background-color: #008000;");
    }

    @FXML
    void pressed(MouseEvent event) {
        ScaleTransition clickTrans = new ScaleTransition(Duration.millis(50), reserver);
        clickTrans.setByX(0.05);
        clickTrans.setByY(0.04);
        clickTrans.setCycleCount(1);
        clickTrans.play();
        reserver.setStyle(reserver.getStyle() + "-fx-background-color: #004d00;");
        clickTrans.setOnFinished(e -> {
            ScaleTransition reverseTrans = new ScaleTransition(Duration.millis(50), reserver);
            reverseTrans.setByX(-0.05);
            reverseTrans.setByY(-0.04);
            reverseTrans.setCycleCount(1);
            reverseTrans.play();
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(ev -> {
                reserver.setStyle(reserver.getStyle().replace("#004d00", "#008000"));
            });
            pause.play();
        });
    }

    public void deleteLabel(Label label) {
        if (label.getParent() != null) {
            ((Pane) label.getParent()).getChildren().remove(label);
        }
    }
    @FXML
    void addReservation(ActionEvent event) {
        SessionManager.getInstance().setId_user(4);


        ServiceReservation sp = new ServiceReservation();
        if(!depart.getText().isEmpty() && !arret.getText().isEmpty() && vehicule.getValue() != null && nb.getValue() <= 4){
            int reservation_id = sp.add2(new Reservation(depart.getText(), arret.getText(), vehicule.getValue(), nb.getValue(),SessionManager.getInstance().getId_user()));
            loc.setVisible(true);
            arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
            depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
            vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
            nb.setStyle("-fx-border-radius: 15px; -fx-border-color: #aeaeae; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");

            String vehicleType = vehicule.getValue();
            int numberOfPlaces = nb.getValue();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/trajet/AjouterPaiement.fxml"));
                Parent root = fxmlLoader.load();

                AjouterPaiement Controller = fxmlLoader.getController();
                //l'affichage mta3 depart wl arret fl controller paiement
                Controller.setDepartArret(depart.getText(), arret.getText());
                Controller.setMontant(numberOfPlaces, vehicleType);
                Controller.set_reservation_id(reservation_id);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if (nb.getValue() > 4) {
                control4.setText("Le nombre ne doit pas dépasser 4");
                control4.setTextFill(Color.RED);

                nb.setStyle("-fx-border-radius: 15px; -fx-border-color: red; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");

            }else {deleteLabel(control4);}
            if (depart.getText().isEmpty()) {
                control1.setText("Veillez saisir votre position");
                control1.setTextFill(Color.RED);
                depart.setStyle("-fx-border-radius: 15px; -fx-border-color: red; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
            }else {deleteLabel(control1);}
            if (arret.getText().isEmpty()) {
                control2.setText("Veillez saisir votre destination");
                control2.setTextFill(Color.RED);
                arret.setStyle("-fx-border-radius: 15px; -fx-border-color: red; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
            }else {deleteLabel(control2);}
            if (vehicule.getValue() == null) {
                control3.setText("Veillez choisir votre mode de transport");
                control3.setTextFill(Color.RED);
                vehicule.setStyle("-fx-border-radius: 15px; -fx-border-color: red; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
            }else {deleteLabel(control3);}
        }
    }

    @FXML
    void background_click(MouseEvent event) {
        loc.setVisible(true);
        if (depart.isFocused()) {
            depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        }
        if (arret.isFocused()) {
            arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        }
        if (vehicule.isFocused()) {
            vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
        }
        if (nb.isFocused()) {
            nb.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
        }
    }
    @FXML
    void depart_pressed(MouseEvent event) {
        loc.setVisible(false);
        depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: green; -fx-background-color: white;");
        arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
        nb.setStyle("-fx-border-radius: 15px; -fx-border-color: #aeaeae; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
    }
    @FXML
    void arret_pressed(MouseEvent event) {
        loc.setVisible(true);
        arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: green; -fx-background-color: white;");
        depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
        nb.setStyle("-fx-border-radius: 15px; -fx-border-color: #aeaeae; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
    }

    @FXML
    void vehicule_pressed(MouseEvent event) {
        loc.setVisible(true);
        vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: green; -fx-border-radius: 15;");
        depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        nb.setStyle("-fx-border-radius: 15px; -fx-border-color: #aeaeae; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
    }

    @FXML
    void nb_pressed(MouseEvent event) {
        loc.setVisible(true);
        nb.setStyle("-fx-border-radius: 15px; -fx-border-color: green; -fx-background-radius: 15px; -fx-background-color: white; -fx-font-size: 10; -fx-padding: 3px 30px;");
        vehicule.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-border-color: #aeaeae; -fx-border-radius: 15;");
        depart.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
        arret.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #aeaeae; -fx-background-color: white;");
    }
}

