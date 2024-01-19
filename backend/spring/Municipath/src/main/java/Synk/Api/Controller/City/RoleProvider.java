package Synk.Api.Controller.City;

import Synk.Api.Model.City.Role.Role;

public interface RoleProvider {
	
	public Role getRole(String username, String cityId);
	
}
