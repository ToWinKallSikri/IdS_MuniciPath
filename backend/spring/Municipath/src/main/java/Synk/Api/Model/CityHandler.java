package Synk.Api.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CityHandler {
	
    private ArrayList<City> cities;
    private UserHandler userHandler;
    private PointHandler pointHandler;

    public CityHandler(UserHandler uh, PointHandler ph) {
    	this.userHandler = uh;
    	this.pointHandler = ph;
        this.cities = new ArrayList<City>();
    }

    public boolean createCity(String cityName, int cap, String curator, Position pos ) {
    	String id = "" + (cityName+cap).hashCode();
        City c1 = new City(id, cityName,pos, curator, cap, new ArrayList<>());
        if (checkIfAlreadyExists(id))
        	return false;
        if(!this.userHandler.matchCurator(curator, id)) 
        	return false;
        cities.add(c1);
        this.pointHandler.addNewCity(id);
        this.pointHandler.createPost("Comune di "+cityName,
        		PostType.INSTITUTIONAL, "", curator, pos, id, true);
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
    		this.userHandler.changeCurator(curator, id)) )
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
    	this.userHandler.discreditCurator(cityId);
    	this.pointHandler.deleteCityPoints(cityId);
    	return true;
    }
    
    public boolean isAuthorized(String cityId, String username) {
    	return true;
    }

}