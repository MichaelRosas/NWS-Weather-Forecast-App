import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import weather.Period;
import weather.WeatherAPI;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;

/**
 * Main JavaFX application class for Weather Forecast App.
 * Displays current weather and 3-day forecast for US cities.
 */
public class JavaFX extends Application {
	// UI Components
	ComboBox<String> cityBox;
	Image precipitationIcon, speedIcon, directionIcon;
	ImageView weatherIconView, precipitationIconView, speedIconView, directionIconView;
	Label temperature, weather, precipitationValue, speedValue, directionValue, precipitationText, speedText, directionText;
	VBox precipitationBox, speedBox, directionBox, root, threeDayForecast;
	HBox infoBox, dayBox1, dayBox2, dayBox3;
	Button change, back;

	// Maps city names to [latitude, longitude] coordinates
	public HashMap<String, double[]> cityCoordinates;

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Constructor - loads city data from CSV file
	 */
	public JavaFX() {
		cityCoordinates = new HashMap<>();
		try (InputStream is = getClass().getResourceAsStream(AppConstants.CITY_DATA_FILE);
		     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			br.readLine(); // Skip header
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				cityCoordinates.put((values[0]+", "+values[1]), new double[]{Double.parseDouble(values[2]), Double.parseDouble(values[3])});
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to load city data", e);
		} catch (NullPointerException e) {
			throw new RuntimeException("City data file not found", e);
		}
	}

