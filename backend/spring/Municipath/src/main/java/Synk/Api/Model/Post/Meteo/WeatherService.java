package Synk.Api.Model.Post.Meteo;

import java.time.LocalDateTime;

import Synk.Api.Model.Post.Position;

public interface WeatherService {
	
	public String getWeather(Position position, LocalDateTime time);
	
}
