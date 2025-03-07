package tn.esprit.controller.vehicule;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.lookups.v1.PhoneNumber;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import tn.esprit.models.user.User;
import tn.esprit.models.vehicules.*;
import tn.esprit.services.VehiculeService.ServiceVehicule;
import tn.esprit.services.user.ServiceUser;
import tn.esprit.util.SessionManager;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class vehiculeController {
    @FXML
    private ImageView image;
    @FXML
    private WebView mapWebView;

    @FXML
    private FlowPane busCardsContainer;
    @FXML
    private FlowPane metroCardsContainer;
    @FXML
    private FlowPane trainCardsContainer;
    @FXML private ComboBox<String> conductorFilter;
    @FXML private ComboBox<String> tripFilter;
    @FXML private ComboBox<String> stateFilter;
    @FXML private ComboBox<TypeService> serviceFilter;
    @FXML private ComboBox<String> tripFilter1;
    @FXML private ComboBox<String> stateFilter1;
    @FXML private ComboBox<String> conductorFilter1;
    @FXML private ComboBox<String> propertyFilter;

    @FXML private ComboBox<String> tripFilter2;
    @FXML private ComboBox<String> stateFilter2;
    @FXML private ComboBox<String> conductorFilter2;
    @FXML private ComboBox<String> propertyFilter2;
    @FXML private TextField searchField;
    @FXML private TextField searchField1;
    @FXML private TextField searchField2;
    @FXML
    private Label username;
    private ServiceUser su = new ServiceUser();
    private User currentUser = su.getById(SessionManager.getInstance().getId_user());
    private ServiceVehicule vehiculeService = new ServiceVehicule();
    private Scene scene;
    private BorderPane rootPane;


    @FXML
    private void initialize() {
        image.setImage(new Image(new File(currentUser.getPhoto_profil()).toURI().toString()));
        username.setText(currentUser.getNom()+" "+currentUser.getPrenom());
        // Chargement du CSS
        String cssPath = getClass().getResource("/vehicule/easyway.css").toExternalForm();

        // Application du CSS aux conteneurs
        busCardsContainer.getStylesheets().add(cssPath);
        metroCardsContainer.getStylesheets().add(cssPath);
        trainCardsContainer.getStylesheets().add(cssPath);

        searchField2.textProperty().addListener((observable, oldValue, newValue) -> {
            applyTrainFilterSearch(propertyFilter2.getValue(), stateFilter2.getValue(), tripFilter2.getValue(), conductorFilter2.getValue(), newValue);
        });

        searchField1.textProperty().addListener((observable, oldValue, newValue) -> {
            applyMetroFilter(propertyFilter.getValue(), stateFilter1.getValue(), tripFilter1.getValue(), conductorFilter1.getValue(), newValue);
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilterSearch(serviceFilter.getValue(), stateFilter.getValue(), tripFilter.getValue(), conductorFilter.getValue(),newValue);
        });
        ObservableList<String> props1 = FXCollections.observableArrayList(vehiculeService.getProprietairesTrain());
        props1.add(0, "Tous"); // Ajouter "Tous" en première position
        propertyFilter2.setItems(props1);
        ObservableList<String> conductors1 = FXCollections.observableArrayList(vehiculeService.getConducteurs());
        conductors1.add(0, "Tous");
        conductorFilter2.setItems(conductors1);
        ObservableList<String> trajets2 = FXCollections.observableArrayList(vehiculeService.getTrajets());
        trajets2.add(0, "Tous"); // Ajouter "Tous" en première position
        tripFilter2.setItems(trajets2);
        ObservableList<String> etats2 = FXCollections.observableArrayList(
                "DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE", "Tous" // Ajouter "Tous" pour afficher tous les états
        );
        stateFilter2.setItems(etats2);
        propertyFilter2.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par propriétaire sélectionné : " + newValue);
            applyTrainFilter(newValue, stateFilter2.getValue(), tripFilter2.getValue(), conductorFilter2.getValue());
        });

        stateFilter2.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par état sélectionné : " + newValue);
            applyTrainFilter(propertyFilter2.getValue(), newValue, tripFilter2.getValue(), conductorFilter2.getValue());
        });

        tripFilter2.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par trajet sélectionné : " + newValue);
            applyTrainFilter(propertyFilter2.getValue(), stateFilter2.getValue(), newValue, conductorFilter2.getValue());
        });

        conductorFilter2.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par conducteur sélectionné : " + newValue);
            applyTrainFilter(propertyFilter.getValue(), stateFilter1.getValue(), tripFilter1.getValue(), newValue);
        });
        ObservableList<String> props = FXCollections.observableArrayList(vehiculeService.getProprietaires());
        props.add(0, "Tous"); // Ajouter "Tous" en première position
        propertyFilter.setItems(props);
        ObservableList<String> conductors = FXCollections.observableArrayList(vehiculeService.getConducteurs());
        conductors.add(0, "Tous");
        conductorFilter1.setItems(conductors);
        ObservableList<String> trajets1 = FXCollections.observableArrayList(vehiculeService.getTrajets());
        trajets1.add(0, "Tous"); // Ajouter "Tous" en première position
        tripFilter1.setItems(trajets1);
        ObservableList<String> etats1 = FXCollections.observableArrayList(
                "DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE", "Tous" // Ajouter "Tous" pour afficher tous les états
        );
        stateFilter1.setItems(etats1);

        propertyFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par propriétaire sélectionné : " + newValue);
            applyMetroFilter(newValue, stateFilter1.getValue(), tripFilter1.getValue(), conductorFilter1.getValue());
        });

        stateFilter1.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par état sélectionné : " + newValue);
            applyMetroFilter(propertyFilter.getValue(), newValue, tripFilter1.getValue(), conductorFilter1.getValue());
        });

        tripFilter1.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par trajet sélectionné : " + newValue);
            applyMetroFilter(propertyFilter.getValue(), stateFilter1.getValue(), newValue, conductorFilter1.getValue());
        });

        conductorFilter1.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par conducteur sélectionné : " + newValue);
            applyMetroFilter(propertyFilter.getValue(), stateFilter1.getValue(), tripFilter1.getValue(), newValue);
        });
        // Remplir les ComboBox
        ObservableList<TypeService> typeServices = FXCollections.observableArrayList(TypeService.values());
        typeServices.add(0, null); // Ajouter "Tous" en première position (null représente "Tous")
        serviceFilter.setItems(typeServices);

        ObservableList<String> etats = FXCollections.observableArrayList(
                "DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE", "Tous" // Ajouter "Tous" pour afficher tous les états
        );
        stateFilter.setItems(etats);

        ObservableList<String> trajets = FXCollections.observableArrayList(vehiculeService.getTrajets());
        trajets.add(0, "Tous"); // Ajouter "Tous" en première position
        tripFilter.setItems(trajets);

        ObservableList<String> conducteurs = FXCollections.observableArrayList(vehiculeService.getConducteurs());
        conducteurs.add(0, "Tous"); // Ajouter "Tous" en première position
        conductorFilter.setItems(conducteurs);

        // Ajouter des écouteurs pour les filtres
        serviceFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par service sélectionné : " + newValue);
            applyFilter(newValue, stateFilter.getValue(), tripFilter.getValue(), conductorFilter.getValue());
        });

        stateFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par état sélectionné : " + newValue);
            applyFilter(serviceFilter.getValue(), newValue, tripFilter.getValue(), conductorFilter.getValue());
        });

        tripFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par trajet sélectionné : " + newValue);
            applyFilter(serviceFilter.getValue(), stateFilter.getValue(), newValue, conductorFilter.getValue());
        });

        conductorFilter.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Nouveau filtre par conducteur sélectionné : " + newValue);
            applyFilter(serviceFilter.getValue(), stateFilter.getValue(), tripFilter.getValue(), newValue);
        });



        // Chargement des données
        loadBusCards();
        loadMetroCards();
        loadTrainCards();

    }

    private List<VBox> allCards; // Liste de toutes les cartes (non filtrées)
    private List<VBox> allMetroCards;
    private List<VBox> allTrainCards;

    private void loadBusCards() {
        List<Bus> buses = vehiculeService.getAll().stream()
                .filter(v -> v instanceof Bus)
                .map(v -> (Bus) v)
                .toList();

        System.out.println("Nombre de bus chargés : " + buses.size());

        // Réinitialiser allCards
        allCards = new ArrayList<>();

        for (Bus bus : buses) {
            System.out.println("Ajout du bus : " + bus.getTypeService());
            VBox card = createBusCard(bus);
            allCards.add(card); // Ajouter la carte à allCards
        }

        // Afficher toutes les cartes initialement
        busCardsContainer.getChildren().setAll(allCards);
    }

    private void loadMetroCards() {
        // Récupérer tous les véhicules
        List<vehicule> vehicules = vehiculeService.getAll();

        // Filtrer et convertir les métros
        List<Metro> metros = vehicules.stream()
                .filter(v -> v instanceof Metro)
                .map(v -> (Metro) v)
                .toList();

        System.out.println("Nombre de métros chargés : " + metros.size());

        // Réinitialiser allMetroCards
        allMetroCards = new ArrayList<>();

        for (Metro metro : metros) {
            System.out.println("Ajout du métro : " + metro.getProprietaire());
            VBox card = createMetroCard(metro);
            allMetroCards.add(card); // Ajouter la carte à allMetroCards
        }

        // Afficher toutes les cartes de métro initialement
        metroCardsContainer.getChildren().setAll(allMetroCards);
    }

    private void loadTrainCards() {
        List<Train> trains = vehiculeService.getAll().stream()
                .filter(v -> v instanceof Train)
                .map(v -> (Train) v)
                .toList();

        System.out.println("Nombre de trains chargés : " + trains.size());

        // Réinitialiser allTrainCards
        allTrainCards = new ArrayList<>();

        for (Train train : trains) {
            System.out.println("Ajout du train : " + train.getProprietaire());
            VBox card = createTrainCard(train);
            allTrainCards.add(card); // Ajouter la carte à allTrainCards
        }

        // Afficher toutes les cartes initialement
        trainCardsContainer.getChildren().setAll(allTrainCards);
    }

    private VBox createBusCard(Bus bus) {
        VBox card = new VBox();
        card.getStyleClass().add("vehicle-card");
        card.setSpacing(10); // Ajouter un espacement entre les éléments

        // Header avec status à gauche
        HBox header = new HBox();
        header.getStyleClass().add("card-header");
        header.setSpacing(10); // Vous pouvez ajuster l'espacement global si nécessaire
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Bus");
        titleLabel.getStyleClass().add("card-title");

// Créez un Region pour occuper l'espace entre les labels
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS); // Le Region s'étendra pour occuper tout l'espace disponible

        Label statusLabel = new Label(bus.getEtat().toString());
        statusLabel.getStyleClass().addAll("status-badge", getStatusStyleClass(bus.getEtat()));

        Button exportPdfButton = new Button();
        exportPdfButton.getStyleClass().add("export-pdf-button");

        exportPdfButton.setOnAction(e -> {
            String filePath = "BusCard_" + bus.getImmatriculation() + ".pdf";
            exportBusToPdf(card, filePath);
            System.out.println("Exportation en PDF réussie : " + filePath);
        });

        Image menuIconImage = new Image(getClass().getResourceAsStream("/vehicule/pdf.png"));
        ImageView menuIcon = new ImageView(menuIconImage);
        menuIcon.setFitWidth(30); // Ajuster la taille de l'icône
        menuIcon.setFitHeight(30);

