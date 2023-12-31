package Synk.Api.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Synk.Api.Model.City.City;
import Synk.Api.Model.City.CityHandler;
import Synk.Api.Model.Group.GroupHandler;
import Synk.Api.Model.Pending.PendingHandler;
import Synk.Api.Model.Post.PointHandler;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.User.UserHandler;

public class MuniciPathMediator {
	
	private PointHandler point;
	private UserHandler user;
	private CityHandler city;
	private GroupHandler group;
	private PendingHandler pending;
	
	public void setPoint(PointHandler point) {
		this.point = point;
	}
	
	public void setUser(UserHandler user) {
		this.user = user;
	}
	
	public void setCity(CityHandler city) {
		this.city = city;
	}
	
	public void setGroup(GroupHandler group) {
		this.group = group;
	}
	
	public void setPending(PendingHandler pending) {
		this.pending = pending;
	}
	
	public boolean isAuthorizedToPost(String cityId, String author) {
		return this.city.isAuthorized(cityId, author);
	}

	public boolean canPublish(String cityId, String author) {
		return this.city.canPublish(cityId, author);
	}

	public void addPostPending(String id, String cityId) {
		this.pending.addPostRequest(id, cityId);
	}

	public void addPostPending(String postId, String title, PostType type, String text,
			List<String> data, Date start, Date end, boolean persistence, String cityId) {
		this.pending.addPostRequest(postId, title, type, text, data, start, end, persistence, cityId);
	}

	public void removeAllCityGroups(String cityId) {
		this.group.removeAllFromCity(cityId);
		
	}

	public void removeFromAllGroups(String post) {
		this.group.removeFromAll(post);
	}

	public City getCity(String cityID) {
		return this.city.getCity(cityID);
	}

	public boolean matchCurator(String curator, String id) {
		return this.user.matchCurator(curator, id);
	}

	public void createPostForNewCity(String id, String cityName, String curator, Position pos) {
        this.point.addNewCity(id);
        this.point.createPost("Comune di "+cityName, PostType.INSTITUTIONAL, "",
        		curator, pos, id, new ArrayList<>(), null, null, true);
	}

	public boolean changeCurator(String curator, String cityId) {
		return this.user.changeCurator(curator, cityId);
	}

	public void deleteCity(String cityId) {
    	this.user.discreditCurator(cityId);
    	this.point.deleteCityPoints(cityId);
	}

	public boolean usernameExists(String username) {
		return this.user.usernameExists(username);
	}
	
	public boolean postExist(String postId) {
		return this.point.getPost(postId) != null;
	}

	public List<Post> getPostsIfAllExists(List<String> postIds) {
		return this.point.getPostsIfAllExists(postIds);
	}

	public List<String> viewGroupFrom(Post post) {
		return this.group.viewGroupFrom(post.getPostId());
	}
	
	
	
	
	
	
}
