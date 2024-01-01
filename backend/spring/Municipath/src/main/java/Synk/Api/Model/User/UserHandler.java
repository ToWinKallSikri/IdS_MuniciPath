package Synk.Api.Model.User;

import java.util.ArrayList;
import java.util.Optional;

import Synk.Api.Model.MuniciPathMediator;

public class UserHandler {

    private ArrayList<User> users;
    private NotificationHandler notifications;
    private MuniciPathMediator mediator;

    public UserHandler(MuniciPathMediator mediator) {
        users = new ArrayList<User>();
        this.notifications = new NotificationHandler();
        this.mediator = mediator;
    }
    
    private Optional<User> getOptUser(String username) {
    	return this.users.stream().parallel()
    			.filter(u -> u.getUsername().equals(username))
    			.findFirst();
    }
    
    private Optional<User> findCuratorOf(String cityId) {
    	return this.users.stream().parallel()
    			.filter(u -> cityId.equals(u.getCityId()))
    			.findFirst();
    }

    public boolean matchCurator(String curator, String cityId) {
    	Optional<User> oUser = getOptUser(curator);
    	if(oUser.isEmpty() || oUser.get().isCurator())
    		return false;
    	oUser.get().setCityId(cityId);
    	return true;
    }
    
    public boolean changeCurator(String curator, String cityId) {
    	Optional<User> oOld = findCuratorOf(cityId), oNew = getOptUser(curator);
    	if(oOld.isEmpty() || oNew.isEmpty() || oNew.get().isCurator())
    		return false;
    	oOld.get().setCityId(null);
    	oNew.get().setCityId(cityId);
		return true;
    }
    
    public boolean discreditCurator(String cityId) {
    	Optional<User> oCurator = findCuratorOf(cityId);
    	if(oCurator.isEmpty()) return false;
    	oCurator.get().setCityId(null);
    	return true;
    }

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	public boolean usernameExists(String username) {
		return this.users.stream().anyMatch(u -> u.getUsername().equals(username));
	}

	public void send(String username, String message) {
		if(usernameExists(username))
			this.notifications.send(username, message);
	}
    
}
