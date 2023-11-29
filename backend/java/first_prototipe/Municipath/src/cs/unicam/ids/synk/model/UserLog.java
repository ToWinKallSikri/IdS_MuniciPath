package cs.unicam.ids.synk.model;

import java.util.ArrayList;

public class UserLog {
	
	private String username;
	private String cityCurator;
	private ArrayList<String> postIDs;
	private boolean platformManager;
	
	public UserLog(String username, String curator, ArrayList<String> postIDs, boolean manager) {
		this.username = username;
		this.cityCurator = curator;
		this.platformManager = manager;
		this.postIDs = postIDs;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public ArrayList<String> getPostIDs() {
		ArrayList<String> list = new ArrayList<String>();
		for(String id : this.postIDs) {
			list.add(id);
		}
		return list;
	}
	
	public boolean isPlatformManager() {
		return this.platformManager;
	}
	
	public boolean isCurator() {
		return this.cityCurator != null;
	}
	
	public String getCityCurator() {
		return this.cityCurator;
	}
	
}
