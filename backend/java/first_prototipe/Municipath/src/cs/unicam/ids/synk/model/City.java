package cs.unicam.ids.synk.model;

import java.util.ArrayList;

public class City {
	
	private Position position;
	private String ID;
	private String name;
	private ArrayList<String> postIDs;
	private String curator;
	
	public City(Position position, String ID, String name, String curator, ArrayList<String> postIDs) {
		this.position = position;
		this.ID = ID;
		this.name = name;
		this.curator = curator;
		this.postIDs = postIDs;
	}
	
	public City(Position position, String ID, String name, String curator) {
		this(position, ID, name, curator, new ArrayList<String>());
	}
	
	public City() {}
	
	public Position getPosition() {
		return this.position;
	}
	
	public String getID() {
		return this.ID;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<String> getPostIDs() {
		return this.postIDs;
	}
	
	public void setPosition(Position position) {
		this.position = position;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPostIDs(ArrayList<String> postIDs) {
		this.postIDs = postIDs;
	}

	public String getCurator() {
		return curator;
	}

	public void setCurator(String curator) {
		this.curator = curator;
	}
	
	
	
}