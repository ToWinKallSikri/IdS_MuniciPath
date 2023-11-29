package cs.unicam.ids.synk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import cs.unicam.ids.synk.model.City;
import cs.unicam.ids.synk.model.User;
import cs.unicam.ids.synk.model.UserLog;

public class UserData {
	
	private HashMap<User, String> users;
	
	private static UserData singleton;
	
	private UserData() {
		this.users = new HashMap<>();
	}
	
	public static synchronized UserData getUserData() {
		if(singleton == null)
			singleton = new UserData();
		return singleton;
	}
	
	public synchronized boolean registration(String username, String password) {
		if(users.keySet().stream().filter(u -> u.getUsername().equals(username)).count()==0)
			return false;
		users.put(new User(username), password);
		return true;
	}
	
	public synchronized Optional<UserLog> getUser(String username, String password) {
		return users.entrySet().stream().filter(e -> e.getValue().equals(password))
		.map(Entry::getKey).filter(u -> u.getUsername().equals(username)).map(User::makeLog).findFirst();
	}
	
	public synchronized boolean makeCurator(UserLog manager, City newCity, String newCuratorUsername) {
		Optional<User> Ouser = findUser(newCuratorUsername);
		if(manager.isPlatformManager() && Ouser.isPresent()) {
			User user = Ouser.get();
			if(! user.isCurator()) {
				addCurator(user, newCity);
				return true;
			}
		}
		return false;
	}
	
	private void addCurator(User oldCurator, City newCity) {
		User newUser = new User(oldCurator.getUsername(),
				newCity.getID(), oldCurator.getPostIDs(), oldCurator.getPlatformManager());
		String psw = this.users.get(oldCurator);
		this.users.remove(oldCurator);
		this.users.put(newUser, psw);
	}
	
	private Optional<User> findUser(String username) {
		return this.users.keySet().stream()
				.filter(user -> user.getUsername().equals(username)).findFirst();
	}
	
	public synchronized boolean addPostToUser(String username, String postID) {
		Optional<ArrayList<String>> result = this.users.keySet().stream().parallel()
			.map(User::getPostIDs).filter(l -> l.contains(postID)).findFirst();
		if(result.isPresent()) return false;
		Optional<User> user = this.users.keySet().stream().parallel()
				.filter(u -> u.getUsername().equals(username)).findFirst();
		return user.get().getPostIDs().add(postID);
	}
	
}
