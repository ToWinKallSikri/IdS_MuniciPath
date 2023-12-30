package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Synk.Api.Model.City.City;
import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.City.Licence;
import Synk.Api.Model.City.Role;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Pending.PendingHandler;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.User.UserHandler;

public class MuniciPathModel {
    private PointHandler poh;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    private PendingHandler peh;
    
    public MuniciPathModel(){
    	MuniciPathMediator mediator = new MuniciPathMediator();
    	this.uh = new UserHandler(mediator);
        this.ch = new CityHandler(mediator);
        this.gh = new GroupHandler(mediator);
        this.poh = new PointHandler(mediator);
        this.peh = new PendingHandler(mediator);
        mediator.setUser(uh);
        mediator.setCity(ch);
        mediator.setGroup(gh);
        mediator.setPoint(poh);
        mediator.setPending(peh);
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
    
    public Licence requestAuthorization(String username, String cityId) {
		return this.ch.requestAuthorization(username, cityId);
	}
	
	public List<Licence> requestAuthorizations(String cityId) {
		return this.ch.requestAuthorizations(cityId);
	}
	
	public boolean addRequest(String username, String cityId) {
		return this.ch.addRequest(username, cityId);
	}
	
	public boolean judge(String requestId, boolean outcome) {
		return this.ch.judge(requestId, outcome);
	}
	
	public boolean setRole(String username, String cityId, Role role) {
		return this.ch.setRole(username, cityId, role);
	}
	
	public Role getRole(String username, String cityId) {
		return this.ch.getRole(username, cityId);
	}
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, Date start, Date end, boolean persistence) {
    	return this.poh.createPost(title, type, text, author, pos, cityId, data, start, end, persistence);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data, Date start, Date end, boolean persistence) {
        return this.poh.editPost(postId, title, type, text, author, cityId, data, start, end, persistence);
    }
    
    public List<Point> getPoints (String cityID) {
          return this.poh.getPoints(cityID);
      
    }
    
    public List<Post> getPosts (String pointId) {
        return this.poh.getPosts(pointId);
    }
    
    public List<Post> getPosts (List<String> postIds) {
        return this.poh.getPosts(postIds);
    }
    
    public Post getPost(String postId) {
        return this.poh.getPost(postId);
    }
    
    public boolean deletePost (String postId, String author) {
    	return this.poh.deletePost(postId, author);
    }
    
    public boolean judge(String pendingId, boolean outcome, String motivation) {
		return this.peh.judge(pendingId, outcome, motivation);
	}
	
	public List<PendingRequest> getAllRequest(String cityId){
		return this.peh.getAllRequest(cityId);
	}
	
	public PendingRequest getRequest(String requestId) {
		return this.peh.getRequest(requestId);
	}
	
	public void checkEnding() {
		this.poh.checkEndingPosts();
		this.gh.checkEndingGroups();
	}
    
}