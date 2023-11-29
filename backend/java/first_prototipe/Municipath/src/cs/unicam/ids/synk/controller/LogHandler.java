package cs.unicam.ids.synk.controller;

import java.util.Optional;
import cs.unicam.ids.synk.model.UserLog;

public class LogHandler {
	
	private UserLog loggedUser;
	private UserData data;
	
	public LogHandler() {
		this.data = UserData.getUserData();
		this.loggedUser = null;
	}
	
	public boolean logIn(String username, String password) {
		if(this.loggedUser != null)
			return false;
		Optional<UserLog> user = data.getUser(username, password);
		if(user.isEmpty())
			return false;
		this.loggedUser = user.get();
		return true;
	}
	
	public boolean logOut() {
		if(this.loggedUser == null)
			return false;
		this.loggedUser = null;
		return true;
	}
	
	public boolean signIn(String username, String password) {
		return data.registration(username, password);
	}
	
	public UserLog getLoggedUser() {
		return this.loggedUser;
	}
	
}
