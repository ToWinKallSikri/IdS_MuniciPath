package Synk.Api.Model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.City.City;
import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.City.Licence;
import Synk.Api.Model.City.Role;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Pending.PendingHandler;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Contribute;
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
        this.poh = new PointHandler();
        this.poh.setMediator(mediator);
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
	
	public List<Licence> getAuthorizations(String cityId) {
		return this.ch.getAuthorizations(cityId);
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
	
	public boolean addModerator(String username, String cityId) {
		return this.ch.addModerator(username, cityId);
	}
	
	public boolean removeModerator(String username, String cityId) {
		return this.ch.removeModerator(username, cityId);
	}
	
	
	
	
	
	
	
	
	
	
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		return this.gh.createGroup(title, author, sorted, cityId, postIds, start, end, persistence);
	}

	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		return this.gh.editGroup(groupId, title, author, sorted, postIds, start, end, persistence);
	}
	
	public boolean removeGroup(String author, String groupId) {
		return this.gh.removeGroup(author, groupId);
	}
	
	public boolean removeGroup(String groupId) {
		return this.gh.removeGroup(groupId);
	}
	
	public Group viewGroup(String groupId) {
		return this.gh.viewGroup(groupId);
	}
	
	public List<Group> viewGroups(List<String> groupIds) {
		return this.gh.viewGroups(groupIds);
	}
	
	
	
	
	
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	return this.poh.createPost(title, type, text, author, pos, cityId, data, start, end, persistence);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
        return this.poh.editPost(postId, title, type, text, author, cityId, data, start, end, persistence);
    }
    
    public List<Point> getPoints (String cityID, String username) {
          return this.poh.getPoints(cityID, username);
      
    }
    
    public List<Post> viewPosts (String pointId, String username) {
        return this.poh.viewPosts(pointId, username);
    }
    
    public List<Post> viewPosts (List<String> postIds) {
        return this.poh.getPosts(postIds);
    }
    
    public Post viewPost(String postId) {
        return this.poh.getPost(postId);
    }
    
    public boolean deletePost (String postId, String author) {
    	return this.poh.deletePost(postId, author);
    }
    
    public boolean deletePost (String postId) {
    	return this.poh.deletePost(postId);
    }
    
    public List<Contribute> getContributes(String username, String postId){
		return this.poh.getContributes(username, postId);
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
