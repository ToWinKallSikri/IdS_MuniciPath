package Synk.Api.Model.User;


import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import Synk.Api.Model.MuniciPathMediator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserHandler {

    private NotificationHandler notifications;
    private MuniciPathMediator mediator;

	@Autowired
	private UserRepository userRepository;

    public UserHandler() {
        this.notifications = new NotificationHandler();
    }

	@PostConstruct
	public void init() {
		this.userRepository.save(new User("nibba","niggaruto", false));
	}

	public void setMediator(MuniciPathMediator mediator) {
		this.mediator = mediator;
	}
    
    private Optional<User> getOptUser(String username) {
    	return this.userRepository.findById(username);
    }
    
    private Optional<User> findCuratorOf(String cityId) {
    	return StreamSupport.stream(userRepository.findAll().spliterator(), true)
				.filter(u -> u.getCityId().equals(cityId) && u.isCurator()).findFirst();
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
    
    public void discreditCurator(String cityId) {
    	Optional<User> oCurator = findCuratorOf(cityId);
    	if(oCurator.isEmpty()) return;
    	oCurator.get().setCityId(null);

    }

	public List<User> getUsers() {
		return StreamSupport.stream(userRepository.findAll().spliterator(), true)
				.toList();
	}

	public void setUsers(List<User> users) {
		this.userRepository.saveAll(users);
	}

	public boolean usernameExists(String username) {
		return this.userRepository.existsById(username);
	}

	public void send(String username, String message) {
		if(usernameExists(username))
			this.notifications.send(username, message);
	}
    
}
