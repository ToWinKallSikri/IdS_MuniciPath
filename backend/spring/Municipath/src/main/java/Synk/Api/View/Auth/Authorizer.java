package Synk.Api.View.Auth;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.AuthorProvider;
import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.City.RoleProvider;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Controller.User.UserProvider;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.User.User;

@Service
public class Authorizer {
	
	private RoleProvider roleProvider;
	private AuthorProvider postAuthorProvider, groupAuthorProvider;
	private UserProvider userProvider;
    private IdentifierManager idManager;
    private final String LIMITED = "unregistered_tourist";
	
    
	public Authorizer(CityHandler city, PointHandler post,
						GroupHandler group, UserHandler user) {
		this.roleProvider = city;
		this.postAuthorProvider = post;
		this.groupAuthorProvider = group;
		this.userProvider = user;
		this.idManager = new IdentifierManager();
	}
	
	
	public boolean isManager(String username) {
    	User user = this.userProvider.getUser(username);
    	return user != null && user.isManager();
    }
	
	
	public boolean isCurator(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		Role role = this.roleProvider.getRole(username, cityId);
		return role == Role.CURATOR;
	}
	
	
	public boolean isStaff(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		Role role = this.roleProvider.getRole(username, cityId);
		return role == Role.CURATOR || role == Role.MODERATOR;
	}

    
	public boolean isAuthorOf(String username, String contentId) {
		return username.equals(this.idManager.isGroup(contentId) ?
				this.groupAuthorProvider.getAuthor(contentId) :
					this.postAuthorProvider.getAuthor(contentId));
	}
	
	
	public boolean havePowerWithIt(String username, String contentId) {
		return isStaff(username, idManager.getCityId(contentId))
				|| isAuthorOf(username, contentId);
	}
	
	
	public boolean isNotLimited(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		if(isLogged(username)) {
			Role role = this.roleProvider.getRole(username, cityId);
			return role != Role.LIMITED;
		} else return false;
	}
	
	
	public boolean isLogged(String username) {
		return !username.equals(LIMITED);
	}
	
}
