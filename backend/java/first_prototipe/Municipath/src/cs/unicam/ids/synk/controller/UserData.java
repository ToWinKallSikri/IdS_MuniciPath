package cs.unicam.ids.synk.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cs.unicam.ids.synk.model.City;
import cs.unicam.ids.synk.model.User;
import cs.unicam.ids.synk.model.UserLog;

public class UserData {
	
	private final ArrayList<User> users;
	
	private static UserData singleton;
	
	private UserData() {
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<User>>() {}.getType();
		this.users = gson.fromJson(getJson(), type);
	}
	
	public static synchronized UserData getUserData() {
		if(singleton == null)
			singleton = new UserData();
		return singleton;
	}
	
	private String getJson() {
		try {
			File file = new File("user.json");
		    @SuppressWarnings("resource")
			Scanner myReader = new Scanner(file);
		    return myReader.nextLine();
		} catch (FileNotFoundException e) {
			return "[]";
		}
	}
	
	public synchronized boolean registration(String username, String password) {
		if(users.stream().anyMatch(u -> u.getUsername().equals(username)))
			return false;
		users.add(new User(username, password));
    	saveAll();
		return true;
	}
	
	public synchronized Optional<UserLog> getUser(String username, String password) {
		return users.stream().filter(u -> u.getPassword().equals(password))
				.filter(u -> u.getUsername().equals(username)).map(User::makeLog).findFirst();
	}
	
	public synchronized boolean makeCurator(UserLog manager, City newCity, String newCuratorUsername) {
		Optional<User> Ouser = findUser(newCuratorUsername);
		if(manager.isPlatformManager() && Ouser.isPresent()) {
			User user = Ouser.get();
			if(! user.isCurator()) {
                user.setCityCurator(newCity.getID());
            	saveAll();
				return true;
			}
		}
		return false;
	}
	
	private Optional<User> findUser(String username) {
		return this.users.stream().filter(user -> user.getUsername()
				.equals(username)).findFirst();
	}
	
	public synchronized boolean addPostToUser(String username, String postID) {
		Optional<ArrayList<String>> result = this.users.stream().parallel()
			.map(User::getPostIDs).filter(l -> l.contains(postID)).findFirst();
		if(result.isPresent()) return false;
        if( findUser(username).map(value -> value.getPostIDs().add(postID)).orElse(false)) {
        	saveAll();
        	return true;
        }
        return false;
    }
	
	private void saveAll() {
	    try {
			Gson gson = new Gson();
			String data = gson.toJson(this.users);
			File file = new File("user.json");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(data);
			writer.close();
	    } catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
