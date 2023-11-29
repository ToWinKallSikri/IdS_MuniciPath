package cs.unicam.ids.synk.model;

public class Response {
	
	private String message, lastCity;
	private Position position;
	private NavigationState state;
	
	public Response(String message, String lastCity, Position position, NavigationState state) {
		this.message = message;
		this.lastCity = lastCity;
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
	
}
