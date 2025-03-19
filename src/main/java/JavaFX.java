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
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JavaFX extends Application {
	ComboBox<String> cityBox;
	Image precipitationIcon, speedIcon, directionIcon, clearNightIcon, clearDayIcon, cloudyIcon, rainIcon, fogIcon, hailIcon, hazeIcon, dustIcon, showersIcon, sleetIcon, snowIcon, thunderIcon, thunderRainIcon, thunderSnowIcon, hurricaneIcon, tornadoIcon, mistIcon, windyIcon;
	ImageView weatherIconView, precipitationIconView, speedIconView, directionIconView;
	Label temperature, weather, precipitationValue, speedValue, directionValue, precipitationText, speedText, directionText;
	VBox precipitationBox, speedBox, directionBox, root, threeDayForecast;
	HBox infoBox, dayBox1, dayBox2, dayBox3;
	Button change, back;

	public HashMap<String, double[]> cityCoordinates;

	public static void main(String[] args) {
		launch(args);
	}

	public JavaFX() {
		cityCoordinates = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/uscities-500.csv"))) {
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				cityCoordinates.put((values[0]+", "+values[1]), new double[]{Double.parseDouble(values[2]), Double.parseDouble(values[3])});
			}
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Weather");
		PointProperties location = MyWeatherAPI.getPoint(41.8373,-87.6862);
		if (location == null){
			throw new RuntimeException("Location did not load");
		}
		ArrayList<Period> forecast = WeatherAPI.getForecast(location.gridId,location.gridX,location.gridY);
		if (forecast == null){
			throw new RuntimeException("Forecast did not load");
		}

		ArrayList<String> sortedCities = new ArrayList<>(cityCoordinates.keySet());
		Collections.sort(sortedCities);
		cityBox = new ComboBox<>();
		cityBox.getItems().addAll(sortedCities);
		cityBox.setPromptText("Chicago, IL");
		Scene mainScene;
		cityBox.setStyle("-fx-background-color: white; -fx-font-family: 'Roboto Thin';  -fx-font-size: 16px; -fx-border-radius: 10px; -fx-padding: 5px;");

		clearNightIcon = new Image("icons/clear-night.png");
		clearDayIcon = new Image("icons/clear-day.png");
		cloudyIcon = new Image("icons/cloudy.png");
		rainIcon = new Image("icons/drizzle.png");
		fogIcon = new Image("icons/fog.png");
		hailIcon = new Image("icons/hail.png");
		hazeIcon = new Image("icons/haze.png");
		dustIcon = new Image("icons/dust.png");
		showersIcon = new Image("icons/rain.png");
		sleetIcon = new Image("icons/sleet.png");
		snowIcon = new Image("icons/snow.png");
		thunderIcon =  new Image("icons/thunderstorms.png");
		thunderRainIcon = new Image("icons/thunderstorms-rain.png");
		thunderSnowIcon = new Image("icons/thunderstorms-snow.png");
		hurricaneIcon = new Image("icons/hurricane.png");
		tornadoIcon = new Image("icons/tornado.png");
		mistIcon = new Image("icons/mist.png");
		windyIcon = new Image("icons/wind.png");
		weatherIconView = new ImageView();
		weatherIconView.setFitWidth(120);
		weatherIconView.setFitHeight(120);

		setWeatherIcon(forecast.get(0).shortForecast, forecast.get(0).isDaytime, weatherIconView);
		weatherIconView.setFitWidth(150);
		weatherIconView.setFitHeight(150);

		temperature = new Label(forecast.get(0).temperature + "°");
		temperature.setMaxWidth(150);
		temperature.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 80px; -fx-font-weight: 300; -fx-text-fill: white;");
		temperature.setAlignment(Pos.CENTER);
		weather = new Label(forecast.get(0).shortForecast);
		weather.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-font-weight: 200; -fx-text-fill: white;");
		weather.setAlignment(Pos.CENTER);
		weather.setWrapText(true);
		weather.setMaxWidth(400);

		precipitationIcon = new Image("icons/precipitation.png");
		precipitationIconView = new ImageView(precipitationIcon);
		precipitationIconView.setFitWidth(50);
		precipitationIconView.setFitHeight(50);
		speedIcon = new Image("icons/speed.png");
		speedIconView = new ImageView(speedIcon);
		speedIconView.setFitWidth(50);
		speedIconView.setFitHeight(50);
		directionIcon = new Image("icons/direction.png");
		directionIconView = new ImageView(directionIcon);
		directionIconView.setFitWidth(50);
		directionIconView.setFitHeight(50);

		precipitationText = new Label("Precipitation");
		precipitationText.setMaxWidth(100);
		precipitationText.setAlignment(Pos.CENTER);
		precipitationText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100;");
		speedText = new Label("Wind Speed");
		speedText.setMaxWidth(100);
		speedText.setAlignment(Pos.CENTER);
		speedText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100;");
		directionText = new Label("Wind Direction");
		directionText.setMaxWidth(100);
		directionText.setAlignment(Pos.CENTER);
		directionText.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 100;");

		precipitationValue = new Label(forecast.get(0).probabilityOfPrecipitation.value + "%");
		precipitationValue.setMaxWidth(80);
		precipitationValue.setAlignment(Pos.CENTER);
		precipitationValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");
		speedValue = new Label(forecast.get(0).windSpeed);
		speedValue.setMaxWidth(95);
		speedValue.setPrefWidth(95);
		speedValue.setAlignment(Pos.CENTER);
		speedValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");
		if(Objects.equals(forecast.get(0).windSpeed, "0 mph")) {
			directionValue = new Label("N/A");
		} else {
			directionValue = new Label(forecast.get(0).windDirection);
		}
		directionValue.setMaxWidth(80);
		directionValue.setAlignment(Pos.CENTER);
		directionValue.setStyle("-fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-weight: 200; -fx-font-size: 16;");

		precipitationBox = new VBox(10, precipitationIconView, precipitationValue, precipitationText);
		precipitationBox.setAlignment(Pos.CENTER);
		precipitationBox.setMaxWidth(115);
		speedBox = new VBox(10, speedIconView, speedValue, speedText);
		speedBox.setAlignment(Pos.CENTER);
		speedBox.setMaxWidth(115);
		directionBox = new VBox(10, directionIconView, directionValue, directionText);
		directionBox.setAlignment(Pos.CENTER);
		directionBox.setMaxWidth(115);

		infoBox = new HBox(20, precipitationBox, speedBox, directionBox);
		infoBox.setStyle("-fx-background-color: rgba(186,186,186,0.3); -fx-background-radius: 20px; -fx-padding: 10px 20px;");
		infoBox.setAlignment(Pos.CENTER);
		infoBox.setMaxWidth(345);
		infoBox.setSpacing(30);

		change = new Button("See 3 Day Forecast");
		change.setStyle("-fx-background-color: orange; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		change.setPrefWidth(180);

		root = new VBox(10, cityBox, weatherIconView, temperature, weather, infoBox, change);
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);

		if (forecast.get(0).isDaytime) {
			dayBox1 = createWeatherBox(forecast, 2, 3);
			dayBox2 = createWeatherBox(forecast, 4, 5);
			dayBox3 = createWeatherBox(forecast, 6, 7);
		} else {
			dayBox1 = createWeatherBox(forecast, 1, 2);
			dayBox2 = createWeatherBox(forecast, 3, 4);
			dayBox3 = createWeatherBox(forecast, 5, 6);
		}

		back = new Button("Go Back");
		back.setStyle("-fx-background-color: orange; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		back.setPrefWidth(180);
		threeDayForecast = new VBox(15, dayBox1, dayBox2, dayBox3, back);

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

		mainScene = new Scene(root, 500, 700);
		cityBox.setOnAction(e -> {
			String selectedCity = cityBox.getValue();
			if (selectedCity != null) {
				updateWeather(selectedCity, primaryStage, mainScene, rootStyle);
			}
		});
		Scene forecastScene = new Scene(threeDayForecast, 500, 700);

		change.setOnAction(e -> primaryStage.setScene(forecastScene));
		back.setOnAction(e -> primaryStage.setScene(mainScene));

		updateWeather("Chicago, IL", primaryStage, mainScene, rootStyle);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	public void updateWeather(String city, Stage primaryStage, Scene mainScene, String rootStyle) {
		ProgressIndicator progressIndicator = new ProgressIndicator();
		progressIndicator.setStyle("-fx-progress-color: orange;");
		Label loadingLabel = new Label("Loading...");
		loadingLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-font-weight: 500; -fx-text-fill: white;");
		VBox loadingScreen = new VBox(20, progressIndicator, loadingLabel);
		loadingScreen.setAlignment(Pos.CENTER);
		loadingScreen.setStyle(rootStyle);
		primaryStage.setScene(new Scene(loadingScreen, 500, 700));

		PauseTransition pause = new PauseTransition(Duration.seconds(1));
		pause.setOnFinished(event -> {
			Platform.runLater(() -> {
				double[] coordinates = cityCoordinates.get(city);
				if (coordinates != null) {
					PointProperties location = MyWeatherAPI.getPoint(coordinates[0], coordinates[1]);
					if (location == null) {
						throw new RuntimeException("Location did not load");
					}
					ArrayList<Period> forecast = WeatherAPI.getForecast(location.gridId, location.gridX, location.gridY);
					if (forecast == null) {
						throw new RuntimeException("Forecast did not load");
					}

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

					if (forecast.get(0).isDaytime) {
						root.setStyle("-fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); -fx-padding: 20px;");
						threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #6dafd6, #4682B4); ");
					} else {
						root.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;");
						threeDayForecast.setStyle("-fx-alignment: center; -fx-spacing: 15px; -fx-padding: 20px; -fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); ");
					}

					primaryStage.setScene(mainScene);
				}
			});
		});

		pause.play();
	}

	private void setWeatherIcon(String shortForecast, boolean isDaytime, ImageView imageView) {
		shortForecast = shortForecast.toLowerCase();

		if (shortForecast.contains("hurricane")) {
			imageView.setImage(hurricaneIcon);
		} else if (shortForecast.contains("tornado")) {
			imageView.setImage(tornadoIcon);
		} else if (shortForecast.contains("thunder") && shortForecast.contains("snow")) {
			imageView.setImage(thunderSnowIcon);
		} else if (shortForecast.contains("thunderstorm")) {
			imageView.setImage(thunderRainIcon);
		} else if (shortForecast.contains("thunder")) {
			imageView.setImage(thunderIcon);
		} else if (shortForecast.contains("hail")) {
			imageView.setImage(hailIcon);
		} else if (shortForecast.contains("sleet") || shortForecast.contains("rain and snow")) {
			imageView.setImage(sleetIcon);
		} else if (shortForecast.contains("snow")) {
			imageView.setImage(snowIcon);
		} else if (shortForecast.contains("heavy rain") || shortForecast.contains("showers")) {
			imageView.setImage(showersIcon);
		} else if (shortForecast.contains("rain") || shortForecast.contains("drizzle")) {
			imageView.setImage(rainIcon);
		} else if (shortForecast.contains("dust")) {
			imageView.setImage(dustIcon);
		} else if (shortForecast.contains("fog")) {
			imageView.setImage(fogIcon);
		} else if (shortForecast.contains("haze")) {
			imageView.setImage(hazeIcon);
		} else if (shortForecast.contains("mist")) {
			imageView.setImage(mistIcon);
		} else if (shortForecast.contains("wind")) {
			imageView.setImage(windyIcon);
		} else if (shortForecast.contains("cloud") || shortForecast.contains("overcast")) {
			imageView.setImage(cloudyIcon);
		} else {
			if (isDaytime) {
				imageView.setImage(clearDayIcon);
			} else {
				imageView.setImage(clearNightIcon);
			}
		}
	}

	private HBox createWeatherBox(ArrayList<Period> forecast, int dayIndex, int nightIndex) {
		ImageView weatherIcon = new ImageView();
		weatherIcon.setFitWidth(70);
		weatherIcon.setFitHeight(70);
		setWeatherIcon(forecast.get(dayIndex).shortForecast, forecast.get(dayIndex).isDaytime, weatherIcon);

		Label precipitationLabel = new Label(forecast.get(dayIndex).probabilityOfPrecipitation.value + "%");
		precipitationLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: 300;");
		VBox weatherIconBox = new VBox(15, weatherIcon, precipitationLabel);
		weatherIconBox.setPrefWidth(120);
		weatherIconBox.setAlignment(Pos.CENTER);

		Label dayLabel = new Label(forecast.get(dayIndex).name);
		dayLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");
		Label forecastLabel = new Label(forecast.get(dayIndex).shortForecast);
		forecastLabel.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 18px; -fx-text-fill: white;");
		forecastLabel.setWrapText(true);
		VBox forecastBox = new VBox(15, dayLabel, forecastLabel);
		forecastBox.setPrefWidth(300);
		forecastBox.setAlignment(Pos.CENTER);

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
