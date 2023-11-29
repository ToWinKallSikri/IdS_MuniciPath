package cs.unicam.ids.synk.model;

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
	public boolean equals(Object o) {
		if(o != null && o instanceof Position) {
			Position other = (Position) o;
			return this.lat == other.lat && this.lon == other.lon;
		}
		return false;
	}
	
	public SettablePosition makeSettablePosition(){
		SettablePosition position = new SettablePosition();
		position.setLon(lon);
		position.setLat(lat);
		return position;
	}
	
}
