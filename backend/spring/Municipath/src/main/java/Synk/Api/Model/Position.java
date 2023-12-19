package Synk.Api.Model;

import java.util.Objects;

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

	@Override
	public int hashCode() {
		return Objects.hash(lat, lng);
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
				&& Double.doubleToLongBits(lng) == Double.doubleToLongBits(other.lng);
	}
	
	@Override
	public String toString() {
		return "" + this.hashCode();
	}
	
}