// Définir l'icône comme graphique du bouton
        exportPdfButton.setGraphic(menuIcon);

// Ajoutez les éléments à l'HBox dans l'ordre souhaité
        header.getChildren().addAll(titleLabel, spacer1, statusLabel, exportPdfButton);

        // Content
        GridPane content = new GridPane();
        content.getStyleClass().add("card-content");
        content.setHgap(15);
        content.setVgap(15);
        content.setPadding(new Insets(15));

        // Basic Info
        addFieldToGrid(content, 0, "Immatriculation:", bus.getImmatriculation());
        addFieldToGrid(content, 1, "Capacité:", String.valueOf(bus.getCapacite()));

        // Conducteur Info
        addFieldToGrid(content, 2, "Conducteur:",
                vehiculeService.getConducteurNomById(bus.getIdConducteur()) + " " +
                        vehiculeService.getConducteurPrenomById(bus.getIdConducteur()));

        // Trajet Info
        addFieldToGrid(content, 3, "Trajet:",
                "Départ: " + vehiculeService.getTrajetDepartById(bus.getIdTrajet()) + "\n" +
                        "Arrêt: " + vehiculeService.getTrajetArretById(bus.getIdTrajet()));

        // Spécifications
        addFieldToGrid(content, 4, "Spécifications:",
                "Portes: " + bus.getNombrePortes() + "\n" +
                        "Type: " + bus.getTypeService() + "\n" +
                        "Places: " + bus.getNombreDePlaces() + "\n" +
                        "Compagnie: " + bus.getCompagnie() + "\n" +
                        "Climatisation: " + (bus.isClimatisation() ? "Oui" : "Non"));

        HBox actions = new HBox();
        actions.getStyleClass().add("card-actions");

        // Création des boutons à partir du FXML
        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("edit-button");
        Region editIcon = new Region();
        editIcon.getStyleClass().add("edit-icon");
        editButton.setGraphic(editIcon);
        editButton.setUserData(bus); // Stocke l'objet bus dans le bouton

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button showRouteButton = new Button("Trajet");
        showRouteButton.getStyleClass().add("trajet-button");
        Region routeIcon = new Region();
        editButton.setGraphic(routeIcon);
        showRouteButton.setOnAction((event -> {
            // Call the showMapInWebView method with the desired address
            showMapInBrowser(vehiculeService.getTrajetDepartById(bus.getIdTrajet()) , vehiculeService.getTrajetArretById(bus.getIdTrajet()) + ", Tunisia");
        }));



        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setPrefHeight(26.0);
        deleteButton.setPrefWidth(110.0);
        Region deleteIcon = new Region();
        deleteIcon.getStyleClass().add("delete-icon");
        deleteButton.setGraphic(deleteIcon);
        deleteButton.setUserData(bus); // Stocke l'objet bus dans le bouton
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);




        // Ajouter les boutons à la HBox


        actions.getChildren().addAll(editButton, spacer2, showRouteButton, spacer, deleteButton);



        card.getChildren().addAll(header, content, actions);
        deleteButton.setOnAction(e->handleDelete(bus));
        editButton.setOnAction(e->openEditDialogBus(bus));
        card.setUserData(bus);
        return card;
    }

    @FXML
    private void handleEditBus(ActionEvent event) {
        Button button = (Button) event.getSource();
        Bus bus = (Bus) button.getUserData();
        openEditDialogBus(bus);
    }

    @FXML
    private void handleDeleteBus(ActionEvent event) {
        Button button = (Button) event.getSource();
        Bus bus = (Bus) button.getUserData();
        handleDelete(bus);
    }

    private String getStatusStyleClass(Etat etat) {
        return switch (etat) {
            case DISPONIBLE -> "status-disponible";
            case HORS_SERVICE -> "status-hors-service";
            case EN_MAINTENANCE -> "status-maintenance";
        };
    }

    private VBox createMetroCard(Metro metro) {
        VBox card = new VBox();
        card.getStyleClass().add("vehicle-card");
        card.setSpacing(10);

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("card-header");
        header.setSpacing(10); // Vous pouvez ajuster l'espacement global si nécessaire
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Metro");
        titleLabel.getStyleClass().add("card-title");

        // Créez un Region pour occuper l'espace entre les labels
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS); // Le Region s'étendra pour occuper tout l'espace disponible

        Label statusLabel = new Label(metro.getEtat().toString());
        statusLabel.getStyleClass().addAll("status-badge", getStatusStyleClass(metro.getEtat()));

        // Ajoutez les éléments à l'HBox dans l'ordre souhaité
        header.getChildren().addAll(titleLabel, spacer1, statusLabel);

        // Content
        GridPane content = new GridPane();
        content.getStyleClass().add("card-content");
        content.setHgap(15);
        content.setVgap(10);
        content.setPadding(new Insets(15));

        // Basic Info
        addFieldToGrid(content, 0, "Immatriculation:", metro.getImmatriculation());
        addFieldToGrid(content, 1, "Capacité:", String.valueOf(metro.getCapacite()));

        // Conducteur Info
        addFieldToGrid(content, 2, "Conducteur:",
                vehiculeService.getConducteurNomById(metro.getIdConducteur()) + " " +
                        vehiculeService.getConducteurPrenomById(metro.getIdConducteur()));

        // Trajet Info
        addFieldToGrid(content, 3, "Trajet:",
                "Départ: " + vehiculeService.getTrajetDepartById(metro.getIdTrajet()) + "\n" +
                        "Arrêt: " + vehiculeService.getTrajetArretById(metro.getIdTrajet()));

        // Spécifications
        addFieldToGrid(content, 4, "Spécifications:",
                "Longueur réseau: " + metro.getLongueurReseau() + " km\n" +
                        "Lignes: " + metro.getNombreLignes() + "\n" +
                        "Rames: " + metro.getNombreRames() + "\n" +
                        "Propriétaire: " + metro.getProprietaire());

        HBox actions = new HBox();
        actions.getStyleClass().add("card-actions");
        actions.setAlignment(Pos.CENTER); // Centrer les boutons dans la HBox
        actions.setSpacing(10); // Espacement entre les boutons

