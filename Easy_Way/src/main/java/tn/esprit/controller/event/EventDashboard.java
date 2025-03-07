package tn.esprit.controller.event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.models.Events.Evenements;
import tn.esprit.services.event.ServiceEvenement;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EventDashboard implements Initializable {

    @FXML
    private GridPane eventGrid;
    @FXML
    private GridPane grid;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> statusFilter;
    @FXML
    private ChoiceBox<String> typeFilter;
    @FXML
    private BarChart<String, Number> eventChart;

    private List<Evenements> eventList = new ArrayList<>();
    private final ServiceEvenement se = new ServiceEvenement();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadFilters();
        loadEventData();
        setupSearchFilter();
    }

    private void loadFilters() {
        statusFilter.getItems().addAll("Tous", "En cour", "Résolu", "Annulé");
        typeFilter.getItems().addAll("Tous", "Retard", "Incident", "Grève");
        statusFilter.setValue("Tous");
        typeFilter.setValue("Tous");

        statusFilter.setOnAction(event -> loadEventData());
        typeFilter.setOnAction(event -> loadEventData());
    }

    private void loadEventData() {
        eventList = se.getAll();
        eventGrid.getChildren().clear();

        int row = 0;
        for (Evenements event : eventList) {
            if (shouldDisplayEvent(event)) {
                VBox eventCard = createEventCard(event);
                grid.add(eventCard, 0, row);
                row++;
            }
        }

        updateChart();
    }

    private VBox createEventCard(Evenements event) {
        VBox card = new VBox();
        card.setMinWidth(225);
        card.setPadding(new Insets(10));
        card.setSpacing(5);
        card.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10px; -fx-border-color: #ddd;");

        Label titleLabel = new Label(event.getType_evenement().toString());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label descLabel = new Label(event.getDescription());
        descLabel.setWrapText(true);

        Label dateLabel = new Label("Début: " + event.getDate_debut() + " | Fin: " + event.getDate_fin());
        dateLabel.setStyle("-fx-font-size: 12px;");

        HBox actionButtons = new HBox(10);
        Button editButton = new Button("Modifier");
        Button deleteButton = new Button("Supprimer");

        editButton.setOnAction(e -> openEditPage(event));
        deleteButton.setOnAction(e -> deleteEvent(event));

        actionButtons.getChildren().addAll(editButton, deleteButton);
        card.getChildren().addAll(titleLabel, descLabel, dateLabel, actionButtons);

        return card;
    }

    private boolean shouldDisplayEvent(Evenements event) {
        String searchText = searchField.getText().toLowerCase();
        String selectedStatus = statusFilter.getValue();
        String selectedType = typeFilter.getValue();

        return (selectedStatus.equals("Tous") || event.getStatus_evenement().toString().equalsIgnoreCase(selectedStatus))
                && (selectedType.equals("Tous") || event.getType_evenement().toString().equalsIgnoreCase(selectedType))
                && (event.getDescription().toLowerCase().contains(searchText)
                || event.getType_evenement().toString().toLowerCase().contains(searchText));
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> loadEventData());
    }

    private void updateChart() {
        Map<String, Integer> eventCount = new HashMap<>();
        for (Evenements event : eventList) {
            eventCount.put(event.getType_evenement().toString(), eventCount.getOrDefault(event.getType_evenement().toString(), 0) + 1);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : eventCount.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        eventChart.getData().clear();
        eventChart.getData().add(series);
    }

    @FXML
    void RedirectToEvent(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Evenement/eventTable.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) grid.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openEditPage(Evenements event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/editEventPage.fxml"));
            Parent root = loader.load();
            editPageController editController = loader.getController();
            editController.initData(event);

            Stage stage = (Stage) eventGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteEvent(Evenements event) {
        se.delete(event.getId_event());
        loadEventData();
    }
}
