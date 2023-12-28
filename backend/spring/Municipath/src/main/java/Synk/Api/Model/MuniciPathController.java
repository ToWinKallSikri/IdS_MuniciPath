package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MuniciPathController {
    private PointHandler ph;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    
    public MuniciPathController(){
    	this.uh = new UserHandler();
        this.ch = new CityHandler(uh, ph);
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
    
    public List<City> searchCity(String search){
    	return ch.getCities(search);
    }
    
    public List<City> getAllCity(){
    	return ch.getCities();
    }
    
    public boolean createEvent(String title, String text, String author, Position pos, String cityId, 
    		ArrayList<String> data, boolean published, Date start, Date end, boolean persistence) {
    	return this.ph.createEvent(title, text, author, pos, cityId, data, published, start, end, persistence);
    }
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, boolean published) {
    	return this.ph.createPost(title, type, text, author, pos, cityId, data, published);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data) {
        return this.ph.editPost(postId, title, type, text, author, cityId, data);
    }
    
    public List<Point> getPoints (String cityID) {
          return this.ph.getPoints(cityID);
      
    }
    
    public List<Post> getPosts (String pointId) {
        return this.ph.getPosts(pointId);
    }
    
    public Post getPost(String postId) {
        return this.ph.getPost(postId);
    }
    
    public boolean deletePost (String postId, String author) {
    	return this.ph.deletePost(postId, author);
    }
    
}
