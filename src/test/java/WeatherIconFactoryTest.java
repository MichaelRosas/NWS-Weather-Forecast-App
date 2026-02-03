import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.image.ImageView;

class WeatherIconFactoryTest {
    
    @Test
    void testCreateWeatherIcon_Hurricane() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("hurricane warning", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
        assertEquals(150, icon.getFitWidth(), "Icon width should be 150");
        assertEquals(150, icon.getFitHeight(), "Icon height should be 150");
    }
    
    @Test
    void testCreateWeatherIcon_Tornado() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("tornado watch", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_ThunderstormWithSnow() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("thunder and snow", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Thunderstorm() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("thunderstorm likely", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Thunder() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("thunder", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Hail() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("hail possible", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Sleet() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("sleet", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_RainAndSnow() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("rain and snow", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Snow() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("snow showers", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_HeavyRain() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("heavy rain", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Showers() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("showers", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Rain() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("rain", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Drizzle() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("drizzle", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Dust() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("dust storm", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Fog() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("fog", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Haze() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("haze", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Mist() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("mist", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Windy() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("wind", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Cloudy() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("cloud", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_Overcast() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("overcast", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_ClearDay() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("sunny", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_ClearNight() {
        ImageView icon = WeatherIconFactory.createWeatherIcon("clear", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_DefaultToDayIcon() {
        // Unknown weather condition should default to clear day icon
        ImageView icon = WeatherIconFactory.createWeatherIcon("unknown condition", true);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_DefaultToNightIcon() {
        // Unknown weather condition should default to clear night icon
        ImageView icon = WeatherIconFactory.createWeatherIcon("unknown condition", false);
        
        assertNotNull(icon, "Icon should not be null");
        assertNotNull(icon.getImage(), "Image should not be null");
    }
    
    @Test
    void testCreateWeatherIcon_CaseInsensitive() {
        // Test case insensitivity
        ImageView icon1 = WeatherIconFactory.createWeatherIcon("RAIN", true);
        ImageView icon2 = WeatherIconFactory.createWeatherIcon("rain", true);
        ImageView icon3 = WeatherIconFactory.createWeatherIcon("RaIn", true);
        
        assertNotNull(icon1, "Icon should not be null");
        assertNotNull(icon2, "Icon should not be null");
        assertNotNull(icon3, "Icon should not be null");
        
        // All should use the same icon (though different ImageView instances)
        assertEquals(icon1.getImage(), icon2.getImage(), "Same weather should use same icon");
        assertEquals(icon2.getImage(), icon3.getImage(), "Case should not matter");
    }
}
