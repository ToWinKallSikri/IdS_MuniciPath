package Synk.Api.Controller.WeatherService;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.WeatherService.Json.Root;



public class WeatherForecast implements WeatherService {
	
    private final String WEATHER_URL = "http://api.weatherapi.com/v1/forecast.json?key=d9c1d96754924d61aa2172143242201&q={lat},{lon}&days=14";
    
    
    @Override
    public String getWeather(Position position, LocalDateTime time) {
    	LocalDateTime now = LocalDateTime.now();
    	int day = time.getDayOfYear() - now.getDayOfYear();
    	day = day < 0 ? day + 365 : day;
    	if(day > 13) return "?";
    	return callService(day, time.getHour(), position);
    }
    
	/**
	 * metodo privato che chiama un servizio meteo
	 * che permette di ottenere il meteo del giorno
	 * @param index lassi di tre ore da adesso
	 * @param pos posizione da ricercare
	 * @return meteo ricercato
	 */
    private String callService(int day, int hour, Position pos) {
        ResponseEntity<Root> response = new RestTemplate().getForEntity(
        		WEATHER_URL, Root.class, pos.getLat(), pos.getLng());
        if (response.getStatusCode() != HttpStatus.OK) return "?";
        return response.getBody().forecast
        		.forecastday.get(day).hour.get(hour).condition.icon;
    }
}
