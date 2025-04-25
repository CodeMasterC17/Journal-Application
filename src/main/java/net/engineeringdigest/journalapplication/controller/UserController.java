package net.engineeringdigest.journalapplication.controller;

import net.engineeringdigest.journalapplication.api.response.WeatherResponse;
import net.engineeringdigest.journalapplication.entity.User;
import net.engineeringdigest.journalapplication.repository.UserRepository;
import net.engineeringdigest.journalapplication.service.UserService;
import net.engineeringdigest.journalapplication.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
//@Tag(name = "User APIs", description = "Read, Update & Delete User")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final WeatherService weatherService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, WeatherService weatherService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.weatherService = weatherService;
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userInDb = userService.findByUserName(userName);
        userInDb.setUserName(user.getUserName());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUserById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUserName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<String> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greeting = "";
        if (weatherResponse != null) {
            greeting = ", Weather feels like " + weatherResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + greeting, HttpStatus.OK);
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<String> getWeatherForCity(@PathVariable String city) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather(city);

        if (weatherResponse != null && weatherResponse.getCurrent() != null) {
            WeatherResponse.Current current = weatherResponse.getCurrent();

            String sb = "Hi " + authentication.getName() + ", here's the current weather:\n" + "Observation Time: " + current.getObservationTime() + "\n" +
                    "Temperature: " + current.getTemperature() + "°C\n" +
                    "Feels Like: " + current.getFeelslike() + "°C\n" +
                    "Weather: " + current.getWeatherDescriptions() + "\n" +
                    "Wind Speed: " + current.getWindSpeed() + " km/h\n" +
                    "Wind Direction: " + current.getWindDir() + " (" + current.getWindDegree() + "°)\n" +
                    "Humidity: " + current.getHumidity() + "%\n" +
                    "Pressure: " + current.getPressure() + " hPa\n" +
                    "Precipitation: " + current.getPrecip() + " mm\n" +
                    "Cloud Cover: " + current.getCloudcover() + "%\n" +
                    "UV Index: " + current.getUvIndex() + "\n" +
                    "Visibility: " + current.getVisibility() + " km\n" +
                    "Daytime: " + (current.getIsDay().equals("yes") ? "Yes" : "No") + "\n";

            return new ResponseEntity<>(sb, HttpStatus.OK);
        }

        return new ResponseEntity<>("Hi " + authentication.getName() + ", weather data not available.", HttpStatus.OK);
    }
}
