# ☀️ NWS Weather Forecast App

A modern JavaFX desktop application that delivers real-time weather forecasts for 500+ US cities using the National Weather Service API.

## 📋 Overview

NWS Weather Forecast App provides accurate, up-to-date weather information through an intuitive desktop interface. The application leverages the official National Weather Service API to display current conditions and 3-day forecasts with detailed meteorological data and dynamic weather icons.

## ✨ Key Features

### Weather Information

- **Current Weather Display**: Real-time temperature, conditions, and weather description with dynamic icons
- **3-Day Forecast**: Extended outlook with day/night periods showing temperature trends and conditions
- **Detailed Metrics**: 
  - Temperature with unit display (Fahrenheit)
  - Precipitation probability with visual indicators
  - Wind speed and direction information
  - Day/night specific weather icons

### User Experience

- **City Selection**: Browse and select from 500 major US cities via dropdown menu
- **Smart Caching**: 30-minute cache expiration reduces API calls and improves performance
- **Loading Indicators**: Visual feedback during data fetching operations
- **Responsive UI**: Clean, modern interface with CSS-styled components
- **Icon System**: Context-aware weather icons (50+ conditions including hurricanes, tornadoes, thunderstorms, snow, rain, fog, and clear skies)

### Technical Features

- **Proxy Pattern**: Implements caching layer to minimize redundant API requests
- **Factory Pattern**: WeatherIconFactory for efficient icon management and caching
- **JSON Parsing**: Jackson library integration for robust API response handling
- **Grid-based Forecasts**: Uses NWS grid system for precise location-based forecasts
- **Error Handling**: Graceful fallbacks and error messages for network issues

## 🛠️ Tech Stack

### Core Technologies

- **Java 21** - Latest LTS version with modern language features
- **JavaFX 21.0.2** - Rich desktop UI framework with FXML support
- **Maven** - Dependency management and build automation

### Libraries & APIs

- **Jackson Databind 2.17.0** - JSON serialization/deserialization
- **National Weather Service API** - Official weather data source (api.weather.gov)
- **JUnit Jupiter 5.9.1** - Unit testing framework

### Development Tools

- **Maven Compiler Plugin 3.11.0** - Java compilation with Java 21 support
- **Maven Surefire Plugin 2.22.1** - Automated test execution
- **JavaFX Maven Plugin 0.0.8** - JavaFX application packaging and execution

## 🏗️ Project Architecture

```
src/main/java/
├── JavaFX.java                # Main application entry point and UI controller
├── MyWeatherAPI.java          # Extended API client with point/grid lookup
├── WeatherProxy.java          # Caching proxy for API calls
├── WeatherIconFactory.java    # Factory for weather icon management
├── AppConstants.java          # Configuration constants and settings
├── PointProperties.java       # Grid point data model
└── weather/                   # Weather data models
    ├── Period.java            # Forecast period with temperature, conditions
    ├── Properties.java        # Forecast properties container
    ├── Root.java              # API root response model
    ├── WeatherAPI.java        # Base API client functionality
    ├── Geometry.java          # Geographic data structures
    ├── Elevation.java         # Elevation data model
    └── ProbabilityOfPrecipitation.java  # Precipitation data

src/main/resources/
├── uscities-500.csv           # City database with coordinates
├── uscities-1k.csv            # Extended city database
├── styles.css                 # Application styling
└── icons/                     # Weather icon assets (50+ icons)

src/test/java/
├── AppConstantsTest.java      # Configuration tests
├── MyWeatherAPITest.java      # API integration tests
├── WeatherIconFactoryTest.java # Icon factory tests
└── WeatherProxyTest.java      # Cache functionality tests
```

### Key Components

- **Proxy Pattern** (`WeatherProxy.java`): Implements intelligent caching with 30-minute TTL to reduce API load
- **Factory Pattern** (`WeatherIconFactory.java`): Preloads and manages 50+ weather icons with lazy loading
- **API Layer** (`MyWeatherAPI.java`): Extends base weather API with coordinate-to-grid conversion
- **UI Controller** (`JavaFX.java`): Manages application state and user interactions with event-driven architecture

## 🚀 Getting Started

### Prerequisites

- **Java JDK 21** or higher
- **Maven 3.6+** 
- Internet connection for NWS API access

### Installation

1. **Clone the repository**

```bash
git clone https://github.com/MichaelRosas/NWS-Weather-Forecast-App.git
cd NWS-Weather-Forecast-App
```

2. **Build the project**

```bash
mvn clean install
```

3. **Run the application**

```bash
mvn javafx:run
```

Or run directly with Maven:

```bash
mvn exec:java -Dexec.mainClass="JavaFX"
```

### Running Tests

```bash
mvn test
```

### Building JAR

```bash
mvn package
```

The executable JAR will be created in the `target/` directory.

## 📝 Available Maven Commands

```bash
mvn clean              # Clean build artifacts
mvn compile            # Compile source code
mvn test               # Run unit tests
mvn package            # Build JAR file
mvn javafx:run         # Launch JavaFX application
mvn install            # Install to local Maven repository
```

## 🔧 Configuration

### Application Constants

Customize behavior in [AppConstants.java](src/main/java/AppConstants.java):

- `CACHE_EXPIRATION_MS`: Cache duration (default: 30 minutes)
- `DEFAULT_CITY`: Initial city on startup (default: Chicago, IL)
- `CITY_DATA_FILE`: City database file path
- `WINDOW_WIDTH` / `WINDOW_HEIGHT`: Application window dimensions
- `WEATHER_ICON_SIZE`: Icon display sizes

### City Database

The application includes two CSV datasets:
- **uscities-500.csv**: 500 major US cities
- **uscities-1k.csv**: 1,000 cities for extended coverage

Format: `City,State,Latitude,Longitude`

### Custom Styling

Modify [styles.css](src/main/resources/styles.css) to customize the UI appearance with CSS variables and JavaFX-specific styles.

## 🌐 API Integration

### National Weather Service API

The application uses the [NWS API](https://www.weather.gov/documentation/services-web-api):

1. **Point Endpoint**: Converts lat/lng to grid coordinates
   ```
   GET /points/{latitude},{longitude}
   ```

2. **Forecast Endpoint**: Retrieves forecast data
   ```
   GET /gridpoints/{office}/{gridX},{gridY}/forecast
   ```

### Caching Strategy

- Forecasts cached for 30 minutes per grid location
- Automatic cache invalidation on expiration
- In-memory HashMap-based cache storage
- Cache key format: `"gridId-gridX-gridY"`

## 🧪 Testing

The application includes comprehensive unit tests:

- **API Tests**: Validates coordinate-to-grid conversion and forecast retrieval
- **Proxy Tests**: Verifies caching behavior and expiration logic
- **Icon Factory Tests**: Ensures correct icon selection for weather conditions
- **Constants Tests**: Validates configuration values

## 📦 Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| JavaFX Controls | 21.0.2 | UI components and layouts |
| JavaFX FXML | 21.0.2 | FXML support (future extension) |
| Jackson Databind | 2.17.0 | JSON parsing and serialization |
| JUnit Jupiter | 5.9.1 | Testing framework |

## 🎨 Weather Icons

The app includes icons for:
- Clear conditions (day/night variants)
- Clouds and fog
- Rain and drizzle
- Thunderstorms (with variants)
- Snow and sleet
- Hail and ice
- Wind and dust
- Severe weather (hurricanes, tornadoes)

Icons are preloaded at startup for optimal performance.

---

**Note**: This application requires an active internet connection to fetch weather data from the National Weather Service API. The API is free and does not require authentication.
