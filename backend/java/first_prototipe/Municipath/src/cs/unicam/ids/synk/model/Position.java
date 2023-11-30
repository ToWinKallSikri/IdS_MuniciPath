package cs.unicam.ids.synk.model;

import java.util.Objects;

public class Position {
	
	private double lat, lon;
	
	public Position(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public double getLat() {
		return this.lat;
	}
	
	public double getLon() {
		return this.lon;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(lat, lon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Double.doubleToLongBits(lat) == Double.doubleToLongBits(other.lat)
	}
	
	@Override
	public String toString() {
		return this.lat + " " + this.lon;
	}
	
	public SettablePosition makeSettablePosition(){
		SettablePosition position = new SettablePosition();
		position.setLon(lon);
		position.setLat(lat);
		return position;
	}
	
}
