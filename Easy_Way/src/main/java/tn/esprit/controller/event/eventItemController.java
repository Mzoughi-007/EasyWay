package tn.esprit.controller.event;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import tn.esprit.models.Events.Evenements;
import tn.esprit.services.event.ServiceEvenement;

import java.net.URL;
import java.util.ResourceBundle;

public class eventItemController implements Initializable {

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Label idDateDebut;

    @FXML
    private Label idDateFin;

    @FXML
    private Label idDesc;

    @FXML
    private Label idLigne;

    @FXML
    private Label idStatus;

    @FXML
    private Label idType;

    @FXML
    private Button viewButton;
    ServiceEvenement se = new ServiceEvenement();
    public void setData(Evenements event){
        idType.setText(event.getType_evenement().name());
        idDesc.setText(event.getDescription());
        idDateDebut.setText(event.getDate_debut().toString());
        idDateFin.setText(event.getDate_fin().toString());
        idLigne.setText(se.getLigneInfo(event.getId_ligne_affectee()));
        idStatus.setText(event.getStatus_evenement().name());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
