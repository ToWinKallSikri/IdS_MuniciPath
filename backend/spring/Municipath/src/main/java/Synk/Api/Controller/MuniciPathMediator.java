package Synk.Api.Controller;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;

public class MuniciPathMediator {
	
	private PointHandler point;
	private UserHandler user;
	private CityHandler city;
	private GroupHandler group;
	private PendingHandler pending;
	private IdentifierManager idManager = new IdentifierManager();
	
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

	public void addPending(String id) {
		this.pending.addRequest(id);
	}

	public void addPostPending(String postId, String title, PostType type, String text,
			List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
		this.pending.addPostRequest(postId, title, type, text, data, start, end, persistence);
	}
	
	public void addGroupPending(String groupId, String title, boolean sorted, List<String> postIds, 
			LocalDateTime start, LocalDateTime end, boolean persistence) {
		this.pending.addGroupRequest(groupId, title, sorted, postIds, start, end, persistence);
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
	
	
	public List<String> viewGroupFrom(Post post, String username) {
		return this.group.viewGroupFrom(post.getPostId(), username);
	}

	public boolean checkCityId(String cityId) {
		return this.city.getCity(cityId) != null;
	}

	public Group getGroup(String groupId) {
		return this.group.viewGroup(groupId);
	}

	public Post getPost(String id) {
		return this.point.getPost(id);
	}

	public void addPost(Post post) {
		this.point.createPost(post.getTitle(), post.getType(), post.getText(), post.getAuthor(),
				post.getPos(), post.getCityID(), (ArrayList<String>) post.getMultimediaData(), post.getStartTime(),
				post.getEndTime(), post.isPersistence());
	}

	public void addGroup(Group group) {
		this.group.createGroup(group.getTitle(), group.getAuthor(), group.isSorted(), group.getCityId(),
				group.getPosts(), group.getStartTime(), group.getEndTime(), group.isPersistence());
	}

	public String getAuthor(String pendingId) {
		return idManager.isGroup(pendingId) ? group.getAuthor(pendingId) : point.getAuthor(pendingId);
	}

	public void send(String username, String message) {
		this.user.send(username, message);
	}

	public void manageGroupRequest(PendingRequest request) {
		if(request.isNew())
			this.group.approveGroup(request.getId());
		else this.group.editGroup(request);
	}

	public void managePostRequest(PendingRequest request) {
		if(request.isNew())
			this.point.approvePost(request.getId());
		else this.point.editPost(request);
	}

	public void deletePendingGroup(String pendingId) {
		this.group.removeGroup(pendingId);
		
	}

	public void deletePendingPost(String pendingId) {
		this.point.deletePost(pendingId);
	}
}
