import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppConstantsTest {
    
    @Test
    void testCacheExpirationMs() {
        assertEquals(1_800_000, AppConstants.CACHE_EXPIRATION_MS, 
                     "Cache expiration should be 30 minutes (1,800,000 ms)");
        
        // Verify it's 30 minutes
        long thirtyMinutesInMs = 30 * 60 * 1000;
        assertEquals(thirtyMinutesInMs, AppConstants.CACHE_EXPIRATION_MS, 
                     "Cache expiration should equal 30 minutes");
    }
    
    @Test
    void testWeatherApiBaseUrl() {
        assertEquals("https://api.weather.gov", AppConstants.WEATHER_API_BASE_URL,
                     "Weather API base URL should be correct");
        assertTrue(AppConstants.WEATHER_API_BASE_URL.startsWith("https://"),
                   "API URL should use HTTPS");
    }
    
    @Test
    void testWindowDimensions() {
        assertEquals(500, AppConstants.WINDOW_WIDTH, "Window width should be 500");
        assertEquals(700, AppConstants.WINDOW_HEIGHT, "Window height should be 700");
        assertTrue(AppConstants.WINDOW_WIDTH > 0, "Window width should be positive");
        assertTrue(AppConstants.WINDOW_HEIGHT > 0, "Window height should be positive");
    }
    
    @Test
    void testWeatherIconSizes() {
        assertEquals(150, AppConstants.WEATHER_ICON_SIZE, "Weather icon size should be 150");
        assertEquals(70, AppConstants.WEATHER_ICON_SMALL, "Small weather icon size should be 70");
        assertEquals(50, AppConstants.INFO_ICON_SIZE, "Info icon size should be 50");
        
        assertTrue(AppConstants.WEATHER_ICON_SIZE > AppConstants.WEATHER_ICON_SMALL,
                   "Regular icon should be larger than small icon");
        assertTrue(AppConstants.WEATHER_ICON_SMALL > AppConstants.INFO_ICON_SIZE,
                   "Small weather icon should be larger than info icon");
    }
    
    @Test
    void testCityDataFile() {
        assertEquals("/uscities-500.csv", AppConstants.CITY_DATA_FILE,
                     "City data file path should be correct");
        assertTrue(AppConstants.CITY_DATA_FILE.startsWith("/"),
                   "File path should start with / for resource loading");
        assertTrue(AppConstants.CITY_DATA_FILE.endsWith(".csv"),
                   "File should be a CSV file");
    }
    
    @Test
    void testDefaultCity() {
        assertEquals("Chicago, IL", AppConstants.DEFAULT_CITY,
                     "Default city should be Chicago, IL");
        assertNotNull(AppConstants.DEFAULT_CITY, "Default city should not be null");
        assertFalse(AppConstants.DEFAULT_CITY.isEmpty(), "Default city should not be empty");
    }
    
    @Test
    void testDefaultCoordinates() {
        assertEquals(41.8373, AppConstants.DEFAULT_LAT, 0.0001,
                     "Default latitude should be Chicago's latitude");
        assertEquals(-87.6862, AppConstants.DEFAULT_LNG, 0.0001,
                     "Default longitude should be Chicago's longitude");
        
        // Verify coordinates are valid (latitude: -90 to 90, longitude: -180 to 180)
        assertTrue(AppConstants.DEFAULT_LAT >= -90 && AppConstants.DEFAULT_LAT <= 90,
                   "Latitude should be in valid range");
        assertTrue(AppConstants.DEFAULT_LNG >= -180 && AppConstants.DEFAULT_LNG <= 180,
                   "Longitude should be in valid range");
    }
    
    @Test
    void testLoadingDelay() {
        assertEquals(1000, AppConstants.LOADING_DELAY_MS,
                     "Loading delay should be 1000ms (1 second)");
        assertTrue(AppConstants.LOADING_DELAY_MS > 0,
                   "Loading delay should be positive");
    }
    
    @Test
    void testConstantsAreConsistent() {
        // Verify that icon sizes make sense relative to window size
        assertTrue(AppConstants.WEATHER_ICON_SIZE < AppConstants.WINDOW_WIDTH,
                   "Weather icon should fit within window width");
        assertTrue(AppConstants.WEATHER_ICON_SIZE < AppConstants.WINDOW_HEIGHT,
                   "Weather icon should fit within window height");
    }
}
