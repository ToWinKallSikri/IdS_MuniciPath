package Synk.Api.Model.City;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.MuniciPathMediator;



public class CityHandler {
	
    private ArrayList<City> cities;
    private MuniciPathMediator mediator;
    private RoleHandler roleHandler;

    public CityHandler(MuniciPathMediator mediator) {
    	this.mediator = mediator;
        this.cities = new ArrayList<City>();
        this.roleHandler = new RoleHandler();
    }

    public boolean createCity(String cityName, int cap, String curator, Position pos ) {
    	String id = "" + (cityName+cap).hashCode();
        City c1 = new City(id, cityName,pos, curator, cap, new ArrayList<>());
        if (checkIfAlreadyExists(id))
        	return false;
        if(!this.mediator.matchCurator(curator, id)) 
        	return false;
        cities.add(c1);
        this.mediator.createPostForNewCity(id, cityName, curator, pos);
        return true;
    }

    private boolean checkIfAlreadyExists (String cityId) {
        return cities.stream().parallel()
        		.anyMatch(c -> c.getId().equals(cityId));
    }
    
    private Optional<City> getOptCity (String cityId) {
        return cities.stream().parallel()
        		.filter(c -> c.getId().equals(cityId)).findFirst();
    }
    
    public City getCity(String cityId) {
    	return this.getOptCity(cityId).orElse(null);
    }
    
    public List<City> getCities(){
    	return this.getCities("");
    }
    
    public List<City> getCities(String search) {
    	return this.cities.stream()
    			.filter(c -> (c.getName()+c.getCap()).toLowerCase()
    					.startsWith(search.toLowerCase())).toList();
    }

    public boolean updateCity(String id, String cityName, int cap, String curator, Position pos ){
    	Optional<City> oCity = getOptCity(id);
    	if(oCity.isEmpty())
    		return false;
    	City city = oCity.get();
    	if(!(city.getCurator().equals(curator) || 
    		this.mediator.changeCurator(curator, id)) )
        	return false;
        city.setName(cityName);
        city.setCap(cap);
        city.setCurator(curator);
        city.setPos(pos);
		return true;
    }

    public boolean deleteCity(String cityId) {
    	Optional<City> oCity = getOptCity(cityId);
    	if(oCity.isEmpty())
    		return false;
    	City city = oCity.get();
    	this.cities.remove(city);
    	this.mediator.deleteCity(cityId);
    	return true;
    }
    
    public boolean isAuthorized(String cityId, String username) {
        return switch (this.roleHandler.getAuthorization(username, cityId).getRole()) {
            case CURATOR, CONTR_AUTH, CONTR_NOT_AUTH -> true;
            default -> false;
        };
	}

	public boolean canPublish(String cityId, String author) {
		Role role = this.roleHandler.getAuthorization(author, cityId).getRole();
		return role == Role.CURATOR || role == Role.CONTR_AUTH || role == Role.MODERATOR;
	}
	
	public Licence requestAuthorization(String username, String cityId) {
		if (!this.mediator.usernameExists(username)) {
			return null;
		}
		return cities.stream()
				.filter(c -> c.getId().equals(cityId) && c.getCurator().equals(username))
				.map(c -> new Licence(username, cityId, Role.CURATOR))
				.findFirst()
				.orElseGet(() -> this.roleHandler.getAuthorization(username, cityId));
	}
	
	public List<Licence> getAuthorizations(String cityId) {
		return this.roleHandler.getAuthorizations(cityId);
	}
	
	public boolean addRequest(String username, String cityId) {
		if(!this.mediator.usernameExists(username))
			return false;
		if(!this.cities.stream().anyMatch(c -> c.getId().equals(cityId)))
			return false;
		return this.roleHandler.addRequest(username, cityId);
	}
	
	public boolean judge(String requestId, boolean outcome) {
		return this.roleHandler.judge(requestId, outcome);
	}
	
	public boolean setRole(String username, String cityId, Role role) {
		if(!this.mediator.usernameExists(username))
			return false;
		City city = getCity(cityId);
		if(city == null || city.getCurator().equals(username))
			return false;
		return this.roleHandler.setRole(username, cityId, role);
	}

	public Role getRole(String username, String cityId) {
		if(!this.mediator.usernameExists(username)) {
			return null;
		}
		return this.roleHandler.getAuthorization(username, cityId).getRole();
	}

}