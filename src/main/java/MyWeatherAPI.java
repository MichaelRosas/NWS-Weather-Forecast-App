import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import weather.WeatherAPI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class MyWeatherAPI extends WeatherAPI {
    public static PointProperties getPoint(double lat, double lng) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.weather.gov/points/"+String.valueOf(lat)+","+String.valueOf(lng)))
                //.method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        PointProperties p = getPointObject(response.body());
        if(p == null){
            System.err.println("Failed to parse JSon");
            return null;
        }
        return p;
    }
    public static PointProperties getPointObject(String json){
        ObjectMapper om = new ObjectMapper();
        PointProperties toRet = null;
        try {
            String propertiesJson = om.readTree(json).get("properties").toString();
            toRet = om.readValue(propertiesJson, PointProperties.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return toRet;
    }
}
