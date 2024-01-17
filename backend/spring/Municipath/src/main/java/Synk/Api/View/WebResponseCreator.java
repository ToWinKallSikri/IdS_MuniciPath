package Synk.Api.View;

import Synk.Api.Model.City.Role.Role;
import Synk.Api.ViewModel.WebResponse;

public class WebResponseCreator {
	
	public WebResponse make(String message) {
		return new WebResponse(message);
	}
	
	public WebResponse make(Role role) {
		return make(role.toString());
	}

	public WebResponse make(boolean bool) {
		return bool ? make("true") : make("false");
	}
	
}
