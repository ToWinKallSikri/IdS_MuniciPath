package Synk.Api.Model.Meteo;

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
	
	public boolean almostEquals(Position position, LocalDateTime time) {
		return this.position.almostEquals(position) && almostEquals(this.time, time);
	}
	
	private boolean almostEquals(LocalDateTime t1, LocalDateTime t2) {
		return Math.abs(ChronoUnit.HOURS.between(t1, t2)) < 3;
	}

	public String getMeteo() {
		return meteo;
	}
	
}
