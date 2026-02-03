import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Factory class for creating weather icons based on forecast descriptions.
 * Uses singleton pattern for icon caching.
 */
public class WeatherIconFactory {

    // Preloaded weather icons (cached in memory)
    private static final Image hurricaneIcon = new Image("icons/hurricane.png");
    private static final Image tornadoIcon = new Image("icons/tornado.png");
    private static final Image thunderSnowIcon = new Image("icons/thunderstorms-snow.png");
    private static final Image thunderRainIcon = new Image("icons/thunderstorms-rain.png");
    private static final Image thunderIcon = new Image("icons/thunderstorms.png");
    private static final Image hailIcon = new Image("icons/hail.png");
    private static final Image sleetIcon = new Image("icons/sleet.png");
    private static final Image snowIcon = new Image("icons/snow.png");
    private static final Image showersIcon = new Image("icons/rain.png");
    private static final Image rainIcon = new Image("icons/drizzle.png");
    private static final Image dustIcon = new Image("icons/dust.png");
    private static final Image fogIcon = new Image("icons/fog.png");
    private static final Image hazeIcon = new Image("icons/haze.png");
    private static final Image mistIcon = new Image("icons/mist.png");
    private static final Image windyIcon = new Image("icons/wind.png");
    private static final Image cloudyIcon = new Image("icons/cloudy.png");
    private static final Image clearDayIcon = new Image("icons/clear-day.png");
    private static final Image clearNightIcon = new Image("icons/clear-night.png");

    /**
     * Creates weather icon ImageView based on forecast description
     * @param shortForecast Weather description from forecast
     * @param isDaytime True if daytime, false if nighttime
     * @return ImageView with appropriate weather icon
     */
    public static ImageView createWeatherIcon(String shortForecast, boolean isDaytime) {
        ImageView weatherIconView = new ImageView();
        weatherIconView.setFitWidth(AppConstants.WEATHER_ICON_SIZE);
        weatherIconView.setFitHeight(AppConstants.WEATHER_ICON_SIZE);

        // Set icon based on words in shortForecast
        if (shortForecast.contains("hurricane")) {
            weatherIconView.setImage(hurricaneIcon);
        } else if (shortForecast.contains("tornado")) {
            weatherIconView.setImage(tornadoIcon);
        } else if (shortForecast.contains("thunder") && shortForecast.contains("snow")) {
            weatherIconView.setImage(thunderSnowIcon);
        } else if (shortForecast.contains("thunderstorm")) {
            weatherIconView.setImage(thunderRainIcon);
        } else if (shortForecast.contains("thunder")) {
            weatherIconView.setImage(thunderIcon);
        } else if (shortForecast.contains("hail")) {
            weatherIconView.setImage(hailIcon);
        } else if (shortForecast.contains("sleet") || shortForecast.contains("rain and snow")) {
            weatherIconView.setImage(sleetIcon);
        } else if (shortForecast.contains("snow")) {
            weatherIconView.setImage(snowIcon);
        } else if (shortForecast.contains("heavy rain") || shortForecast.contains("showers")) {
            weatherIconView.setImage(showersIcon);
        } else if (shortForecast.contains("rain") || shortForecast.contains("drizzle")) {
            weatherIconView.setImage(rainIcon);
        } else if (shortForecast.contains("dust")) {
            weatherIconView.setImage(dustIcon);
        } else if (shortForecast.contains("fog")) {
            weatherIconView.setImage(fogIcon);
        } else if (shortForecast.contains("haze")) {
            weatherIconView.setImage(hazeIcon);
        } else if (shortForecast.contains("mist")) {
            weatherIconView.setImage(mistIcon);
        } else if (shortForecast.contains("wind")) {
            weatherIconView.setImage(windyIcon);
        } else if (shortForecast.contains("cloud") || shortForecast.contains("overcast")) {
            weatherIconView.setImage(cloudyIcon);
        } else {
            // If no specific icon, then default to icon showing day/night
            if (isDaytime) {
                weatherIconView.setImage(clearDayIcon);
            } else {
                weatherIconView.setImage(clearNightIcon);
            }
        }

        return weatherIconView;
    }
}