// Bouton Modifier
        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("edit-button");
        Region editIcon = new Region();
        editIcon.getStyleClass().add("edit-icon");
        editButton.setGraphic(editIcon);
        editButton.setUserData(metro); // Stocke l'objet metro dans le bouton

// Espaceur pour pousser les boutons vers les extrémités
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS); // L'espaceur occupe tout l'espace disponible

// Bouton Trajet
        Button showRouteButton = new Button("Trajet");
        showRouteButton.getStyleClass().add("trajet-button");
        Region routeIcon = new Region();
        routeIcon.getStyleClass().add("route-icon");
        showRouteButton.setGraphic(routeIcon);
        showRouteButton.setOnAction((event -> {
            // Afficher la carte avec le trajet
            showMapInBrowser(vehiculeService.getTrajetDepartById(metro.getIdTrajet()), vehiculeService.getTrajetArretById(metro.getIdTrajet()) + ", Tunisia");
        }));

// Bouton Supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setPrefHeight(26.0);
        deleteButton.setPrefWidth(110.0);
        Region deleteIcon = new Region();
        deleteIcon.getStyleClass().add("delete-icon");
        deleteButton.setGraphic(deleteIcon);
        deleteButton.setUserData(metro); // Stocke l'objet metro dans le bouton
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        editButton.setOnAction(e -> openEditDialogMetro(metro));
        deleteButton.setOnAction(e -> handleDelete(metro));


// Ajouter les boutons à la HBox dans l'ordre souhaité
        actions.getChildren().addAll(editButton, spacer2, showRouteButton, spacer, deleteButton);

// Ajouter les actions à la carte
        card.getChildren().addAll(header, content, actions);
        card.setUserData(metro);
        return card;
    }

    private VBox createTrainCard(Train train) {
        VBox card = new VBox();
        card.getStyleClass().add("vehicle-card");

        // Header
        HBox header = new HBox();
        header.getStyleClass().add("card-header");
        header.setSpacing(10); // Vous pouvez ajuster l'espacement global si nécessaire
        header.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Train");
        titleLabel.getStyleClass().add("card-title");

// Créez un Region pour occuper l'espace entre les labels
        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS); // Le Region s'étendra pour occuper tout l'espace disponible

        Label statusLabel = new Label(train.getEtat().toString());
        statusLabel.getStyleClass().addAll("status-badge", getStatusStyleClass(train.getEtat()));

