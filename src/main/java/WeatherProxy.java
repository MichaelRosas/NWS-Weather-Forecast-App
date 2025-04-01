import weather.Period;
import weather.WeatherAPI;
import java.util.HashMap;
import java.util.ArrayList;

class WeatherProxy {
    // Creates hashmap to store locations accessed
    private static final HashMap<String, ArrayList<Period>> cache = new HashMap<>();

    // Defines new getForecast method to get weather data using grid numbers
    public static ArrayList<Period> getForecast(String gridId, int gridX, int gridY) {
        String key = gridId + "-" + gridX + "-" + gridY;
        // If location was already accessed and is in cache then return it's forecast
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        // If location is new then get the forecast
        ArrayList<Period> forecast = WeatherAPI.getForecast(gridId, gridX, gridY);
        // Puts the forecast into the cache
        if (forecast != null) {
            cache.put(key, forecast);
        }
        return forecast;
    }
}