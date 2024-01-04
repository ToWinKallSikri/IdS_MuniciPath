package Synk.Api.Model.Post.Meteo;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import Synk.Api.Model.Post.Position;



public class WeatherForecast implements WeatherService {
	
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=f11be85a0ecf5636165e348258c60dec";
    //API KEY PER OPENWEATHERMAP "f11be85a0ecf5636165e348258c60dec";
    //120 ore (5 giorni) / 3 ore = 40 previsioni
    
    
    @Override
    public String getWeather(Position position, LocalDateTime time) {
    	int count = getThreeHourIndex(time);
    	if(count > 39) return "?";
    	return callService(count, position);
    }

    /**
     * Ritorna il numero di lassi di tempo di tre ore che corrispondono
     * alla distanza tra il momento attuale e quello dato
     * @param time momento dato
     * @return numero di lassi di tre ore
     */
	private int getThreeHourIndex(LocalDateTime time) {
    	LocalDateTime now = LocalDateTime.now();
    	for(int count = 0; count < 40; count++) {
    		now = now.plusHours(1);
    		if(time.isBefore(now))
    			return count;
    	}
    	return 40;
    }
    
	/**
	 * metodo privato che chiama un servizio meteo
	 * che permette di ottenere il meteo del giorno
	 * @param index lassi di tre ore da adesso
	 * @param pos posizione da ricercare
	 * @return meteo ricercato
	 */
    private String callService(int index, Position pos) {
        ResponseEntity<String> response = new RestTemplate().getForEntity(
        		WEATHER_URL, String.class, pos.getLat(), pos.getLng());
        if (response.getStatusCode() != HttpStatus.OK) return "?";
        return new ArrayList<String>(Arrays.asList(response.getBody().split("main\":\"")))
        		.stream().filter(s -> !s.contains("cod"))
        		.map(s -> s.subSequence(0, s.indexOf("\",")).toString())
        		.toList().get(index);
    }
}
