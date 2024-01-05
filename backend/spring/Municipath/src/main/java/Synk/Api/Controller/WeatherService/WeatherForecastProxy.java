package Synk.Api.Controller.WeatherService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.WeatherService.Prediction;

public class WeatherForecastProxy implements WeatherService {
	
	/**
	 * lista delle previsioni,
	 * vero servizio e momento di reset
	 */
    List<Prediction> predictions;
    WeatherForecast service;
    LocalDateTime reset;
    
    /**
     * costruttore del proxy
     */
    public WeatherForecastProxy(){
    	this.predictions = new ArrayList<>();
    	this.service = new WeatherForecast();
    	setReset();
    }
    
    /**
     * metodo per resettare il momento di reset delle
     * previsioni a tra 12 ore.
     */
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
    
    /**
     * controlla se il momento di reset e' arrivato
     * nel caso, lo aggiorna e cancella tutte le
     * previsioni
     */
    private void checkReset() {
		if(this.reset.isBefore(LocalDateTime.now())) {
			setReset();
			this.predictions.clear();
		}
	}
    
    /**
     * cerca le previsioni che corrispondono
     * parzialmente a quelle date in input
     * @param position posizione ricercata
     * @param time momento ricercata-o
     * @return se esiste, la previsione ricercata, altrimenti null
     */
	private Prediction searchPrediction(Position position, LocalDateTime time) {
		return this.predictions.stream()
				.filter(p -> p.almostEquals(position, time))
				.findFirst().orElse(null);
	}
    
}
