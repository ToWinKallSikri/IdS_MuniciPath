package Synk.Api.Model.Post;

import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class Position {
	
	private double lat;
	private double lng;
	
	public Position() {}

    public Position(double d, double e) {
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
		return Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(other.lat)
				&& Double.doubleToLongBits(this.lng) == Double.doubleToLongBits(other.lng);
	}
	
	/**
	 * ritorna true se due posizioni sono identiche per
	 * le tre prime cifre decimali
	 * @param obj posizione da controllare
	 * @return true se sono quasi uguali, false altrimenti.
	 */
	public boolean almostEquals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Math.round(this.lat * 1000) == Math.round(other.lat * 1000)
				&& Math.round(this.lng * 1000) == Math.round(other.lng * 1000);
	}
	
	@Override
	public String toString() {
		return "" + this.hashCode();
	}
	
}
