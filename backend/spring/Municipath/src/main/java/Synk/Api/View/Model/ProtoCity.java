package Synk.Api.View.Model;


import Synk.Api.Model.Post.Position;

public class ProtoCity {
	private String cityName;
	private int cap;
	private String curator;
	private Position pos;
	
	public ProtoCity() {}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}

	public String getCurator() {
		return curator;
	}

	public void setCurator(String curator) {
		this.curator = curator;
	}

	public Position getPos() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
}
