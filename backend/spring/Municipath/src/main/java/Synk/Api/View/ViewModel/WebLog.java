package Synk.Api.View.ViewModel;

public class WebLog {
	
	private String username;
	
	private String password;

	public WebLog(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public WebLog() { }

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
	
}
