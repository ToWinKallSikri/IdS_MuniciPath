package Synk.Api.Model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.City.City;
import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.City.Role.Licence;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Pending.PendingHandler;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.User.User;
import Synk.Api.Model.User.UserHandler;

public class MuniciPathModel {
	
    private PointHandler poh;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    private PendingHandler peh;
    
    public MuniciPathModel(){
    	MuniciPathMediator mediator = new MuniciPathMediator();
    	this.uh = new UserHandler();
		this.uh.setMediator(mediator);
        this.ch = new CityHandler();
        this.ch.setMediator(mediator);
        this.gh = new GroupHandler();
        this.gh.setMediator(mediator);
        this.poh = new PointHandler();
        this.poh.setMediator(mediator);
        this.peh = new PendingHandler();
        this.peh.setMediator(mediator);
        mediator.setUser(uh);
        mediator.setCity(ch);
        mediator.setGroup(gh);
        mediator.setPoint(poh);
        mediator.setPending(peh);
    }
    
    //manager
    public boolean createCity(String cityName, int cap, String curator, Position pos ) {
    	if(cityName == null || curator == null || pos == null)
    		return false;
        return ch.createCity(cityName, cap, curator, pos);
    }
    
    //manager
    public boolean updateCity(String id, String cityName, int cap, String curator, Position pos ){
    	if(id == null || cityName == null || curator == null || pos == null)
    		return false;
		return ch.updateCity(id, cityName, cap, curator, pos);
    }
    
    //manager
    public boolean deleteCity(String cityId) {
    	if(cityId == null)
    		return false;
    	return ch.deleteCity(cityId);
    }
    
    public List<City> searchCity(String search){
    	if(search == null)
    		search = "";
    	return ch.getCities(search);
    }
    
    public List<City> getAllCity(){
    	return ch.getCities();
    }
	
    //staff
	public boolean setRole(String username, String cityId, Role role) {
		if(username == null || cityId == null || role == null)
			return false;
		return this.ch.setRole(username, cityId, role);
	}
	
	//metodo di servizio
	private Role getRole(String username, String cityId) {
		return this.ch.getRole(username, cityId);
	}
	
	//curator
	public boolean addModerator(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.ch.addModerator(username, cityId);
	}
	
	//curator
	public boolean removeModerator(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.ch.removeModerator(username, cityId);
	}
	
	public boolean addRequest(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.ch.addRequest(username, cityId);
	}
	
	//staff
	public List<RoleRequest> getRequests(String cityId){
		if(cityId == null)
			return null;
		return this.ch.getRequests(cityId);
	}
	
	//staff
	public boolean judge(String requestId, boolean outcome) {
		if(requestId == null)
			return false;
		return this.ch.judge(requestId, outcome);
	}
	
	
	
	
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(title == null || author == null || cityId == null || postIds == null)
			return false;
		return this.gh.createGroup(title, author, sorted, cityId, postIds, start, end, persistence);
	}

	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(title == null || author == null || postIds == null)
			return false;
		return this.gh.editGroup(groupId, title, author, sorted, postIds, start, end, persistence);
	}
	
	//staff
	public boolean editGroup(String groupId, String title, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(title == null || postIds == null)
			return false;
		return this.gh.editGroup(groupId, title, sorted, postIds, start, end, persistence);
	}
	
	public boolean removeGroup(String author, String groupId) {
		if(author == null || groupId == null)
			return false;
		return this.gh.removeGroup(author, groupId);
	}
	
	public boolean removeGroup(String groupId) {
		if(groupId == null)
			return false;
		return this.gh.removeGroup(groupId);
	}
	
	public Group viewGroup(String groupId) {
		if(groupId == null)
			return null;
		return this.gh.viewGroup(groupId);
	}
	
	public List<Group> viewGroups(List<String> groupIds) {
		if(groupIds == null)
			return null;
		return this.gh.viewGroups(groupIds);
	}
	
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(title == null || type == null || text == null || author == null 
    			|| pos == null || cityId == null || data == null)
    		return false;
    	return this.poh.createPost(title, type, text, author, pos, cityId, data, start, end, persistence);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(postId == null || title == null || type == null || text == null 
    			|| author == null ||  cityId == null || data == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, author, cityId, data, start, end, persistence);
    }
    
    //staff
    public boolean editPost(String postId, String title, PostType type, String text,
    		ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(postId == null || title == null || type == null || text == null || data == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, data, start, end, persistence);
    }
    
    public List<Point> getPoints (String cityId, String username) {
    	if(cityId == null || username == null)
    		return null;
        return this.poh.getPoints(cityId, username);
      
    }
    
    public List<Post> viewPosts (String pointId, String username) {
    	if(pointId == null || username == null)
    		return null;
        return this.poh.viewPosts(pointId, username);
    }
    
    public List<Post> viewPosts (List<String> postIds) {
    	if(postIds == null)
    		return null;
        return this.poh.getPosts(postIds);
    }
    
    public Post viewPost(String postId, String username) {
    	if(postId == null || username == null)
    		return null;
        return this.poh.getPost(postId, username);
    }
    
    public boolean deletePost (String postId, String author) {
    	if(postId == null || author == null)
    		return false;
    	return this.poh.deletePost(postId, author);
    }
    
    //staff
    public boolean deletePost (String postId) {
    	if(postId == null)
    		return false;
    	return this.poh.deletePost(postId);
    }
    
    
    public List<Contribute> getContributes(String username, String postId){
    	if(postId == null || username == null)
    		return null;
		return this.poh.getContributes(username, postId);
	}
    
    
    public boolean addContentToContest(String contestAuthor, String contestId, List<String> content) {
    	if(contestAuthor == null || contestId == null || content == null)
    		return false;
		return this.poh.addContentToContest(contestAuthor, contestId, content);
	}
	
	
	public boolean declareWinner(String author, String contestId, String winnerId) {
		if(author == null || contestId == null || winnerId == null)
			return false;
		return this.poh.declareWinner(author, contestId, winnerId);
	}
    
    
    public boolean judge(String pendingId, boolean outcome, String motivation) {
    	if(pendingId == null || motivation == null)
    		return false;
		return this.peh.judge(pendingId, outcome, motivation);
	}
	
    //staff
	public List<PendingRequest> getAllRequest(String cityId){
		if(cityId == null)
			return null;
		return this.peh.getAllRequest(cityId);
	}
	
	public PendingRequest getRequest(String requestId) {
		if(requestId == null)
			return null;
		return this.peh.getRequest(requestId);
	}
	
	
	
	public boolean addUser(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.addUser(username, password);
	}
	
	//manager
	public boolean removeUser(String username) {
		if(username == null)
			return false;
		return this.uh.removeUser(username);
	}
	
	public boolean changePassowrd(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.changePassowrd(username, password);
	}
	
	//manager
	public boolean userValidation(String username) {
		if(username == null)
			return false;
		return this.uh.userValidation(username);
	}
	
	//manager
	public boolean manageManager(String username, boolean auth) {
		if(username == null)
			return false;
		return this.uh.manageManager(username, auth);
	}
    
	//metodo di servizio
    private User getUser(String username) {
    	return this.uh.getUser(username);
    }
    
    //manager
    public List<User> getUsersNotConvalidated(){
    	return this.uh.getNotConvalidatedUsers();
    }
	
	//metodo di servizio parallelo
	public void checkEnding() {
		this.poh.checkEndingPosts();
		this.gh.checkEndingGroups();
	}
    
}
