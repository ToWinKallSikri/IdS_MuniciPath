package Synk.Api.Model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
	@Id
    private String username;
    private String password;
    private boolean isManager;
    private String cityId;
    
	public User(String username, String password, boolean isManager) {
		this.username = username;
		this.password = password;
		this.isManager = isManager;
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
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public boolean isCurator() {
		return this.cityId != null;
	}
}
