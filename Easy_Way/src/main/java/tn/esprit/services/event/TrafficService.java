//package tn.esprit.services.event;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import org.json.JSONObject;
//
//public class TrafficService {
//    private static final String API_KEY = "pryGAeAjAjr3T7ccKR85wmQfUQAOg4F4";
//    private static final String LOCATION = "10.1815,36.8065"; // Longitude, Latitude (Tunis)
//
//    public static String getTrafficCondition() {
//        String urlString = "https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?key=" + API_KEY + "&point=" + LOCATION;
//        try {
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod("GET");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = in.readLine()) != null) {
//                response.append(line);
//            }
//            in.close();
//
//            JSONObject jsonResponse = new JSONObject(response.toString());
//            double speedRatio = jsonResponse.getJSONObject("flowSegmentData").getDouble("currentSpeed") /
//                    jsonResponse.getJSONObject("flowSegmentData").getDouble("freeFlowSpeed");
//
//            if (speedRatio > 0.8) return "Light Traffic";
//            else if (speedRatio > 0.5) return "Moderate Traffic";
//            else return "Heavy Traffic";
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return "Unknown";
//        }
//    }
//}
