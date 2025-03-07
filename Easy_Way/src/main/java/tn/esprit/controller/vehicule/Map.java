package tn.esprit.controller.vehicule;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Map {

    // Replace with your actual OpenCage API Key
    private static final String API_KEY = "817dbe8b9965489c8dcffa41b72fe069";

    // Method to get coordinates for a given address
    public static double[] getCoordinates(String address) {
        double[] coordinates = new double[2];
        try {
            // Format the address for the URL (replace spaces with +)
            String formattedAddress = address.replace(" ", "+");

            // Build the URL to query the OpenCage API
            String urlString = "https://api.opencagedata.com/geocode/v1/json?q=" + formattedAddress
                    + "&key=" + API_KEY;

            // Create a URL object
            URL url = new URL(urlString);

            // Open the connection to the API
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the raw API response for debugging
            System.out.println("API Response: " + response.toString());

            // Parse the response to extract latitude and longitude
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Get the status object and check the code
            JSONObject status = jsonResponse.getJSONObject("status");
            int statusCode = status.getInt("code");

            if (statusCode == 200) {
                // Check if there are any results
                if (jsonResponse.getJSONArray("results").length() > 0) {
                    // Extract the first result from the response
                    JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);
                    JSONObject geometry = firstResult.getJSONObject("geometry");
                    double latitude = geometry.getDouble("lat");
                    double longitude = geometry.getDouble("lng");

                    // Return the coordinates as an array
                    coordinates[0] = latitude;
                    coordinates[1] = longitude;
                } else {
                    System.out.println("No results found for the address: " + address);
                }
            } else {
                System.out.println("Unable to geocode input address: " + address);
                System.out.println("Status Code: " + statusCode);
                System.out.println("Status Message: " + status.getString("message"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    /**
     * Generates an HTML file with a Leaflet map.
     *
     * @param latitude  The latitude of the location.
     * @param longitude The longitude of the location.
     * @param address   The formatted address to display on the map.
     * @return The HTML content as a string.
     */
    public static String generateLeafletMap(double latitude, double longitude, String address) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Carte OpenStreetMap</title>\n" +
                "    <link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.css\" />\n" +
                "    <script src=\"https://unpkg.com/leaflet@1.9.4/dist/leaflet.js\"></script>\n" +
                "    <style>\n" +
                "        #map { height: 500px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"map\"></div>\n" +
                "    <script>\n" +
                "        var map = L.map('map').setView([" + latitude + ", " + longitude + "], 15);\n" +
                "        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {\n" +
                "            attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'\n" +
                "        }).addTo(map);\n" +
                "        L.marker([" + latitude + ", " + longitude + "]).addTo(map)\n" +
                "            .bindPopup('" + address + "')\n" +
                "            .openPopup();\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}