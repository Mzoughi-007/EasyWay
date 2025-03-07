package tn.esprit.controller.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Events.Evenements;
import tn.esprit.services.event.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListView implements Initializable {
    @FXML
    private VBox eventLayout;
    ServiceEvenement se = new ServiceEvenement();
    @FXML
    void goToAddPage(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/event/addEvenement.fxml"));
        root = loader.load();

        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Evenements> events = se.getAll();
        for(Evenements e : events){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/evenement/eventItem.fxml"));

            try {
                HBox hBox = loader.load();
                eventItemController eic = loader.getController();
                eic.setData(e);
                eventLayout.getChildren().add(hBox);
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }
}
