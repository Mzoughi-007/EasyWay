//package tn.esprit.services.event;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import org.json.JSONObject;
//
//public class WeatherService {
//    private static final String API_KEY = "8e6295e0f2c63aabd355579fc4677ce2";
//    private static final String CITY = "Tunis"; // Change based on location
//
//    public static String getWeatherCondition() {
//        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&appid=" + API_KEY + "&units=metric";
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
//            return jsonResponse.getJSONArray("weather").getJSONObject(0).getString("main"); // Example: "Rain", "Clear", etc.
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return "Unknown";
//        }
//    }
//}
