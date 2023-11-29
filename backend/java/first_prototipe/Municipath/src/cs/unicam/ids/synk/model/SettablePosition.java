package cs.unicam.ids.synk.model;

public class SettablePosition {
	
	private double lat, lon;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}
	
	public Position makePosition() {
		return new Position(lat, lon);
	}

}
