package Synk.Api.Model.Meteo;

import java.time.LocalDateTime;

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
	
	public boolean isTheSame(Position position, LocalDateTime time) {
		return this.position.equals(position) && this.time.equals(time);
	}

	public String getMeteo() {
		return meteo;
	}
	
}
