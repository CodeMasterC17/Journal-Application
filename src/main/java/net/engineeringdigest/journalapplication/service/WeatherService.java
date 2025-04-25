package net.engineeringdigest.journalapplication.service;

import net.engineeringdigest.journalapplication.api.response.WeatherResponse;
import net.engineeringdigest.journalapplication.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final AppCache appCache;
    private final RedisService redisService;

    @Autowired
    public WeatherService(RestTemplate restTemplate, AppCache appCache, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.appCache = appCache;
        this.redisService = redisService;
    }


    public WeatherResponse getWeather(String city) {
        WeatherResponse weatherResponse = redisService.get("weather_0f_" + city, WeatherResponse.class);
        if(weatherResponse != null) {
            return weatherResponse;
        } else {
            String finalAPI = appCache.appCache.get("WEATHER_API").replace("<city>", city).replace("<apiKey>", apiKey);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if(body != null) {
                redisService.set("weather_0f_" + city, body, 300L);
            }
            return body;
        }
    }
}
