package cs.unicam.ids.synk.controller.command;

import cs.unicam.ids.synk.controller.CityData;
import cs.unicam.ids.synk.model.NavigationState;
import cs.unicam.ids.synk.model.Position;
import cs.unicam.ids.synk.model.Response;
import cs.unicam.ids.synk.model.UserLog;

public abstract class Command {
	
	protected CommandType type;
	private UserLog userLog;
	private String lastCity;
	private String lastPost;
	private Position position;
	private NavigationState state;
	private String [] info;
	
	
	public Command(UserLog userLog, String lastCity, String lastPost,
			Position position, NavigationState state, String[] info) {
		this.userLog = userLog;
		this.lastCity = lastCity;
		this.lastPost = lastPost;
		this.position = position;
		this.state = state;
		this.info = info;
	}

	public CommandType getType() {
		return type;
	}

	public UserLog getUserLog() {
		return userLog;
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

	public String[] getInfo() {
		return info;
	}
	
	public String getLastPost() {
		return lastPost;
	}

	public abstract Response execute(CityData data);
	
}
