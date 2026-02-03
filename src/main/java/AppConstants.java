/**
 * Application-wide constants for configuration and UI sizing.
 * Centralizes magic numbers for easier maintenance.
 */
public class AppConstants {
    // Cache settings
    public static final long CACHE_EXPIRATION_MS = 1_800_000; // 30 minutes in milliseconds
    
    // API endpoints
    public static final String WEATHER_API_BASE_URL = "https://api.weather.gov";
    
    // UI dimensions
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 700;
    public static final int WEATHER_ICON_SIZE = 150;
    public static final int WEATHER_ICON_SMALL = 70;
    public static final int INFO_ICON_SIZE = 50;
    
    // CSV file path
    public static final String CITY_DATA_FILE = "/uscities-500.csv";
    
    // Default city
    public static final String DEFAULT_CITY = "Chicago, IL";
    public static final double DEFAULT_LAT = 41.8373;
    public static final double DEFAULT_LNG = -87.6862;
    
    // Loading delay (milliseconds)
    public static final int LOADING_DELAY_MS = 1000;
    
    private AppConstants() {
        // Prevent instantiation
    }
}
