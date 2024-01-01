package Synk.Api.Model.Meteo;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import Synk.Api.Model.Post.Position;



public class WeatherForecast {
	
    private final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid=f11be85a0ecf5636165e348258c60dec";
    //API KEY PER OPENWEATHERMAP "f11be85a0ecf5636165e348258c60dec";
    //120 ore (5 giorni) / 3 ore = 40 previsioni
    
    List<Prediction> predictions;
    
    
    public WeatherForecast() {
    	predictions = new ArrayList<>();
    }
    
    
    public String getWeather(Position position, LocalDateTime time) {
    	Prediction prediction = searchPrediction(position, time);
    	if(prediction != null)
    		return prediction.getMeteo();
    	int count = getThreeHourIndex(time);
    	if(count > 39) return "?";
    	String meteo = callService(count, position);
    	this.predictions.add(new Prediction(position, time, meteo));
        return meteo;
    }
    
    private Prediction searchPrediction(Position position, LocalDateTime time) {
		return this.predictions.stream()
				.filter(p -> p.isTheSame(position, time))
				.findFirst().orElse(null);
	}


	private int getThreeHourIndex(LocalDateTime time) {
    	LocalDateTime now = LocalDateTime.now();
    	for(int count = 0; count < 40; count++) {
    		now = now.plusHours(1);
    		if(time.isBefore(now))
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
