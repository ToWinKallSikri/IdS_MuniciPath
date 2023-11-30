package cs.unicam.ids.synk.model;

public class Response {
	
	private String message, lastCity, lastPost;
	private Position position;
	private NavigationState state;
	
	public Response(String message, String lastCity, String lastPost, Position position, NavigationState state) {
		this.message = message;
		this.lastCity = lastCity;
		this.lastPost = lastPost;
		this.position = position;
		this.state = state;
	}
	
	public String getMessage() {
		return message;
	}
	public String getLastCity() {
		return lastCity;
	}
	public Position getPosition() {
		return position;
	}
	public NavigationState getState() {
		return state;
	}

	public String getLastPost() {
		return lastPost;
	}
	
}
