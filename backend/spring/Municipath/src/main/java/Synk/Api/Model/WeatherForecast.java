package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;



public class WeatherForecast {
	
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=f11be85a0ecf5636165e348258c60dec";
    //API KEY PER OPENWEATHERMAP "f11be85a0ecf5636165e348258c60dec";
    //120 ore (5 giorni) / 3 ore = 40 previsioni
    
    public String getWeather(City city, Date time) {
    	int count = getThreeHourIndex(time);
    	if(count > 39) return "?";
        return callService(count, city.getPos());
    }
    
    private int getThreeHourIndex(Date time) {
    	long now = new Date().getTime();
    	for(int count = 0; count < 40; count++) {
    		now += 360000;
    		if(time.getTime() < now)
    			return count;
    	}
    	return 40;
    }
    
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
