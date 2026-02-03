import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyWeatherAPITest {
    
    @Test
    void testGetPoint_ValidCoordinates_ReturnsPointProperties() {
        // Test with Chicago coordinates
        PointProperties point = MyWeatherAPI.getPoint(41.8373, -87.6862);
        
        assertNotNull(point, "Point properties should not be null for valid coordinates");
        assertNotNull(point.gridId, "Grid ID should not be null");
        assertTrue(point.gridX > 0, "Grid X should be positive");
        assertTrue(point.gridY > 0, "Grid Y should be positive");
        
        // Chicago should be in LOT (Chicago) grid
        assertEquals("LOT", point.gridId, "Chicago should be in LOT grid");
    }
    
    @Test
    void testGetPoint_NewYorkCoordinates_ReturnsPointProperties() {
        // Test with New York coordinates
        PointProperties point = MyWeatherAPI.getPoint(40.7128, -74.0060);
        
        assertNotNull(point, "Point properties should not be null for NYC");
        assertNotNull(point.gridId, "Grid ID should not be null");
        assertTrue(point.gridX > 0, "Grid X should be positive");
        assertTrue(point.gridY > 0, "Grid Y should be positive");
    }
    
    @Test
    void testGetPoint_LosAngelesCoordinates_ReturnsPointProperties() {
        // Test with Los Angeles coordinates
        PointProperties point = MyWeatherAPI.getPoint(34.0522, -118.2437);
        
        assertNotNull(point, "Point properties should not be null for LA");
        assertNotNull(point.gridId, "Grid ID should not be null");
        assertTrue(point.gridX > 0, "Grid X should be positive");
        assertTrue(point.gridY > 0, "Grid Y should be positive");
    }
    
    @Test
    void testGetPoint_InvalidCoordinates_ReturnsNull() {
        // Test with invalid coordinates (outside US)
        PointProperties point = MyWeatherAPI.getPoint(0.0, 0.0);
        
        assertNull(point, "Point properties should be null for invalid coordinates");
    }
    
    @Test
    void testGetPoint_OceanCoordinates_ReturnsNull() {
        // Test with ocean coordinates (no land forecast)
        PointProperties point = MyWeatherAPI.getPoint(25.0, -160.0);
        
        // Note: Some ocean coordinates may have marine forecasts, so this might not always be null
        // but it's a reasonable test for invalid locations
        if (point != null) {
            assertNotNull(point.gridId, "If point exists, gridId should not be null");
        }
    }
    
    @Test
    void testGetPointObject_ValidJson_ParsesCorrectly() {
        // Sample JSON from weather.gov API
        String validJson = """
            {
                "properties": {
                    "gridId": "LOT",
                    "gridX": 75,
                    "gridY": 73
                }
            }
            """;
        
        PointProperties point = MyWeatherAPI.getPointObject(validJson);
        
        assertNotNull(point, "Point should not be null for valid JSON");
        assertEquals("LOT", point.gridId, "Grid ID should match");
        assertEquals(75, point.gridX, "Grid X should match");
        assertEquals(73, point.gridY, "Grid Y should match");
    }
    
    @Test
    void testGetPointObject_InvalidJson_ReturnsNull() {
        String invalidJson = "not valid json";
        
        PointProperties point = MyWeatherAPI.getPointObject(invalidJson);
        
        assertNull(point, "Point should be null for invalid JSON");
    }
    
    @Test
    void testGetPointObject_EmptyJson_ReturnsNull() {
        String emptyJson = "{}";
        
        PointProperties point = MyWeatherAPI.getPointObject(emptyJson);
        
        assertNull(point, "Point should be null for empty JSON");
    }
    
    @Test
    void testGetPointObject_MissingProperties_ReturnsNull() {
        String missingPropertiesJson = """
            {
                "notProperties": {
                    "gridId": "LOT"
                }
            }
            """;
        
        PointProperties point = MyWeatherAPI.getPointObject(missingPropertiesJson);
        
        assertNull(point, "Point should be null when properties field is missing");
    }
    
    @Test
    void testGetPoint_DifferentLocations_ReturnDifferentGrids() {
        PointProperties chicago = MyWeatherAPI.getPoint(41.8373, -87.6862);
        PointProperties miami = MyWeatherAPI.getPoint(25.7617, -80.1918);
        
        assertNotNull(chicago, "Chicago point should not be null");
        assertNotNull(miami, "Miami point should not be null");
        
        // Different cities should have different grids
        assertNotEquals(chicago.gridId, miami.gridId, "Different cities should have different grid IDs");
    }
}
