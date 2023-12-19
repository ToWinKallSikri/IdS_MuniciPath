package Synk.Api.Model;

import java.util.List;

public class MuniciPathController {
    private PointHandler ph;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    
    public MuniciPathController(){
    	this.uh = new UserHandler();
        this.ch = new CityHandler(uh);
        this.gh = new GroupHandler();
        this.ph = new PointHandler(ch, gh);
    }
    
    public boolean createCity(String cityName, int cap, String curator, Position pos ) {
        return ch.createCity(cityName, cap, curator, pos);
    }

    public boolean updateCity(String id, String cityName, int cap, String curator, Position pos ){
		return ch.updateCity(id, cityName, cap, curator, pos);
    }
    
    public boolean deleteCity(String cityId) {
    	return ch.deleteCity(cityId);
    }
    
    public City getCity(String cityId) {
    	return ch.getCity(cityId);
    }
    
    public List<City> searchCity(String search){
    	return ch.getCities(search);
    }
    
    public List<City> getAllCity(String search){
    	return ch.getCities();
    } 
    
}
