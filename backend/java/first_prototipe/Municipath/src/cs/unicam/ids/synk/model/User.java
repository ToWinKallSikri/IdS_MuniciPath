package cs.unicam.ids.synk.model;

import java.util.ArrayList;

public class User {
	
	private String username;
	private String cityCurator;
	private ArrayList<String> postIDs;
	private boolean platformManager;
	
	public User(String username, String curator, ArrayList<String> postIDs, boolean manager) {
		this.username = username;
		this.cityCurator = curator;
		this.platformManager = manager;
		this.postIDs = postIDs;
	}
	
	public User(String username) {
		this(username, null, new ArrayList<String>(), false);
	}
	
	public User() {}
	
	public String getUsername() {
		return this.username;
	}
	
	public ArrayList<String> getPostIDs() {
		return this.postIDs;
	}
	
	public boolean getPlatformManager() {
		return this.platformManager;
	}
	
	public boolean isCurator() {
		return this.cityCurator != null;
	}
	
	public String getCityCurator() {
		return this.cityCurator;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setCityCurator(String cityCurator) {
		this.cityCurator = cityCurator;
	}

	public void setPostIDs(ArrayList<String> postIDs) {
		this.postIDs = postIDs;
	}

	public void setPlatformManager(boolean platformManager) {
		this.platformManager = platformManager;
	}
	
	public UserLog makeLog() {
		return new UserLog(this.username, this.cityCurator, this.postIDs, this.platformManager);
	}

	
}
