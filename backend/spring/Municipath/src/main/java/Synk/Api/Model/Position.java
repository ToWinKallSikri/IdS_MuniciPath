package Synk.Api.Model;

public class Position {

	private double lat;
	private double lng;
	
	public Position() {}

    public Position(double d, double e) {
		super();
		this.lat = d;
		this.lng = e;
	}

	
    public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}

}
