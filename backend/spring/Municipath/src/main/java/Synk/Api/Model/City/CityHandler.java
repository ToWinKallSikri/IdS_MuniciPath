package Synk.Api.Model.City;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.City.Role.Licence;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleHandler;
import Synk.Api.Model.City.Role.RoleRequest;


@Repository
public class CityHandler {
	
    private MuniciPathMediator mediator;
    @Autowired
    private  RoleHandler roleHandler;
    @Autowired
	private CityRepository cityRepository;
    
    public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
    
    
    public boolean createCity(String cityName, int cap, String curator, Position pos ) {
    	String id = "" + (cityName+cap).hashCode();
        City c1 = new City(id, cityName,pos, curator, cap);
        if (checkIfAlreadyExists(id))
        	return false;
        if(!this.mediator.matchCurator(curator, id)) 
        	return false;
        cityRepository.save(c1);
        this.mediator.createPostForNewCity(id, cityName, curator, pos);
        this.roleHandler.addCity(id, curator);
        return true;
    }

    private boolean checkIfAlreadyExists (String cityId) {
    	return this.cityRepository.existsById(cityId);
    }
    
    public City getCity(String cityId) {
    	return this.cityRepository.findById(cityId).orElse(null);
    }
    
    public List<City> getCities(){
    	return this.getCities("");
    }
    
    public List<City> getCities(String search) {
    	return StreamSupport.stream(cityRepository.findAll().spliterator(), true)
    			.filter(c -> (c.getName()+c.getCap()).toLowerCase()
    					.startsWith(search.toLowerCase())).toList();
    }

    public boolean updateCity(String id, String cityName, int cap, String curator, Position pos ){
    	City city = getCity(id);
    	if(city == null)
    		return false;
    	if(!city.getCurator().equals(curator))
    		if(!this.mediator.changeCurator(curator, id))
    			return false;
        city.setName(cityName);
        city.setCap(cap);
        city.setCurator(curator);
        city.setPos(pos);
        this.cityRepository.save(city);
		return true;
    }

    public boolean deleteCity(String cityId) {
    	City city = getCity(cityId);
    	if(city == null)
    		return false;
    	this.cityRepository.delete(city);
    	this.mediator.deleteCity(cityId);
    	this.roleHandler.removeCity(cityId);
    	return true;
    }
    
    public boolean isAuthorized(String cityId, String username) {
    	Role role = getAuthorization(username, cityId).getRole();
    	return role != Role.LIMITED && role != Role.TOURIST;
	}


	public boolean canPublish(String cityId, String author) {
		Role role = getAuthorization(author, cityId).getRole();
		return role == Role.CURATOR || role == Role.CONTR_AUTH || role == Role.MODERATOR;
	}
	
	public Licence getAuthorization(String username, String cityId) {
		if (!this.mediator.usernameExists(username)) 
			return null;
		return this.roleHandler.getAuthorization(username, cityId);
	}
	
	public List<Licence> getAuthorizations(String cityId) {
		return this.roleHandler.getAuthorizations(cityId);
	}
	
	public boolean setRole(String username, String cityId, Role role) {
		if(!this.mediator.usernameExists(username))
			return false;
		City city = getCity(cityId);
		if(city == null || city.getCurator().equals(username))
			return false;
		return this.roleHandler.setRole(username, cityId, role);
	}
	
	public boolean addModerator(String username, String cityId) {
		if(!this.mediator.usernameExists(username))
			return false;
		City city = getCity(cityId);
		if(city == null || city.getCurator().equals(username))
			return false;
		return this.roleHandler.addModerator(username, cityId);
	}
	
	public boolean removeModerator(String username, String cityId) {
		if(!this.mediator.usernameExists(username))
			return false;
		City city = getCity(cityId);
		if(city == null || city.getCurator().equals(username))
			return false;
		return this.roleHandler.removeModerator(username, cityId);
	}

	public Role getRole(String username, String cityId) {
		return this.getAuthorization(username, cityId).getRole();
	}
	
	public boolean addRequest(String username, String cityId) {
		if(getCity(cityId) == null || !this.mediator.usernameExists(username))
			return false;
		return this.roleHandler.addRequest(username, cityId);
	}
	
	public List<RoleRequest> getRequests(String cityId) {
		if(this.checkIfAlreadyExists(cityId))
			return null;
		return this.roleHandler.getRequests(cityId);
	}
	
	public boolean judge(String requestId, boolean outcome) {
		return this.roleHandler.judge(requestId, outcome);
	}

}