package Synk.Api.Model.Post.Meteo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Synk.Api.Model.Post.Position;

public class Prediction {
	
	private Position position;
	private LocalDateTime time;
	private String meteo;
	
	public Prediction(Position position, LocalDateTime time, String meteo) {
		this.position = position;
		this.time = time;
		this.meteo = meteo;
	}
	
	/**
	 * metodo per controllare se due una posizione e un momento
	 * possono corrispondere a quello della predizione
	 * @param position posizione
	 * @param time momento
	 * @return true se sono simili, false altrimenti
	 */
	public boolean almostEquals(Position position, LocalDateTime time) {
		return this.position.almostEquals(position) && almostEquals(this.time, time);
	}
	
	/**
	 * metodo privato per vedere se due momenti sono 
	 * vicini di almeno tre ore.
	 * @param t1 primo momento
	 * @param t2 secondo momento
	 * @return true se la loro distanza e' minore di
	 * tre ore, false altrimenti
	 */
	private boolean almostEquals(LocalDateTime t1, LocalDateTime t2) {
		return Math.abs(ChronoUnit.HOURS.between(t1, t2)) < 3;
	}

	public String getMeteo() {
		return meteo;
	}
	
}
