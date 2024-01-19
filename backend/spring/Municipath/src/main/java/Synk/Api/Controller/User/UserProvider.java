package Synk.Api.Controller.User;

import Synk.Api.Model.User.User;

public interface UserProvider {
	
	public User getUser(String username);
	
}
