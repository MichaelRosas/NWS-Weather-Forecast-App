import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import weather.Period;
import java.util.ArrayList;

class WeatherProxyTest {
    
    @BeforeEach
    void setUp() {
        // Note: Cache is static, so it persists between tests
        // In a real scenario, we'd want a way to clear it or use dependency injection
    }
    
    @Test
    void testGetForecast_ValidLocation_ReturnsForecast() {
        // Test with a known valid location (Chicago area)
        ArrayList<Period> forecast = WeatherProxy.getForecast("LOT", 75, 73);
        
        assertNotNull(forecast, "Forecast should not be null for valid location");
        assertFalse(forecast.isEmpty(), "Forecast should contain periods");
    }
    
    @Test
    void testGetForecast_InvalidLocation_ReturnsNull() {
        // Test with an invalid location
        ArrayList<Period> forecast = WeatherProxy.getForecast("XXX", 0, 0);
        
        assertNull(forecast, "Forecast should be null for invalid location");
    }
    
    @Test
    void testCache_SameLocationTwice_UsesCache() {
        // First call - should fetch from API
        ArrayList<Period> forecast1 = WeatherProxy.getForecast("LOT", 75, 73);
        
        // Second call - should use cache (faster)
        long startTime = System.currentTimeMillis();
        ArrayList<Period> forecast2 = WeatherProxy.getForecast("LOT", 75, 73);
        long endTime = System.currentTimeMillis();
        
        assertNotNull(forecast2, "Cached forecast should not be null");
        assertTrue(endTime - startTime < 100, "Cache retrieval should be very fast (< 100ms)");
        
        // Both forecasts should be the same object from cache
        assertSame(forecast1, forecast2, "Should return same cached forecast object");
    }
    
    @Test
    void testGetForecast_DifferentLocations_ReturnsDifferentForecasts() {
        // Get forecast for two different locations
        ArrayList<Period> forecast1 = WeatherProxy.getForecast("LOT", 75, 73);
        ArrayList<Period> forecast2 = WeatherProxy.getForecast("LOT", 76, 74);
        
        assertNotNull(forecast1, "First forecast should not be null");
        assertNotNull(forecast2, "Second forecast should not be null");
        
        // Forecasts should be different objects (different cache entries)
        assertNotSame(forecast1, forecast2, "Different locations should have different forecasts");
    }
    
    @Test
    void testGetForecast_ForecastHasPeriods() {
        ArrayList<Period> forecast = WeatherProxy.getForecast("LOT", 75, 73);
        
        assertNotNull(forecast, "Forecast should not be null");
        assertTrue(forecast.size() > 0, "Forecast should have at least one period");
        
        // Verify first period has expected data
        Period firstPeriod = forecast.get(0);
        assertNotNull(firstPeriod.name, "Period name should not be null");
        assertNotNull(firstPeriod.shortForecast, "Short forecast should not be null");
        assertTrue(firstPeriod.temperature > -100 && firstPeriod.temperature < 150, 
                   "Temperature should be in reasonable range");
    }
}
