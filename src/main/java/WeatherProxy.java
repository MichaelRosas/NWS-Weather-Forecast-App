import weather.Period;
import weather.WeatherAPI;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Proxy class for weather API that implements caching with expiration.
 * Caches forecast data for 30 minutes to reduce API calls.
 */
class WeatherProxy {
    /**
     * Cache entry containing forecast data and timestamp
     */
    private static class CacheEntry {
        ArrayList<Period> forecast;
        long timestamp;
        
        CacheEntry(ArrayList<Period> forecast) {
            this.forecast = forecast;
            this.timestamp = System.currentTimeMillis();
        }
        
        boolean isExpired() {
            // Cache expires after 30 minutes
            return System.currentTimeMillis() - timestamp > AppConstants.CACHE_EXPIRATION_MS;
        }
    }
    
    // Maps "gridId-gridX-gridY" to cached forecast data
    private static final HashMap<String, CacheEntry> cache = new HashMap<>();

    /**
     * Gets weather forecast with caching
     * @param gridId Weather grid identifier
     * @param gridX Grid X coordinate
     * @param gridY Grid Y coordinate
     * @return List of forecast periods, or null if unavailable
     */
    public static ArrayList<Period> getForecast(String gridId, int gridX, int gridY) {
        String key = gridId + "-" + gridX + "-" + gridY;
        
        // Check if location is in cache and not expired
        if (cache.containsKey(key)) {
            CacheEntry entry = cache.get(key);
            if (!entry.isExpired()) {
                return entry.forecast;
            } else {
                // Remove expired entry
                cache.remove(key);
            }
        }

        // If location is new or cache expired, fetch fresh forecast
        ArrayList<Period> forecast = WeatherAPI.getForecast(gridId, gridX, gridY);
        // Puts the forecast into the cache
        if (forecast != null) {
            cache.put(key, new CacheEntry(forecast));
        }
        return forecast;
    }
}