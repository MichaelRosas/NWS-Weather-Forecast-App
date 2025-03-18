import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JavaFX extends Application {
	ComboBox<String> cityBox;
	Image precipitationIcon, speedIcon, directionIcon, clearNightIcon, clearDayIcon, cloudyIcon, rainIcon, fogIcon, hailIcon, hazeIcon, showersIcon, sleetIcon, snowIcon, thunderIcon, thunderRainIcon, thunderSnowIcon, hurricaneIcon, tornadoIcon, mistIcon, windyIcon;
	ImageView weatherIconView, precipitationIconView, speedIconView, directionIconView;
	Label temperature, weather, precipitationValue, speedValue, directionValue, precipitationText, speedText, directionText;
	VBox precipitationBox, speedBox, directionBox, root;
	HBox infoBox;
	Button change;

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
		PointProperties location = MyWeatherAPI.getPoint(41.8781,-87.6298);
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
		updateWeather("Chicago");
		cityBox.setOnAction(e -> {
			String selectedCity = cityBox.getValue();
			if (selectedCity != null) {
				updateWeather(selectedCity);
			}
		});
		cityBox.setStyle("-fx-background-color: white; -fx-font-family: 'Roboto Thin';  -fx-font-size: 16px; -fx-border-radius: 10px; -fx-padding: 5px;");

		clearNightIcon = new Image("icons/clear-night.png");
		clearDayIcon = new Image("icons/clear-day.png");
		cloudyIcon = new Image("icons/cloudy.png");
		rainIcon = new Image("icons/drizzle.png");
		fogIcon = new Image("icons/fog.png");
		hailIcon = new Image("icons/hail.png");
		hazeIcon = new Image("icons/haze.png");
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
		
		String shortForecast = forecast.get(0).shortForecast;
		if (shortForecast.toLowerCase().contains("hurricane")) {
			weatherIconView.setImage(hurricaneIcon);
		} else if (shortForecast.toLowerCase().contains("tornado")) {
			weatherIconView.setImage(tornadoIcon);
		} else if (shortForecast.toLowerCase().contains("thunder") && shortForecast.toLowerCase().contains("snow")) {
			weatherIconView.setImage(thunderSnowIcon);
		} else if (shortForecast.toLowerCase().contains("thunderstorm")) {
			weatherIconView.setImage(thunderRainIcon);
		} else if (shortForecast.toLowerCase().contains("thunder")) {
			weatherIconView.setImage(thunderIcon);
		} else if (shortForecast.toLowerCase().contains("hail")) {
			weatherIconView.setImage(hailIcon);
		} else if (shortForecast.toLowerCase().contains("snow")) {
			weatherIconView.setImage(snowIcon);
		} else if (shortForecast.toLowerCase().contains("sleet")) {
			weatherIconView.setImage(sleetIcon);
		} else if (shortForecast.toLowerCase().contains("heavy rain") || shortForecast.toLowerCase().contains("showers")) {
			weatherIconView.setImage(showersIcon);
		} else if (shortForecast.toLowerCase().contains("rain") || shortForecast.toLowerCase().contains("drizzle")) {
			weatherIconView.setImage(rainIcon);
		} else if (shortForecast.toLowerCase().contains("fog")) {
			weatherIconView.setImage(fogIcon);
		} else if (shortForecast.toLowerCase().contains("haze")) {
			weatherIconView.setImage(hazeIcon);
		} else if (shortForecast.toLowerCase().contains("mist")) {
			weatherIconView.setImage(mistIcon);
		} else if (shortForecast.toLowerCase().contains("wind")) {
			weatherIconView.setImage(windyIcon);
		} else if (shortForecast.toLowerCase().contains("cloud") || shortForecast.toLowerCase().contains("overcast")) {
			weatherIconView.setImage(cloudyIcon);
		} else {
			if(forecast.get(0).isDaytime) {
				weatherIconView.setImage(clearDayIcon);
			} else {
				weatherIconView.setImage(clearNightIcon);
			}
		}
		weatherIconView.setFitWidth(150);
		weatherIconView.setFitHeight(150);

		temperature = new Label(String.valueOf(forecast.get(0).temperature) + "°");
		temperature.setMaxWidth(150);
		temperature.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 80px; -fx-font-weight: 300; -fx-text-fill: white;");
		temperature.setAlignment(Pos.CENTER);
		weather = new Label(forecast.get(0).shortForecast);
		weather.setMaxWidth(400);
		weather.setStyle("-fx-font-family: 'Roboto Thin'; -fx-font-size: 24px; -fx-font-weight: 200; -fx-text-fill: white;");
		weather.setAlignment(Pos.CENTER);

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

		precipitationValue = new Label(String.valueOf(forecast.get(0).probabilityOfPrecipitation.value) + "%");
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
		change.setStyle("-fx-background-color: #FFA500; -fx-font-family: 'Roboto Thin'; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-background-radius: 20px;");
		change.setPrefWidth(200);

		root = new VBox(10, cityBox, weatherIconView, temperature, weather, infoBox, change);
		if(forecast.get(0).isDaytime) {
			root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #4682B4); -fx-padding: 20px;");
		} else {
			root.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;");
		}
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);

		Scene scene = new Scene(root, 480, 680);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void updateWeather(String city) {
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
			if(Objects.equals(forecast.get(0).windSpeed, "0 mph")) {
				directionValue.setText("N/A");
			} else {
				directionValue.setText(forecast.get(0).windDirection);
			}
			if(forecast.get(0).isDaytime) {
				root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #4682B4); -fx-padding: 20px;");
			} else {
				root.setStyle("-fx-background-color: linear-gradient(to bottom, #5c5ec1, #3d3f9c); -fx-padding: 20px;");
			}

			String shortForecast = forecast.get(0).shortForecast;
			if (shortForecast.toLowerCase().contains("hurricane")) {
				weatherIconView.setImage(hurricaneIcon);
			} else if (shortForecast.toLowerCase().contains("tornado")) {
				weatherIconView.setImage(tornadoIcon);
			} else if (shortForecast.toLowerCase().contains("thunder") && shortForecast.toLowerCase().contains("snow")) {
				weatherIconView.setImage(thunderSnowIcon);
			} else if (shortForecast.toLowerCase().contains("thunderstorm")) {
				weatherIconView.setImage(thunderRainIcon);
			} else if (shortForecast.toLowerCase().contains("thunder")) {
				weatherIconView.setImage(thunderIcon);
			} else if (shortForecast.toLowerCase().contains("hail")) {
				weatherIconView.setImage(hailIcon);
			} else if (shortForecast.toLowerCase().contains("snow")) {
				weatherIconView.setImage(snowIcon);
			} else if (shortForecast.toLowerCase().contains("sleet")) {
				weatherIconView.setImage(sleetIcon);
			} else if (shortForecast.toLowerCase().contains("heavy rain") || shortForecast.toLowerCase().contains("showers")) {
				weatherIconView.setImage(showersIcon);
			} else if (shortForecast.toLowerCase().contains("rain") || shortForecast.toLowerCase().contains("drizzle")) {
				weatherIconView.setImage(rainIcon);
			} else if (shortForecast.toLowerCase().contains("fog")) {
				weatherIconView.setImage(fogIcon);
			} else if (shortForecast.toLowerCase().contains("haze")) {
				weatherIconView.setImage(hazeIcon);
			} else if (shortForecast.toLowerCase().contains("mist")) {
				weatherIconView.setImage(mistIcon);
			} else if (shortForecast.toLowerCase().contains("wind")) {
				weatherIconView.setImage(windyIcon);
			} else if (shortForecast.toLowerCase().contains("cloud") || shortForecast.toLowerCase().contains("overcast")) {
				weatherIconView.setImage(cloudyIcon);
			} else {
				if(forecast.get(0).isDaytime) {
					weatherIconView.setImage(clearDayIcon);
				} else {
					weatherIconView.setImage(clearNightIcon);
				}
			}
		}
	}
}