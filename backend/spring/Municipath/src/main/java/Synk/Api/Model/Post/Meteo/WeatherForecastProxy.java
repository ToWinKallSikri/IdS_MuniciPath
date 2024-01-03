package Synk.Api.Model.Post.Meteo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Synk.Api.Model.Post.Position;

public class WeatherForecastProxy implements WeatherService {

    List<Prediction> predictions;
    WeatherForecast service;
    LocalDateTime reset;
    
    public WeatherForecastProxy(){
    	this.predictions = new ArrayList<>();
    	this.service = new WeatherForecast();
    	setReset();
    }
    
    private void setReset() {
    	this.reset = LocalDateTime.now().plusHours(12);
    }
    
    @Override
    public String getWeather(Position position, LocalDateTime time) {
    	checkReset();
    	Prediction prediction = searchPrediction(position, time);
    	if(prediction != null)
    		return prediction.getMeteo();
    	String meteo = service.getWeather(position, time);
    	this.predictions.add(new Prediction(position, time, meteo));
        return meteo;
    }

    
    private void checkReset() {
		if(this.reset.isBefore(LocalDateTime.now())) {
			setReset();
			this.predictions.clear();
		}
	}

	private Prediction searchPrediction(Position position, LocalDateTime time) {
		return this.predictions.stream()
				.filter(p -> p.almostEquals(position, time))
				.findFirst().orElse(null);
	}
    
}
