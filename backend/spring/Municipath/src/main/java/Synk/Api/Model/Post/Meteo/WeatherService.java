package Synk.Api.Model.Post.Meteo;

import java.time.LocalDateTime;

import Synk.Api.Model.Post.Position;

public interface WeatherService {
	
	/**
	 * Metodo che restituisce una stringa contenente
	 * le previsioni meteo previste per
	 * una data posizione e un dato momento
	 * @param position posizione da controllare
	 * @param time momento da controllare
	 * @return previsione prevista, o "?" se non recuperabile
	 */
	public String getWeather(Position position, LocalDateTime time);
	
}