// Ajoutez les éléments à l'HBox dans l'ordre souhaité
        header.getChildren().addAll(titleLabel, spacer1, statusLabel);

        // Content
        GridPane content = new GridPane();
        content.getStyleClass().add("card-content");
        content.setHgap(15);
        content.setVgap(10);
        content.setPadding(new Insets(15));

        // Basic Info
        addFieldToGrid(content, 0, "Immatriculation:", train.getImmatriculation());
        addFieldToGrid(content, 1, "Capacité:", String.valueOf(train.getCapacite()));

        // Conducteur Info
        addFieldToGrid(content, 2, "Conducteur:",
                vehiculeService.getConducteurNomById(train.getIdConducteur()) + " " +
                        vehiculeService.getConducteurPrenomById(train.getIdConducteur()));

        // Trajet Info
        addFieldToGrid(content, 3, "Trajet:",
                "Départ: " + vehiculeService.getTrajetDepartById(train.getIdTrajet()) + "\n" +
                        "Arrêt: " + vehiculeService.getTrajetArretById(train.getIdTrajet()));

        // Spécifications
        addFieldToGrid(content, 4, "Spécifications:",
                "Longueur réseau: " + train.getLongueurReseau() + " km\n" +
                        "Lignes: " + train.getNombreLignes() + "\n" +
                        "Wagons: " + train.getNombreWagons() + "\n" +
                        "Vitesse Max: " + train.getVitesseMaximale() + " km/h\n" +
                        "Propriétaire: " + train.getProprietaire());

        // Actions
        HBox actions = new HBox();
        actions.getStyleClass().add("card-actions");

        // Création des boutons à partir du FXML
        Button editButton = new Button("Modifier");
        editButton.getStyleClass().add("edit-button");
        Region editIcon = new Region();
        editIcon.getStyleClass().add("edit-icon");
        editButton.setGraphic(editIcon);
        editButton.setUserData(train); // Stocke l'objet bus dans le bouton

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteButton = new Button("Supprimer");
        deleteButton.getStyleClass().add("delete-button");
        deleteButton.setPrefHeight(26.0);
        deleteButton.setPrefWidth(110.0);
        Region deleteIcon = new Region();
        deleteIcon.getStyleClass().add("delete-icon");
        deleteButton.setGraphic(deleteIcon);
        deleteButton.setUserData(train); // Stocke l'objet bus dans le bouton
        Button showRouteButton = new Button("Trajet");
        showRouteButton.getStyleClass().add("trajet-button");
        Region routeIcon = new Region();
        routeIcon.getStyleClass().add("route-icon");
        showRouteButton.setGraphic(routeIcon);
        showRouteButton.setOnAction((event -> {
            // Afficher la carte avec le trajet
            showMapInBrowser(vehiculeService.getTrajetDepartById(train.getIdTrajet()), vehiculeService.getTrajetArretById(train.getIdTrajet()) + ", Tunisia");
        }));

        editButton.setOnAction(e -> openEditDialogTrain(train));
        deleteButton.setOnAction(e -> handleDelete(train));
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);


        actions.getChildren().addAll(editButton, spacer2, showRouteButton, spacer, deleteButton);

        card.getChildren().addAll(header, content, actions);
        card.setUserData(train);
        return card;
    }

    private void addFieldToGrid(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("field-label");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("field-value");
        valueNode.setWrapText(true);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    public void handleDelete(vehicule v) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer ce véhicule ?");
        alert.setContentText("Immatriculation : " + v.getImmatriculation());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                vehiculeService.deleteByImmatriculation(v.getImmatriculation());
                loadBusCards();
                loadMetroCards();
                loadTrainCards();
            }
        });
    }

    @FXML
    private void handleAddBus() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vehicule/formulaireBus.fxml"));
            Scene scene = new Scene(loader.load());

            AjouterBusController controller = loader.getController();

            // Add refresh callback
            controller.setOnBusAdded(() -> {
                loadBusCards();
            });

            // Alternative: Add window event handler
            Stage stage = new Stage();


            stage.setScene(scene);
            stage.setTitle("Ajouter un bus");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddMetro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vehicule/formulaireMetro.fxml"));
            Scene scene = new Scene(loader.load());
            AjouterMetroController controller = loader.getController();

            // Ajouter le callback de rafraîchissement
            controller.setOnMetroAdded(() -> {
                loadMetroCards();
            });

            Stage stage = new Stage();
            // Optionnel : rafraîchir à la fermeture de la fenêtre
            stage.setOnHidden(event -> {
                loadMetroCards();
            });

            stage.setScene(scene);
            stage.setTitle("Ajouter un Metro");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleAddTrain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vehicule/formulaireTrain.fxml"));
            Scene scene = new Scene(loader.load());
            AjouterTrainController controller = loader.getController();

            // Ajouter le callback de rafraîchissement
            controller.setOnTrainAdded(() -> {
                loadTrainCards();
            });

            Stage stage = new Stage();
            // Optionnel : rafraîchir à la fermeture de la fenêtre
            stage.setOnHidden(event -> {
                loadTrainCards();
            });

            stage.setScene(scene);
            stage.setTitle("Ajouter un Train");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private TextField createStyledTextField(String style) {
        TextField field = new TextField();
        field.setStyle(style);
        return field;
    }

    private ComboBox<String> createStyledComboBox(String style) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setStyle(style);
        return comboBox;
    }

    private void addSectionLabel(GridPane grid, String text, String style, int row) {
        Label label = new Label(text);
        label.setStyle(style);
        grid.add(label, 0, row, 2, 1);
    }

    private void addStyledField(GridPane grid, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        label.setStyle("""
            -fx-font-size: 13px;
            -fx-text-fill: #555555;
            -fx-padding: 0 10 0 0;
            """);
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }

    private void addField(GridPane grid, String labelText, Control field, int row) {
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 12px;");
        grid.add(label, 0, row);
        grid.add(field, 1, row);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openEditDialogBus(Bus bus) {
        Dialog<Bus> dialog = new Dialog<>();
        dialog.setTitle("Modifier le bus");
        dialog.setHeaderText("Modifier les informations du bus");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Initialisation des champs
        TextField immatriculationField = new TextField(bus.getImmatriculation());
        TextField capaciteField = new TextField(String.valueOf(bus.getCapacite()));
        ComboBox<String> etatBox = new ComboBox<>();
        etatBox.getItems().addAll("DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE");
        etatBox.setValue(bus.getEtat().toString());

        TextField nombrePortesField = new TextField(String.valueOf(bus.getNombrePortes()));
        ComboBox<String> typeServiceBox = new ComboBox<>();
        typeServiceBox.getItems().addAll("URBAIN", "INTERURBAIN");
        typeServiceBox.setValue(bus.getTypeService().toString());

        TextField nombreDePlacesField = new TextField(String.valueOf(bus.getNombreDePlaces()));
        TextField compagnieField = new TextField(bus.getCompagnie());
        CheckBox climatisationCheckBox = new CheckBox();
        climatisationCheckBox.setSelected(bus.isClimatisation());

        TextField nomConducteurField = new TextField(vehiculeService.getConducteurNomById(bus.getIdConducteur()));
        TextField prenomConducteurField = new TextField(vehiculeService.getConducteurPrenomById(bus.getIdConducteur()));
        TextField departField = new TextField(vehiculeService.getTrajetDepartById(bus.getIdTrajet()));
        TextField arretField = new TextField(vehiculeService.getTrajetArretById(bus.getIdTrajet()));

        // Ajout des champs à la grille avec des labels descriptifs
        int row = 0;

        // Informations de base
        grid.add(new Label("Informations de base"), 0, row, 2, 1);
        grid.add(new Label("Immatriculation:"), 0, ++row);
        grid.add(immatriculationField, 1, row);
        grid.add(new Label("Capacité:"), 0, ++row);
        grid.add(capaciteField, 1, row);
        grid.add(new Label("État:"), 0, ++row);
        grid.add(etatBox, 1, row);

        // Spécifications du bus
        grid.add(new Label("Spécifications"), 0, ++row, 2, 1);
        grid.add(new Label("Nombre de portes:"), 0, ++row);
        grid.add(nombrePortesField, 1, row);
        grid.add(new Label("Type de service:"), 0, ++row);
        grid.add(typeServiceBox, 1, row);
        grid.add(new Label("Nombre de places:"), 0, ++row);
        grid.add(nombreDePlacesField, 1, row);
        grid.add(new Label("Compagnie:"), 0, ++row);
        grid.add(compagnieField, 1, row);
        grid.add(new Label("Climatisation:"), 0, ++row);
        grid.add(climatisationCheckBox, 1, row);

        // Informations du conducteur
        grid.add(new Label("Conducteur"), 0, ++row, 2, 1);
        grid.add(new Label("Nom:"), 0, ++row);
        grid.add(nomConducteurField, 1, row);
        grid.add(new Label("Prénom:"), 0, ++row);
        grid.add(prenomConducteurField, 1, row);

        // Informations du trajet
        grid.add(new Label("Trajet"), 0, ++row, 2, 1);
        grid.add(new Label("Départ:"), 0, ++row);
        grid.add(departField, 1, row);
        grid.add(new Label("Arrêt:"), 0, ++row);
        grid.add(arretField, 1, row);

        // Style des titres de section
        grid.getChildren().stream()
                .filter(node -> node instanceof Label)
                .map(node -> (Label) node)
                .filter(label -> label.getText().matches("(Informations de base|Spécifications|Conducteur|Trajet)"))
                .forEach(label -> {
                    label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 0 5 0;");
                });

        dialog.getDialogPane().setContent(grid);

        // Initialisation de Twilio
        Twilio.init("ACbd76408a41c47b98e14ee113fc7f4b93", "e6198f395d107e5ba9850cbab9ff3a3e");

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String oldEtat = bus.getEtat().toString(); // État avant modification
                    bus.setImmatriculation(immatriculationField.getText());
                    bus.setCapacite(Integer.parseInt(capaciteField.getText()));
                    bus.setEtat(Etat.valueOf(etatBox.getValue()));
                    bus.setNombrePortes(Integer.parseInt(nombrePortesField.getText()));
                    bus.setTypeService(TypeService.valueOf(typeServiceBox.getValue()));
                    bus.setNombreDePlaces(Integer.parseInt(nombreDePlacesField.getText()));
                    bus.setCompagnie(compagnieField.getText());
                    bus.setClimatisation(climatisationCheckBox.isSelected());

                    int conducteurId = vehiculeService.getConducteurId(
                            nomConducteurField.getText(),
                            prenomConducteurField.getText()
                    );
                    int trajetId = vehiculeService.getTrajetId(
                            departField.getText(),
                            arretField.getText()
                    );

                    if (conducteurId != -1 && trajetId != -1) {
                        bus.setIdConducteur(conducteurId);
                        bus.setIdTrajet(trajetId);

                        // Vérifie si l'état a changé
                        if (!oldEtat.equals(bus.getEtat().toString())) {
                            String messageBody = "";
                            switch (bus.getEtat()) {
                                case DISPONIBLE:
                                    messageBody = "Votre bus est maintenant DISPONIBLE pour les trajets.";
                                    break;
                                case HORS_SERVICE:
                                    messageBody = "Votre bus est actuellement HORS SERVICE. Veuillez vérifier sa disponibilité.";
                                    break;
                                case EN_MAINTENANCE:
                                    messageBody = "Votre bus est en MAINTENANCE et n'est pas disponible pour le moment.";
                                    break;
                                default:
                                    messageBody = "L'état du bus a été mis à jour.";
                                    break;
                            }
                            // Envoi d'un SMS uniquement si l'état a changé
                            envoyerSms("Le statut de votre bus a été mis à jour en : " + bus.getEtat().toString());
                        }
                        return bus;
                    } else {
                        showAlert("Erreur", "Conducteur ou trajet non trouvé");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedBus -> {
            vehiculeService.update(updatedBus);
            loadBusCards();
        });
    }

    private void envoyerSms(String messageBody) {
        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber("+21628897633"),
                    new com.twilio.type.PhoneNumber("+19896013337"),  // Ton numéro Twilio
                    messageBody  // Message
            ).create();

            System.out.println("Message SID: " + message.getSid());  // Affiche l'ID du message pour suivi
        } catch (Exception e) {
            e.printStackTrace();  // Gérer les exceptions
        }
    }

    private void openEditDialogMetro(Metro metro) {
        Dialog<Metro> dialog = new Dialog<>();
        dialog.setTitle("Modifier le métro");
        dialog.setHeaderText("Modifier les informations du métro");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Création des champs
        TextField immatriculationField = new TextField(metro.getImmatriculation());
        TextField capaciteField = new TextField(String.valueOf(metro.getCapacite()));
        ComboBox<String> etatBox = new ComboBox<>();
        etatBox.getItems().addAll("DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE");
        etatBox.setValue(metro.getEtat().toString());

        TextField longuerReseauField = new TextField(String.valueOf(metro.getLongueurReseau()));
        TextField nombreLignesField = new TextField(String.valueOf(metro.getNombreLignes()));
        TextField proprietaireField = new TextField(metro.getProprietaire());
        TextField ramesField = new TextField(String.valueOf(metro.getNombreRames()));

        TextField nomConducteurField = new TextField(vehiculeService.getConducteurNomById(metro.getIdConducteur()));
        TextField prenomConducteurField = new TextField(vehiculeService.getConducteurPrenomById(metro.getIdConducteur()));
        TextField departField = new TextField(vehiculeService.getTrajetDepartById(metro.getIdTrajet()));
        TextField arretField = new TextField(vehiculeService.getTrajetArretById(metro.getIdTrajet()));

        // Ajout des champs à la grille
        int row = 0;
        grid.add(new Label("Immatriculation:"), 0, row);
        grid.add(immatriculationField, 1, row++);
        grid.add(new Label("Capacité:"), 0, row);
        grid.add(capaciteField, 1, row++);
        grid.add(new Label("État:"), 0, row);
        grid.add(etatBox, 1, row++);
        grid.add(new Label("Longueur Réseau (km):"), 0, row);
        grid.add(longuerReseauField, 1, row++);
        grid.add(new Label("Nombre de lignes:"), 0, row);
        grid.add(nombreLignesField, 1, row++);
        grid.add(new Label("Nombre de rames:"), 0, row);
        grid.add(ramesField, 1, row++);
        grid.add(new Label("Propriétaire:"), 0, row);
        grid.add(proprietaireField, 1, row++);
        grid.add(new Label("Nom Conducteur:"), 0, row);
        grid.add(nomConducteurField, 1, row++);
        grid.add(new Label("Prénom Conducteur:"), 0, row);
        grid.add(prenomConducteurField, 1, row++);
        grid.add(new Label("Départ:"), 0, row);
        grid.add(departField, 1, row++);
        grid.add(new Label("Arrêt:"), 0, row);
        grid.add(arretField, 1, row);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Vérification des champs vides
                if (immatriculationField.getText().isEmpty() || capaciteField.getText().isEmpty() ||
                        longuerReseauField.getText().isEmpty() || nombreLignesField.getText().isEmpty() ||
                        proprietaireField.getText().isEmpty() || ramesField.getText().isEmpty() ||
                        nomConducteurField.getText().isEmpty() || prenomConducteurField.getText().isEmpty() ||
                        departField.getText().isEmpty() || arretField.getText().isEmpty()) {
                    showAlert("Champs obligatoires", "Veuillez remplir tous les champs !");
                    return null;
                }

                try {
                    // Conversion et validation des valeurs numériques
                    int capacite = Integer.parseInt(capaciteField.getText());
                    int nombreLignes = Integer.parseInt(nombreLignesField.getText());
                    double longueurReseau = Double.parseDouble(longuerReseauField.getText());
                    int nombreRames = Integer.parseInt(ramesField.getText());

                    if (capacite <= 0 || nombreLignes <= 0 || longueurReseau <= 0 || nombreRames <= 0) {
                        showAlert("Valeurs invalides", "Les valeurs numériques doivent être positives !");
                        return null;
                    }

                    // Vérification du conducteur et du trajet
                    int conducteurId = vehiculeService.getConducteurId(
                            nomConducteurField.getText(),
                            prenomConducteurField.getText()
                    );
                    int trajetId = vehiculeService.getTrajetId(
                            departField.getText(),
                            arretField.getText()
                    );

                    if (conducteurId == -1) {
                        showAlert("Conducteur introuvable",
                                "Le conducteur " + nomConducteurField.getText() + " " +
                                        prenomConducteurField.getText() + " n'existe pas !");
                        return null;
                    }

                    if (trajetId == -1) {
                        showAlert("Trajet non disponible",
                                "Le trajet " + departField.getText() + " à " +
                                        arretField.getText() + " n'existe pas !");
                        return null;
                    }

                    // Vérification si l'état du métro a changé
                    String oldEtat = metro.getEtat().toString();
                    metro.setImmatriculation(immatriculationField.getText());
                    metro.setCapacite(capacite);
                    metro.setEtat(Etat.valueOf(etatBox.getValue()));
                    metro.setLongueurReseau(longueurReseau);
                    metro.setNombreLignes(nombreLignes);
                    metro.setNombreRames(nombreRames);
                    metro.setProprietaire(proprietaireField.getText());
                    metro.setIdConducteur(conducteurId);
                    metro.setIdTrajet(trajetId);
                    // Initialisation de Twilio
                    Twilio.init("ACbd76408a41c47b98e14ee113fc7f4b93", "e6198f395d107e5ba9850cbab9ff3a3e");

                    // Si l'état a changé, envoyer un SMS
                    if (!oldEtat.equals(metro.getEtat().toString())) {
                        String messageBody = "";
                        switch (metro.getEtat()) {
                            case DISPONIBLE:
                                messageBody = "Le métro est maintenant DISPONIBLE pour les trajets.";
                                break;
                            case HORS_SERVICE:
                                messageBody = "Le métro est actuellement HORS SERVICE. Veuillez vérifier sa disponibilité.";
                                break;
                            case EN_MAINTENANCE:
                                messageBody = "Le métro est en MAINTENANCE et n'est pas disponible pour le moment.";
                                break;
                            default:
                                messageBody = "Le statut du métro a été mis à jour.";
                                break;
                        }

                        // Envoi du SMS
                        envoyerSms("Le statut de votre métro a été mis à jour : " + messageBody);
                    }

                    return metro;

                } catch (NumberFormatException e) {
                    showAlert("Erreur de format",
                            "Veuillez entrer des nombres valides pour la capacité, les lignes, " +
                                    "la longueur du réseau et les rames !");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedMetro -> {
            vehiculeService.update(updatedMetro);
            loadMetroCards(); // Mise à jour de l'affichage des cards
        });
    }


    private void openEditDialogTrain(Train train) {
        Dialog<Train> dialog = new Dialog<>();
        dialog.setTitle("Modifier le train");
        dialog.setHeaderText("Modifier les informations du train");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField immatriculationField = new TextField(train.getImmatriculation());
        TextField capaciteField = new TextField(String.valueOf(train.getCapacite()));
        ComboBox<String> etatBox = new ComboBox<>();
        etatBox.getItems().addAll("DISPONIBLE", "HORS_SERVICE", "EN_MAINTENANCE");
        etatBox.setValue(train.getEtat().toString());

        TextField longuerReseauField = new TextField(String.valueOf(train.getLongueurReseau()));
        TextField nombreLignesField = new TextField(String.valueOf(train.getNombreLignes()));
        TextField proprietaireField = new TextField(train.getProprietaire());
        TextField arretField = new TextField(vehiculeService.getTrajetArretById(train.getIdTrajet()));
        TextField departField = new TextField(vehiculeService.getTrajetDepartById(train.getIdTrajet()));
        TextField wagonsField = new TextField(String.valueOf(train.getNombreWagons()));
        TextField vitesseField = new TextField(String.valueOf(train.getVitesseMaximale()));

        TextField nomConducteurField = new TextField(vehiculeService.getConducteurNomById(train.getIdConducteur()));
        TextField prenomConducteurField = new TextField(vehiculeService.getConducteurPrenomById(train.getIdConducteur()));

        // Ajout des champs à la grille
        grid.add(new Label("Immatriculation:"), 0, 0);
        grid.add(immatriculationField, 1, 0);
        grid.add(new Label("Capacité:"), 0, 1);
        grid.add(capaciteField, 1, 1);
        grid.add(new Label("État:"), 0, 2);
        grid.add(etatBox, 1, 2);
        grid.add(new Label("Nombre de lignes:"), 0, 3);
        grid.add(nombreLignesField, 1, 3);
        grid.add(new Label("Longueur Réseau (km):"), 0, 4);
        grid.add(longuerReseauField, 1, 4);
        grid.add(new Label("Propriétaire:"), 0, 5);
        grid.add(proprietaireField, 1, 5);
        grid.add(new Label("Nombre de Wagons:"), 0, 6);
        grid.add(wagonsField, 1, 6);
        grid.add(new Label("Nom Conducteur:"), 0, 7);
        grid.add(nomConducteurField, 1, 7);
        grid.add(new Label("Prénom Conducteur:"), 0, 8);
        grid.add(prenomConducteurField, 1, 8);
        grid.add(new Label("Départ:"), 0, 9);
        grid.add(departField, 1, 9);
        grid.add(new Label("Arrêt:"), 0, 10);
        grid.add(arretField, 1, 10);
        grid.add(new Label("Vitesse Maximale (km/h):"), 0, 11);
        grid.add(vitesseField, 1, 11);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Vérification des champs vides
                if (immatriculationField.getText().isEmpty() || capaciteField.getText().isEmpty() ||
                        etatBox.getValue().isEmpty() || nombreLignesField.getText().isEmpty() ||
                        longuerReseauField.getText().isEmpty() || proprietaireField.getText().isEmpty() ||
                        wagonsField.getText().isEmpty() || nomConducteurField.getText().isEmpty() ||
                        prenomConducteurField.getText().isEmpty() || departField.getText().isEmpty() ||
                        arretField.getText().isEmpty() || vitesseField.getText().isEmpty()) {
                    showAlert("Erreur de saisie", "Veuillez remplir tous les champs obligatoires !");
                    return null;
                }

                // Validation des valeurs numériques
                try {
                    int capacite = Integer.parseInt(capaciteField.getText());
                    int nombreLignes = Integer.parseInt(nombreLignesField.getText());
                    int nombreWagons = Integer.parseInt(wagonsField.getText());
                    double longueurReseau = Double.parseDouble(longuerReseauField.getText());
                    double vitesse = Double.parseDouble(vitesseField.getText());

                    if (capacite <= 0 || nombreLignes <= 0 || nombreWagons <= 0 ||
                            longueurReseau <= 0 || vitesse <= 0) {
                        showAlert("Erreur de saisie", "Les valeurs numériques doivent être positives !");
                        return null;
                    }

                    // Vérification du conducteur et du trajet
                    int conducteurId = vehiculeService.getConducteurId(
                            nomConducteurField.getText(),
                            prenomConducteurField.getText()
                    );
                    int trajetId = vehiculeService.getTrajetId(
                            departField.getText(),
                            arretField.getText()
                    );

                    if (conducteurId == -1) {
                        showAlert("Conducteur introuvable",
                                "Le conducteur " + nomConducteurField.getText() + " " +
                                        prenomConducteurField.getText() + " n'existe pas !");
                        return null;
                    }

                    if (trajetId == -1) {
                        showAlert("Trajet non disponible",
                                "Le trajet " + departField.getText() + " à " +
                                        arretField.getText() + " n'existe pas !");
                        return null;
                    }

                    String oldEtat = train.getEtat().toString();


                    // Mise à jour des données du train
                    train.setImmatriculation(immatriculationField.getText());
                    train.setCapacite(capacite);
                    train.setEtat(Etat.valueOf(etatBox.getValue()));
                    train.setNombreLignes(nombreLignes);
                    train.setLongueurReseau(longueurReseau);
                    train.setProprietaire(proprietaireField.getText());
                    train.setNombreWagons(nombreWagons);
                    train.setVitesseMaximale(vitesse);
                    train.setIdConducteur(conducteurId);
                    train.setIdTrajet(trajetId);

                    // Initialisation de Twilio
                    Twilio.init("ACbd76408a41c47b98e14ee113fc7f4b93", "e6198f395d107e5ba9850cbab9ff3a3e");

                    if (!oldEtat.equals(train.getEtat().toString())) {
                        String messageBody = "";
                        switch (train.getEtat()) {
                            case DISPONIBLE:
                                messageBody = "Le train est maintenant DISPONIBLE pour les trajets.";
                                break;
                            case HORS_SERVICE:
                                messageBody = "Le train est actuellement HORS SERVICE. Veuillez vérifier sa disponibilité.";
                                break;
                            case EN_MAINTENANCE:
                                messageBody = "Le train est en MAINTENANCE et n'est pas disponible pour le moment.";
                                break;
                            default:
                                messageBody = "Le statut du train a été mis à jour.";
                                break;
                        }

                        // Envoi du SMS
                        envoyerSms("Le statut de votre train a été mis à jour : " + messageBody);
                    }

                    return train;


                } catch (NumberFormatException e) {
                    showAlert("Erreur de saisie",
                            "Veuillez entrer des nombres valides pour la capacité, le nombre de lignes, " +
                                    "le nombre de wagons, la longueur du réseau et la vitesse !");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedTrain -> {
            vehiculeService.update(updatedTrain);
            loadTrainCards(); // Mise à jour de l'affichage des cards
        });
    }
    private void updateVehicule(vehicule vehicule) {
        ServiceVehicule vehiculeService = new ServiceVehicule();
        vehiculeService.update(vehicule);
    }

    private HostServices hostServices; // Inject HostServices into your class

    // Constructor or method to set HostServices
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    private void showMapInBrowser(String startAddress, String endAddress) {
        try {
            // Get the coordinates for the start address
            double[] startCoords = Map.getCoordinates(startAddress);
            // Get the coordinates for the end address
            double[] endCoords = Map.getCoordinates(endAddress);

            // Check if coordinates were found for both addresses
            if (startCoords[0] != 0 && startCoords[1] != 0 && endCoords[0] != 0 && endCoords[1] != 0) {
                double startLat = startCoords[0];
                double startLng = startCoords[1];
                double endLat = endCoords[0];
                double endLng = endCoords[1];

                // Generate a URL for the map with a route between the two points
                String mapUrl = generateRouteUrl(startLat, startLng, endLat, endLng);

                // Debug: Print the URL
                System.out.println("Generated Map URL:\n" + mapUrl);

                // Open the URL in the default browser
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(mapUrl));
                    System.out.println("Map opened in the default browser.");
                } else {
                    System.out.println("Desktop browsing is not supported on this platform.");
                }
            } else {
                System.out.println("No valid coordinates found for one or both addresses.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate a route URL
    private String generateRouteUrl(double startLat, double startLng, double endLat, double endLng) {
        // Example: OpenStreetMap OSRM URL
        //return "https://www.openstreetmap.org/directions?engine=osrm_car&route="
        //  + startLat + "%2C" + startLng + "%3B" + endLat + "%2C" + endLng;

        // Alternatively, for Google Maps:
        return "https://www.google.com/maps/dir/?api=1&origin=" + startLat + "," + startLng + "&destination=" + endLat + "," + endLng;
    }

    // Helper method to generate a map URL
    private String generateMapUrl(double latitude, double longitude, String address) {
        // Example: OpenStreetMap URL
        return "https://www.openstreetmap.org/?mlat=" + latitude + "&mlon=" + longitude + "#map=15/" + latitude + "/" + longitude;

        // Alternatively, for Google Maps:
        // return "https://www.google.com/maps?q=" + latitude + "," + longitude;
    }


    public void exportBusToPdf(VBox busCard, String filePath) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Titre principal
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLUE);
            Paragraph title = new Paragraph("Détails du Bus", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Tableau pour organiser les informations
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            // Ajout des données de la VBox
            for (Node node : busCard.getChildren()) {
                if (node instanceof HBox) {
                    extractDataFromHBox((HBox) node, table);
                } else if (node instanceof GridPane) {
                    extractDataFromGridPane((GridPane) node, table);
                }
            }

            document.add(table);

            // Pied de page
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Généré par l'application EasyWay", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();

            // Ouvrir automatiquement le PDF
            openPdf(filePath);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'exportation en PDF : " + e.getMessage());
        }
    }

    // Méthode pour extraire les données d'un HBox et les ajouter au tableau
    private void extractDataFromHBox(HBox hbox, PdfPTable table) {
        for (Node child : hbox.getChildren()) {
            if (child instanceof Label) {
                Label label = (Label) child;
                addTableRow(table, label.getText());
            }
        }
    }

    // Méthode pour extraire les données d'un GridPane et les ajouter au tableau
    private void extractDataFromGridPane(GridPane gridPane, PdfPTable table) {
        for (Node child : gridPane.getChildren()) {
            if (child instanceof Label) {
                Label label = (Label) child;
                addTableRow(table, label.getText());
            }
        }
    }

    // Ajouter une ligne dans le tableau
    private void addTableRow(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 12)));
        cell.setBorderWidth(1);
        cell.setPadding(5);
        table.addCell(cell);
    }

    // Ouvrir le PDF après la génération
    private void openPdf(String filePath) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(Paths.get(filePath).toFile());
            } else {
                System.out.println("Desktop n'est pas supporté sur cette plateforme.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le PDF : " + e.getMessage());
        }
    }
    // Méthode pour afficher une alerte (JavaFX)
    private boolean matches(Node card, TypeService service, String state, String trajet, String conducteur) {
        // Récupérer l'objet associé à la carte
        Object userData = card.getUserData();

        // Vérifier si l'objet est une instance de Bus
        if (userData instanceof Bus) {
            Bus bus = (Bus) userData; // Caster en Bus

            // Vérifier le type de service
            boolean serviceMatch = (service == null) || (bus.getTypeService() == service);

            // Vérifier l'état
            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true; // Aucun filtre par état sélectionné
            } else {
                try {
                    Etat etat = Etat.valueOf(state); // Convertir la chaîne en enum
                    stateMatch = (bus.getEtat() == etat); // Comparer l'état
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            // Vérifier le trajet
            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true; // Aucun filtre par trajet sélectionné
            } else {
                String trajetBus = vehiculeService.getTrajetDepartById(bus.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(bus.getIdTrajet());
                trajetMatch = trajetBus.equals(trajet); // Comparer le trajet
            }

            // Vérifier le conducteur
            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true; // Aucun filtre par conducteur sélectionné
            } else {
                String conducteurBus = vehiculeService.getConducteurNomById(bus.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(bus.getIdConducteur());
                conducteurMatch = conducteurBus.equals(conducteur); // Comparer le conducteur
            }

            // Retourner true si tous les critères sont satisfaits
            return serviceMatch && stateMatch && trajetMatch && conducteurMatch;
        }

        return false; // Si ce n'est pas un Bus, exclure la carte
    }

    private void applyFilter(TypeService selectedService, String selectedState, String selectedTrajet, String selectedConducteur) {
        System.out.println("Application du filtre pour le service : " + selectedService +
                ", l'état : " + selectedState +
                ", le trajet : " + selectedTrajet +
                ", le conducteur : " + selectedConducteur);

        // Réinitialiser les cartes affichées
        busCardsContainer.getChildren().clear();

        // Filtrer les cartes en fonction des critères sélectionnés
        List<Node> filteredCards = allCards.stream()
                .filter(card -> matches(card, selectedService, selectedState, selectedTrajet, selectedConducteur))
                .collect(Collectors.toList());

        System.out.println("Nombre de cartes filtrées : " + filteredCards.size());

        // Ajouter les cartes filtrées au FlowPane
        busCardsContainer.getChildren().addAll(filteredCards);
    }

    private boolean matchesMetro(Node card, String proprietaire, String state, String trajet, String conducteur) {
        // Récupérer l'objet associé à la carte
        Object userData = card.getUserData();

        // Vérifier si l'objet est une instance de Metro
        if (userData instanceof Metro) {
            Metro metro = (Metro) userData; // Caster en Metro

            // Vérifier le propriétaire
            boolean proprietaireMatch = (proprietaire == null || "Tous".equals(proprietaire)) ||
                    metro.getProprietaire().equals(proprietaire);

            // Vérifier l'état
            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true; // Aucun filtre par état sélectionné
            } else {
                try {
                    Etat etat = Etat.valueOf(state); // Convertir la chaîne en enum
                    stateMatch = (metro.getEtat() == etat); // Comparer l'état
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            // Vérifier le trajet
            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true; // Aucun filtre par trajet sélectionné
            } else {
                String trajetMetro = vehiculeService.getTrajetDepartById(metro.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(metro.getIdTrajet());
                trajetMatch = trajetMetro.equals(trajet); // Comparer le trajet
            }

            // Vérifier le conducteur
            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true; // Aucun filtre par conducteur sélectionné
            } else {
                String conducteurMetro = vehiculeService.getConducteurNomById(metro.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(metro.getIdConducteur());
                conducteurMatch = conducteurMetro.equals(conducteur); // Comparer le conducteur
            }

            // Retourner true si tous les critères sont satisfaits
            return proprietaireMatch && stateMatch && trajetMatch && conducteurMatch;
        }

        return false; // Si ce n'est pas un Metro, exclure la carte
    }

    private void applyMetroFilter(String selectedProprietaire, String selectedState, String selectedTrajet, String selectedConducteur) {
        System.out.println("Application du filtre pour le propriétaire : " + selectedProprietaire +
                ", l'état : " + selectedState +
                ", le trajet : " + selectedTrajet +
                ", le conducteur : " + selectedConducteur);

        // Réinitialiser les cartes affichées
        metroCardsContainer.getChildren().clear();

        // Filtrer les cartes en fonction des critères sélectionnés
        List<Node> filteredCards = allMetroCards.stream()
                .filter(card -> matchesMetro(card, selectedProprietaire, selectedState, selectedTrajet, selectedConducteur))
                .collect(Collectors.toList());

        System.out.println("Nombre de cartes filtrées : " + filteredCards.size());

        // Ajouter les cartes filtrées au conteneur
        metroCardsContainer.getChildren().addAll(filteredCards);
    }

    private boolean matchesTrain(Node card, String proprietaire, String state, String trajet, String conducteur) {
        // Récupérer l'objet associé à la carte
        Object userData = card.getUserData();

        // Vérifier si l'objet est une instance de Train
        if (userData instanceof Train) {
            Train train = (Train) userData; // Caster en Train

            // Vérifier le propriétaire
            boolean proprietaireMatch = (proprietaire == null || "Tous".equals(proprietaire)) ||
                    train.getProprietaire().equals(proprietaire);

            // Vérifier l'état
            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true; // Aucun filtre par état sélectionné
            } else {
                try {
                    Etat etat = Etat.valueOf(state); // Convertir la chaîne en enum
                    stateMatch = (train.getEtat() == etat); // Comparer l'état
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            // Vérifier le trajet
            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true; // Aucun filtre par trajet sélectionné
            } else {
                String trajetTrain = vehiculeService.getTrajetDepartById(train.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(train.getIdTrajet());
                trajetMatch = trajetTrain.equals(trajet); // Comparer le trajet
            }

            // Vérifier le conducteur
            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true; // Aucun filtre par conducteur sélectionné
            } else {
                String conducteurTrain = vehiculeService.getConducteurNomById(train.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(train.getIdConducteur());
                conducteurMatch = conducteurTrain.equals(conducteur); // Comparer le conducteur
            }

            // Retourner true si tous les critères sont satisfaits
            return proprietaireMatch && stateMatch && trajetMatch && conducteurMatch;
        }

        return false; // Si ce n'est pas un Train, exclure la carte
    }

    private void applyTrainFilter(String selectedProprietaire, String selectedState, String selectedTrajet, String selectedConducteur) {
        System.out.println("Application du filtre pour le propriétaire : " + selectedProprietaire +
                ", l'état : " + selectedState +
                ", le trajet : " + selectedTrajet +
                ", le conducteur : " + selectedConducteur);

        // Réinitialiser les cartes affichées
        trainCardsContainer.getChildren().clear();

        // Filtrer les cartes en fonction des critères sélectionnés
        List<Node> filteredCards = allTrainCards.stream()
                .filter(card -> matchesTrain(card, selectedProprietaire, selectedState, selectedTrajet, selectedConducteur))
                .collect(Collectors.toList());

        System.out.println("Nombre de cartes filtrées : " + filteredCards.size());

        // Ajouter les cartes filtrées au conteneur
        trainCardsContainer.getChildren().addAll(filteredCards);
    }
    private boolean matchesBusSearch(Node card, TypeService service, String state, String trajet, String conducteur, String searchQuery) {
        Object userData = card.getUserData();

        if (userData instanceof Bus) {
            Bus bus = (Bus) userData;

            boolean serviceMatch = (service == null) || (bus.getTypeService() == service);

            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true;
            } else {
                try {
                    Etat etat = Etat.valueOf(state);
                    stateMatch = (bus.getEtat() == etat);
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true;
            } else {
                String trajetBus = vehiculeService.getTrajetDepartById(bus.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(bus.getIdTrajet());
                trajetMatch = trajetBus.equals(trajet);
            }

            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true;
            } else {
                String conducteurBus = vehiculeService.getConducteurNomById(bus.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(bus.getIdConducteur());
                conducteurMatch = conducteurBus.equals(conducteur);
            }

            // Vérifier la recherche sur tous les attributs du bus
            boolean searchMatch = (searchQuery == null || searchQuery.isEmpty()) ||
                    bus.getImmatriculation().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    String.valueOf(bus.getCapacite()).contains(searchQuery) ||
                    (vehiculeService.getConducteurNomById(bus.getIdConducteur()) != null &&
                            vehiculeService.getConducteurNomById(bus.getIdConducteur()).toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (vehiculeService.getConducteurPrenomById(bus.getIdConducteur()) != null &&
                            vehiculeService.getConducteurPrenomById(bus.getIdConducteur()).toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (vehiculeService.getTrajetDepartById(bus.getIdTrajet()) != null &&
                            vehiculeService.getTrajetDepartById(bus.getIdTrajet()).toLowerCase().contains(searchQuery.toLowerCase())) ||
                    (vehiculeService.getTrajetArretById(bus.getIdTrajet()) != null &&
                            vehiculeService.getTrajetArretById(bus.getIdTrajet()).toLowerCase().contains(searchQuery.toLowerCase())) ||
                    String.valueOf(bus.getNombrePortes()).contains(searchQuery) ||
                    String.valueOf(bus.getTypeService()).toLowerCase().contains(searchQuery.toLowerCase()) ||
                    String.valueOf(bus.getNombreDePlaces()).contains(searchQuery) ||
                    bus.getCompagnie().toLowerCase().contains(searchQuery.toLowerCase()) ||
                    (bus.isClimatisation() ? "oui" : "non").contains(searchQuery.toLowerCase());

            return serviceMatch && stateMatch && trajetMatch && conducteurMatch && searchMatch;
        }

        return false;
    }

    private void applyFilterSearch(TypeService selectedService, String selectedState, String selectedTrajet, String selectedConducteur, String searchQuery) {
        System.out.println("Application du filtre avec recherche : " + searchQuery);

        busCardsContainer.getChildren().clear();

        List<Node> filteredCards = allCards.stream()
                .filter(card -> matchesBusSearch(card, selectedService, selectedState, selectedTrajet, selectedConducteur, searchQuery))
                .collect(Collectors.toList());

        System.out.println("Nombre de résultats après recherche : " + filteredCards.size());

        busCardsContainer.getChildren().addAll(filteredCards);
    }

    private boolean matchesMetroSearch(Node card, String proprietaire, String state, String trajet, String conducteur, String searchQuery) {
        // Récupérer l'objet associé à la carte
        Object userData = card.getUserData();

        // Vérifier si l'objet est une instance de Metro
        if (userData instanceof Metro) {
            Metro metro = (Metro) userData; // Caster en Metro

            // Vérifier le propriétaire
            boolean proprietaireMatch = (proprietaire == null || "Tous".equals(proprietaire)) ||
                    metro.getProprietaire().equalsIgnoreCase(proprietaire);

            // Vérifier l'état
            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true; // Aucun filtre par état sélectionné
            } else {
                try {
                    Etat etat = Etat.valueOf(state); // Convertir la chaîne en enum
                    stateMatch = (metro.getEtat() == etat); // Comparer l'état
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            // Vérifier le trajet
            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true; // Aucun filtre par trajet sélectionné
            } else {
                String trajetMetro = vehiculeService.getTrajetDepartById(metro.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(metro.getIdTrajet());
                trajetMatch = trajetMetro.equalsIgnoreCase(trajet);
            }

            // Vérifier le conducteur
            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true; // Aucun filtre par conducteur sélectionné
            } else {
                String conducteurMetro = vehiculeService.getConducteurNomById(metro.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(metro.getIdConducteur());
                conducteurMatch = conducteurMetro.equalsIgnoreCase(conducteur);
            }

            // Vérifier la recherche globale sur tous les attributs
            boolean searchMatch = (searchQuery == null || searchQuery.trim().isEmpty()) ||
                    matchesSearchQuery(metro, searchQuery.toLowerCase());

            // Retourner true si tous les critères sont satisfaits
            return proprietaireMatch && stateMatch && trajetMatch && conducteurMatch && searchMatch;
        }

        return false; // Si ce n'est pas un Metro, exclure la carte
    }

    private boolean matchesSearchQuery(Metro metro, String searchQuery) {
        return metro.getImmatriculation().toLowerCase().contains(searchQuery) ||
                metro.getProprietaire().toLowerCase().contains(searchQuery) ||
                String.valueOf(metro.getCapacite()).contains(searchQuery) ||
                safeToLower(vehiculeService.getConducteurNomById(metro.getIdConducteur())).contains(searchQuery) ||
                safeToLower(vehiculeService.getConducteurPrenomById(metro.getIdConducteur())).contains(searchQuery) ||
                safeToLower(vehiculeService.getTrajetDepartById(metro.getIdTrajet())).contains(searchQuery) ||
                safeToLower(vehiculeService.getTrajetArretById(metro.getIdTrajet())).contains(searchQuery) ||
                String.valueOf(metro.getNombreLignes()).contains(searchQuery) ||
                String.valueOf(metro.getNombreRames()).contains(searchQuery) ||
                String.valueOf(metro.getLongueurReseau()).contains(searchQuery);
    }

    // Méthode utilitaire pour éviter NullPointerException
    private String safeToLower(String value) {
        return value != null ? value.toLowerCase() : "";
    }


    private void applyMetroFilter(String selectedProprietaire, String selectedState, String selectedTrajet, String selectedConducteur, String searchQuery) {
        System.out.println("Application du filtre avec recherche : " + searchQuery);

        // Réinitialiser les cartes affichées
        metroCardsContainer.getChildren().clear();

        // Filtrer les cartes en fonction des critères sélectionnés et de la recherche
        List<Node> filteredCards = allMetroCards.stream()
                .filter(card -> matchesMetroSearch(card, selectedProprietaire, selectedState, selectedTrajet, selectedConducteur, searchQuery))
                .collect(Collectors.toList());

        System.out.println("Nombre de cartes filtrées : " + filteredCards.size());

        // Ajouter les cartes filtrées au conteneur
        metroCardsContainer.getChildren().addAll(filteredCards);
    }

    private boolean matchesTrain(Node card, String proprietaire, String state, String trajet, String conducteur, String searchQuery) {
        // Récupérer l'objet associé à la carte
        Object userData = card.getUserData();

        // Vérifier si c'est une instance de Train
        if (userData instanceof Train) {
            Train train = (Train) userData; // Caster en Train

            // Vérifier le propriétaire
            boolean proprietaireMatch = (proprietaire == null || "Tous".equals(proprietaire)) ||
                    train.getProprietaire().equalsIgnoreCase(proprietaire);

            // Vérifier l'état
            boolean stateMatch;
            if (state == null || "Tous".equals(state)) {
                stateMatch = true;
            } else {
                try {
                    Etat etat = Etat.valueOf(state);
                    stateMatch = (train.getEtat() == etat);
                } catch (IllegalArgumentException e) {
                    System.out.println("État invalide : " + state);
                    stateMatch = false;
                }
            }

            // Vérifier le trajet
            boolean trajetMatch;
            if (trajet == null || "Tous".equals(trajet)) {
                trajetMatch = true;
            } else {
                String trajetTrain = vehiculeService.getTrajetDepartById(train.getIdTrajet()) + " - " + vehiculeService.getTrajetArretById(train.getIdTrajet());
                trajetMatch = trajetTrain.equalsIgnoreCase(trajet);
            }

            // Vérifier le conducteur
            boolean conducteurMatch;
            if (conducteur == null || "Tous".equals(conducteur)) {
                conducteurMatch = true;
            } else {
                String conducteurTrain = vehiculeService.getConducteurNomById(train.getIdConducteur()) + " " + vehiculeService.getConducteurPrenomById(train.getIdConducteur());
                conducteurMatch = conducteurTrain.equalsIgnoreCase(conducteur);
            }

            // Vérifier la recherche globale sur tous les attributs
            boolean searchMatch = (searchQuery == null || searchQuery.trim().isEmpty()) ||
                    matchesSearchQuery(train, searchQuery.toLowerCase());

            // Retourner true si tous les critères sont satisfaits
            return proprietaireMatch && stateMatch && trajetMatch && conducteurMatch && searchMatch;
        }

        return false;
    }

    private boolean matchesSearchQuery(Train train, String searchQuery) {
        return train.getImmatriculation().toLowerCase().contains(searchQuery) ||
                train.getProprietaire().toLowerCase().contains(searchQuery) ||
                String.valueOf(train.getCapacite()).contains(searchQuery) ||
                Optional.ofNullable(vehiculeService.getConducteurNomById(train.getIdConducteur()))
                        .map(String::toLowerCase).orElse("").contains(searchQuery) ||
                Optional.ofNullable(vehiculeService.getConducteurPrenomById(train.getIdConducteur()))
                        .map(String::toLowerCase).orElse("").contains(searchQuery) ||
                Optional.ofNullable(vehiculeService.getTrajetDepartById(train.getIdTrajet()))
                        .map(String::toLowerCase).orElse("").contains(searchQuery) ||
                Optional.ofNullable(vehiculeService.getTrajetArretById(train.getIdTrajet()))
                        .map(String::toLowerCase).orElse("").contains(searchQuery) ||
                String.valueOf(train.getNombreLignes()).contains(searchQuery) ||
                String.valueOf(train.getNombreWagons()).contains(searchQuery) ||
                String.valueOf(train.getVitesseMaximale()).contains(searchQuery) ||
                String.valueOf(train.getLongueurReseau()).contains(searchQuery);
    }

    private void applyTrainFilterSearch(String selectedProprietaire, String selectedState, String selectedTrajet, String selectedConducteur, String searchQuery) {
        System.out.println("Application du filtre avec recherche : " + searchQuery);

        // Réinitialiser les cartes affichées
        trainCardsContainer.getChildren().clear();

        // Filtrer les cartes en fonction des critères sélectionnés et de la recherche
        List<Node> filteredCards = allTrainCards.stream()
                .filter(card -> matchesTrain(card, selectedProprietaire, selectedState, selectedTrajet, selectedConducteur, searchQuery))
                .collect(Collectors.toList());

        System.out.println("Nombre de cartes filtrées : " + filteredCards.size());

        // Ajouter les cartes filtrées au conteneur
        trainCardsContainer.getChildren().addAll(filteredCards);
    }

    @FXML
    void RedirectToEvent(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/eventTable.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void RedirectToLigne(ActionEvent event) {

    }

    @FXML
    void RedirectToTrajet(ActionEvent event) {

    }

    @FXML
    void RedirectToUsers(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UsersList.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void RedirectToReclamation(ActionEvent event) throws IOException {
        Stage stage;
        Scene scene;
        Parent root;
        // Load the new FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reclamation/CardView.fxml"));
        root = loader.load();
        // Get the stage from the event source
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        // Set the new scene and show
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void logout(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().logout();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/user/UserSpace.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }



}