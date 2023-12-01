package cs.unicam.ids.synk.model;

import java.util.ArrayList;

public class User {
	
	private String username;
	private String password;
	private String cityCurator;
	private ArrayList<String> postIDs;
	private boolean platformManager;
	
	public User(String username, String password, String curator, ArrayList<String> postIDs, boolean manager) {
		this.username = username;
		this.password = password;
		this.cityCurator = curator;
		this.platformManager = manager;
		this.postIDs = postIDs;
	}
	
	public User(String username, String password) {
		this(username, password, "null", new ArrayList<String>(), false);
	}
	
	public User() {}
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCityCurator() {
		return cityCurator;
	}

	public void setCityCurator(String cityCurator) {
		this.cityCurator = cityCurator;
	}

	public ArrayList<String> getPostIDs() {
		return postIDs;
	}

	public void setPostIDs(ArrayList<String> postIDs) {
		this.postIDs = postIDs;
	}

	public boolean isPlatformManager() {
		return platformManager;
	}

	public void setPlatformManager(boolean platformManager) {
		this.platformManager = platformManager;
	}

	public UserLog makeLog() {
		return new UserLog(this.username, this.cityCurator, this.postIDs, this.platformManager);
	}

	public boolean isCurator() {
		return ! this.getCityCurator().equals("null");
	}
	
}
