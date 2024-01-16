package Synk.Api.Model.User.Follow;

public class Follow {
	
	private String id;
    private String followed;
    private String username;
    
	public Follow() { }
	
	public Follow(String id, String followed, String username) {
		this.id = id;
		this.followed = followed;
		this.username = username;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFollowed() {
		return followed;
	}
	public void setFollowed(String followed) {
		this.followed = followed;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
    
    
	
}
