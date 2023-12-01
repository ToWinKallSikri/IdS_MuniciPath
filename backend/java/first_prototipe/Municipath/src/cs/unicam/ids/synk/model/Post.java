package cs.unicam.ids.synk.model;

public class Post {
	
	private String ID, title, text, author, cityID;
	private Position position;
	private boolean approved;
	
	public Post() {}
	
	
	public Post(String iD, String title, String text, String author, 
			String cityID, Position position, boolean approved) {
		this.ID = iD;
		this.title = title;
		this.text = text;
		this.author = author;
		this.cityID = cityID;
		this.position = position;
		this.approved = approved;
	}


	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public boolean getApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
}

