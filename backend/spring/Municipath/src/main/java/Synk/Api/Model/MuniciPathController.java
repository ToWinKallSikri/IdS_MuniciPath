package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Synk.Api.Model.City.City;
import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Pending.PendingHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.User.UserHandler;

public class MuniciPathController {
    private PointHandler poh;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    private PendingHandler peh;
    
    public MuniciPathController(){
    	this.uh = new UserHandler();
        this.ch = new CityHandler(uh, poh);
        this.gh = new GroupHandler();
        this.poh = new PointHandler(ch, gh);
        this.peh = new PendingHandler(poh, gh);
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
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, boolean published, Date start, Date end, boolean persistence) {
    	return this.poh.createPost(title, type, text, author, pos, cityId, data, published, start, end, persistence);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data) {
        return this.poh.editPost(postId, title, type, text, author, cityId, data);
    }
    
    public List<Point> getPoints (String cityID) {
          return this.poh.getPoints(cityID);
      
    }
    
    public List<Post> getPosts (String pointId) {
        return this.poh.getPosts(pointId);
    }
    
    public Post getPost(String postId) {
        return this.poh.getPost(postId);
    }
    
    public boolean deletePost (String postId, String author) {
    	return this.poh.deletePost(postId, author);
    }
    
}