	/**
	 * Initializes and displays the main application window
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Weather");
		
		// Load initial forecast for default city
		PointProperties location = MyWeatherAPI.getPoint(AppConstants.DEFAULT_LAT, AppConstants.DEFAULT_LNG);
		if (location == null){
			throw new RuntimeException("Location did not load");
		}
		ArrayList<Period> forecast = WeatherProxy.getForecast(location.gridId,location.gridX,location.gridY);
		if (forecast == null){
			throw new RuntimeException("Forecast did not load");
		}

		// Setup city selector dropdown
		ArrayList<String> sortedCities = new ArrayList<>(cityCoordinates.keySet());
		Collections.sort(sortedCities);
		cityBox = new ComboBox<>();
		cityBox.getItems().addAll(sortedCities);
		cityBox.setPromptText(AppConstants.DEFAULT_CITY);
		Scene mainScene;
		cityBox.setStyle("-fx-background-color: white; -fx-font-family: 'Roboto Thin';  -fx-font-size: 16px; -fx-border-radius: 10px; -fx-padding: 5px;");

		// Initialize main weather icon
		weatherIconView = new ImageView();
		weatherIconView.setFitWidth(AppConstants.WEATHER_ICON_SIZE);
		weatherIconView.setFitHeight(AppConstants.WEATHER_ICON_SIZE);

		setWeatherIcon(forecast.get(0).shortForecast, forecast.get(0).isDaytime, weatherIconView);

		// Main temperature display
		temperature = new Label(forecast.get(0).temperature + "°");
		temperature.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 80px; -fx-font-weight: 300; -fx-text-fill: white;");
		temperature.setAlignment(Pos.CENTER);
		temperature.setWrapText(true);
		
		// Weather description
		weather = new Label(forecast.get(0).shortForecast);
		weather.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-font-weight: 200; -fx-text-fill: white;");
		weather.setAlignment(Pos.CENTER);
		weather.setWrapText(true);
		weather.setMaxWidth(480);

		// Initialize info icons (precipitation, wind speed, wind direction)
		precipitationIcon = new Image("icons/precipitation.png");
		precipitationIconView = new ImageView(precipitationIcon);
		precipitationIconView.setFitWidth(AppConstants.INFO_ICON_SIZE);
		precipitationIconView.setFitHeight(AppConstants.INFO_ICON_SIZE);
		speedIcon = new Image("icons/speed.png");
		speedIconView = new ImageView(speedIcon);
		speedIconView.setFitWidth(AppConstants.INFO_ICON_SIZE);
		speedIconView.setFitHeight(AppConstants.INFO_ICON_SIZE);
		directionIcon = new Image("icons/direction.png");
		directionIconView = new ImageView(directionIcon);
		directionIconView.setFitWidth(AppConstants.INFO_ICON_SIZE);
		directionIconView.setFitHeight(AppConstants.INFO_ICON_SIZE);

		// Info box labels
		precipitationText = new Label("Precipitation");
		precipitationText.setAlignment(Pos.CENTER);
		precipitationText.setWrapText(true);
		precipitationText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100; -fx-font-size: 12px;");
		speedText = new Label("Wind Speed");
		speedText.setAlignment(Pos.CENTER);
		speedText.setWrapText(true);
		speedText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100; -fx-font-size: 12px;");
		directionText = new Label("Wind Direction");
		directionText.setAlignment(Pos.CENTER);
		directionText.setWrapText(true);
		directionText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100; -fx-font-size: 12px;");
		precipitationValue = new Label(forecast.get(0).probabilityOfPrecipitation.value + "%");
		precipitationValue.setAlignment(Pos.CENTER);
		precipitationValue.setWrapText(true);
		precipitationValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");
		speedValue = new Label(forecast.get(0).windSpeed);
		speedValue.setAlignment(Pos.CENTER);
		speedValue.setWrapText(true);
		speedValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");
		if(Objects.equals(forecast.get(0).windSpeed, "0 mph")) {
			directionValue = new Label("N/A");
		} else {
			directionValue = new Label(forecast.get(0).windDirection);
		}
		directionValue.setAlignment(Pos.CENTER);
		directionValue.setWrapText(true);
		directionValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");

		// Organize info sections into boxes
		precipitationBox = new VBox(10, precipitationIconView, precipitationValue, precipitationText);
		precipitationBox.setAlignment(Pos.CENTER);
		precipitationBox.setMaxWidth(115);
		speedBox = new VBox(10, speedIconView, speedValue, speedText);
		speedBox.setAlignment(Pos.CENTER);
		speedBox.setMaxWidth(115);
		directionBox = new VBox(10, directionIconView, directionValue, directionText);
		directionBox.setAlignment(Pos.CENTER);
		directionBox.setMaxWidth(115);

		// Combine info boxes into single container
		infoBox = new HBox(20, precipitationBox, speedBox, directionBox);
		infoBox.setStyle("-fx-background-color: rgba(186,186,186,0.3); -fx-background-radius: 20px; -fx-padding: 10px 20px;");
		infoBox.setAlignment(Pos.CENTER);
		infoBox.setMaxWidth(480);
		infoBox.setSpacing(20);

		// Button to switch to 3-day forecast view
		change = new Button("See 3 Day Forecast");
		change.setStyle("-fx-background-color: orange; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		change.setPrefWidth(210);

		// Main layout container
		root = new VBox(10, cityBox, weatherIconView, temperature, weather, infoBox, change);
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);

		// Create 3-day forecast boxes (skip current day)
		if (forecast.get(0).isDaytime) {
			dayBox1 = createWeatherBox(forecast, 2, 3);
			dayBox2 = createWeatherBox(forecast, 4, 5);
			dayBox3 = createWeatherBox(forecast, 6, 7);
		} else {
			dayBox1 = createWeatherBox(forecast, 1, 2);
			dayBox2 = createWeatherBox(forecast, 3, 4);
			dayBox3 = createWeatherBox(forecast, 5, 6);
		}

		// Back button for returning from forecast view
		back = new Button("Go Back");
		back.setStyle("-fx-background-color: orange; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		back.setPrefWidth(180);
		threeDayForecast = new VBox(15, dayBox1, dayBox2, dayBox3, back);

		// Apply day/night theme
		String rootStyle;
		if (forecast.get(0).isDaytime) {
			rootStyle = "-fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); -fx-padding: 20px;";
			root.setStyle("-fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); -fx-padding: 20px;");
			threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); ");
		} else {
			rootStyle = "-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;";
			root.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;");
			threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); ");
		}

		// Create scenes
		mainScene = new Scene(root, AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
		
		// City selection event handler
		cityBox.setOnAction(e -> {
			String selectedCity = cityBox.getValue();
			if (selectedCity != null) {
				updateWeather(selectedCity, primaryStage, mainScene, rootStyle);
			}
		});
		Scene forecastScene = new Scene(threeDayForecast, AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);

		// Button handlers for switching between views
		change.setOnAction(e -> primaryStage.setScene(forecastScene));
		back.setOnAction(e -> primaryStage.setScene(mainScene));

		// Load initial city and display
		updateWeather(AppConstants.DEFAULT_CITY, primaryStage, mainScene, rootStyle);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	/**
	 * Updates weather display for selected city with loading screen
	 * @param city City name to load weather for
	 * @param primaryStage Main application stage
	 * @param mainScene Scene to return to after loading
	 * @param rootStyle CSS style for background theme
	 */
	public void updateWeather(String city, Stage primaryStage, Scene mainScene, String rootStyle) {
		// Display loading screen
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setStyle("-fx-progress-color: orange;");
		Label loadingLabel = new Label("Loading...");
		loadingLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-font-weight: 500; -fx-text-fill: white;");
		VBox loadingScreen = new VBox(20, progressIndicator, loadingLabel);
		loadingScreen.setAlignment(Pos.CENTER);
		loadingScreen.setStyle(rootStyle);
		primaryStage.setScene(new Scene(loadingScreen, AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT));

		PauseTransition pause = new PauseTransition(Duration.millis(AppConstants.LOADING_DELAY_MS));
		pause.setOnFinished(event -> {
			Platform.runLater(() -> {
				try {
					// Fetch weather data for selected city
					double[] coordinates = cityCoordinates.get(city);
					if (coordinates == null) {
						showError(primaryStage, mainScene, "City not found: " + city);
						return;
					}
					
					PointProperties location = MyWeatherAPI.getPoint(coordinates[0], coordinates[1]);
					if (location == null) {
						showError(primaryStage, mainScene, "Failed to get location data. Please try again.");
						return;
					}
					
					ArrayList<Period> forecast = WeatherAPI.getForecast(location.gridId, location.gridX, location.gridY);
					if (forecast == null) {
						showError(primaryStage, mainScene, "Failed to get weather forecast. Please try again.");
						return;
					}

					// Update main screen with new weather data
					temperature.setText(forecast.get(0).temperature + "°");
					weather.setText(forecast.get(0).shortForecast);
					precipitationValue.setText(forecast.get(0).probabilityOfPrecipitation.value + "%");
					speedValue.setText(forecast.get(0).windSpeed);
					if (Objects.equals(forecast.get(0).windSpeed, "0 mph")) {
						directionValue.setText("N/A");
					} else {
						directionValue.setText(forecast.get(0).windDirection);
					}

					setWeatherIcon(forecast.get(0).shortForecast, forecast.get(0).isDaytime, weatherIconView);

					// Update 3-day forecast boxes
					dayBox1.getChildren().clear();
					dayBox2.getChildren().clear();
					dayBox3.getChildren().clear();

					if (forecast.get(0).isDaytime) {
						dayBox1 = createWeatherBox(forecast, 2, 3);
						dayBox2 = createWeatherBox(forecast, 4, 5);
						dayBox3 = createWeatherBox(forecast, 6, 7);
					} else {
						dayBox1 = createWeatherBox(forecast, 1, 2);
						dayBox2 = createWeatherBox(forecast, 3, 4);
						dayBox3 = createWeatherBox(forecast, 5, 6);
					}

					threeDayForecast.getChildren().setAll(dayBox1, dayBox2, dayBox3, back);

					// Update theme based on time of day
					if (forecast.get(0).isDaytime) {
						root.setStyle("-fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); -fx-padding: 20px;");
						threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); ");
					} else {
						root.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;");
						threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); ");
					}

					primaryStage.setScene(mainScene);
				} catch (Exception e) {
					e.printStackTrace();
					showError(primaryStage, mainScene, "An error occurred: " + e.getMessage());
				}
			});
		});

		pause.play();
	}

	/**
	 * Displays error dialog to user
	 * @param stage Application stage
	 * @param returnScene Scene to return to when dismissed
	 * @param message Error message to display
	 */
	private void showError(Stage stage, Scene returnScene, String message) {
		Label errorLabel = new Label("Error");
		errorLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white;");
		
		Label errorMessage = new Label(message);
		errorMessage.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 18px; -fx-text-fill: white;");
		errorMessage.setWrapText(true);
		errorMessage.setMaxWidth(400);
		errorMessage.setAlignment(Pos.CENTER);
		
		Button okButton = new Button("OK");
		okButton.setStyle("-fx-background-color: orange; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		okButton.setOnAction(e -> stage.setScene(returnScene));
		
		VBox errorBox = new VBox(20, errorLabel, errorMessage, okButton);
		errorBox.setAlignment(Pos.CENTER);
		errorBox.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 40px;");
		
		Scene errorScene = new Scene(errorBox, AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
		stage.setScene(errorScene);
	}

	/**
	 * Sets weather icon based on forecast description
	 * @param shortForecast Weather description text
	 * @param isDaytime Whether it's currently daytime
	 * @param imageView ImageView to update with icon
	 */
	private void setWeatherIcon(String shortForecast, boolean isDaytime, ImageView imageView) {
		// Calls factory
		ImageView createdIcon = WeatherIconFactory.createWeatherIcon(shortForecast, isDaytime);
		imageView.setImage(createdIcon.getImage());
	}

	/**
	 * Creates a weather forecast box for a single day
	 * @param forecast Complete forecast data
	 * @param dayIndex Index for daytime forecast
	 * @param nightIndex Index for nighttime forecast
	 * @return HBox containing the day's weather information
	 */
	private HBox createWeatherBox(ArrayList<Period> forecast, int dayIndex, int nightIndex) {
		ImageView weatherIcon = new ImageView();
		weatherIcon.setFitWidth(AppConstants.WEATHER_ICON_SMALL);
		weatherIcon.setFitHeight(AppConstants.WEATHER_ICON_SMALL);
		setWeatherIcon(forecast.get(dayIndex).shortForecast, forecast.get(dayIndex).isDaytime, weatherIcon);

		Label precipitationLabel = new Label(forecast.get(dayIndex).probabilityOfPrecipitation.value + "%");
		precipitationLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: 300;");
		VBox weatherIconBox = new VBox(15, weatherIcon, precipitationLabel);
		weatherIconBox.setPrefWidth(120);
		weatherIconBox.setAlignment(Pos.CENTER);

		Label dayLabel = new Label(forecast.get(dayIndex).name);
		dayLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");
		dayLabel.setWrapText(true);
		Label forecastLabel = new Label(forecast.get(dayIndex).shortForecast);
		forecastLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 16px; -fx-text-fill: white;");
		forecastLabel.setWrapText(true);
		forecastLabel.setMaxWidth(200);
		VBox forecastBox = new VBox(10, dayLabel, forecastLabel);
		forecastBox.setMaxWidth(200);

		Label dayTempLabel = new Label(forecast.get(dayIndex).temperature + "°");
		dayTempLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 50px; -fx-text-fill: white; -fx-font-weight: 500;");
		Label nightTempLabel = new Label("N: " + forecast.get(nightIndex).temperature + "°");
		nightTempLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: 500;");
		VBox tempBox = new VBox(15, dayTempLabel, nightTempLabel);
		tempBox.setPrefWidth(160);
		tempBox.setAlignment(Pos.CENTER);

		HBox weatherBox = new HBox(15, weatherIconBox, forecastBox, tempBox);
		weatherBox.setStyle(
				"-fx-padding: 15px; -fx-spacing: 10px; " +
						"-fx-background-color: rgba(186,186,186,0.3);" +
						"-fx-border-radius: 10px; -fx-background-radius: 10px; " +
						"-fx-alignment: center;"
		);
		weatherBox.setPrefWidth(500);
		weatherBox.setMaxWidth(500);

		return weatherBox;
	}
}