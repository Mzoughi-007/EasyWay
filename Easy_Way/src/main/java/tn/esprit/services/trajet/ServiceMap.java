package tn.esprit.services.trajet;

import tn.esprit.models.trajet.Map;
import javafx.scene.web.WebView;

public class ServiceMap {

    public void initializeMap(WebView map) {
        String address = "Ariana, Tunisia";
        double[] coordinates = Map.getCoordinates(address);

        if (coordinates[0] != 0 && coordinates[1] != 0) {
            // Get the place name from coordinates
            String placeName = Map.getPlaceNameFromCoordinates(coordinates[0], coordinates[1]);
            System.out.println("Place Name: " + placeName); // Print place name

            // Create OpenStreetMap URL with the coordinates
            String url = "https://www.openstreetmap.org/?mlat=" + coordinates[0] + "&mlon=" + coordinates[1] + "#map=12/" + coordinates[0] + "/" + coordinates[1];
            map.getEngine().load(url); // Load map
        } else {
            System.out.println("Error: Invalid coordinates for address: " + address);
        }
    }


    public void updateMap(WebView map, String address) {
        double[] coordinates = Map.getCoordinates(address);

        if (coordinates[0] != 0 && coordinates[1] != 0) {
            // Create the OpenStreetMap URL with the coordinates
            String url = "https://www.openstreetmap.org/?mlat=" + coordinates[0] + "&mlon=" + coordinates[1] + "#map=12/" + coordinates[0] + "/" + coordinates[1];
            map.getEngine().load(url); // Load the map in the WebView
        } else {
            System.out.println("Error: Invalid coordinates for address: " + address);
        }
    }

}
