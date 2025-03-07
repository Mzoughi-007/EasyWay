package tn.esprit.controller.event;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Events.Evenements;
import tn.esprit.services.event.ServiceEvenement;

public class ShowList {
    @FXML
    private TableView<Evenements> tableView;
    @FXML
    TableColumn<Evenements, Void> actionColumn;

    @FXML
    private TableColumn<?, ?> debutColumn;

    @FXML
    private TableColumn<?, ?> descriptionColumn;

    @FXML
    private TableColumn<?, ?> finColumn;

    @FXML
    private TableColumn<?, ?> ligneColumn;

    @FXML
    private TableColumn<?, ?> statusColumn;

    @FXML
    private TableColumn<?, ?> typeColumn;
    @FXML
    private TableView<Evenements> eventTable;

    private ObservableList<Evenements> eventData = FXCollections.observableArrayList();

    private ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    public void initialize() {
        fetchDataFromDatabase();
        eventTable.setItems(eventData);
    }

    private void fetchDataFromDatabase() {
        eventData.addAll(serviceEvenement.getAll());
    }

    @FXML
    void addEvent(ActionEvent event) {

    }

    @FXML
    void searchEvent(ActionEvent event) {

    }

}