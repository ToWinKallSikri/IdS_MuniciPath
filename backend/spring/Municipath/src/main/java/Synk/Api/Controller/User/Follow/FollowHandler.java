package Synk.Api.Controller.User.Follow;

import java.util.ArrayList;
import java.util.List;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.User.Follow.Follow;

public class FollowHandler {
	
	private List<Follow> follows;
	private IdentifierManager idManager;
	
	public FollowHandler() {
		this.follows = new ArrayList<>();
		this.idManager = new IdentifierManager();
	}
	
	public boolean followContributor(String username, String contributor) {
		if(username.equals(contributor) || this.alreadyFollowingContributor(username, contributor))
			return false;
		String id = username + "u" + contributor;
		Follow follow = new Follow(id, contributor, username);
		this.follows.add(follow);
		return true;
	}
	
	public boolean unfollowContributor(String username, String contributor) {
		String id = username + "u" + contributor;
		Follow follow = getFollow(id);
		if(follow == null)
			return false;
		this.follows.remove(follow);
		return true;
	}
	
	public boolean followCity(String username, String cityId) {
		if(this.alreadyFollowingCity(username, cityId))
			return false;
		String id = username + "c" + cityId;
		Follow follow = new Follow(id, cityId, username);
		this.follows.add(follow);
		return true;
	}
	
	public boolean unfollowCity(String username, String cityId) {
		String id = username + "c" + cityId;
		Follow follow = getFollow(id);
		if(follow == null)
			return false;
		this.follows.remove(follow);
		return true;
	}
	
	private Follow getFollow(String id) {
		return this.follows.stream()
				.filter(f -> f.getId().equals(id))
				.findFirst().orElse(null);
	}
	
	public boolean follow(String username, MetaData data) {
		if(data.isOfCity())
			return this.followCity(username, data.getCityId());
		return this.followContributor(username, data.getAuthor());
	}
	
	public boolean unfollow(String username, MetaData data) {
		if(data.isOfCity())
			return this.unfollowCity(username, data.getCityId());
		return this.unfollowContributor(username, data.getAuthor());
	}
	
	public boolean alreadyFollowing(String username, MetaData data) {
		if(data.isOfCity())
			return this.alreadyFollowingCity(username, data.getCityId());
		return this.alreadyFollowingContributor(username, data.getAuthor());
	}
	
	public boolean alreadyFollowingCity(String username, String cityId) {
		String id = username + "c" + cityId;
		return this.follows.stream().anyMatch(f -> f.getId().equals(id));
	}
	
	public boolean alreadyFollowingContributor(String username, String contributor) {
		String id = username + "u" + contributor;
		return this.follows.stream().anyMatch(f -> f.getId().equals(id));
	}
	
	public List<String> getAllFollowed(String username){
		return this.follows.stream()
				.filter(f -> f.getUsername().equals(username))
				.map(f -> f.getFollowed()).toList();
	}

	public List<String> getAllCityFollowers(String cityId) {
		return this.follows.stream().filter(f -> f.getFollowed().equals(cityId))
				.filter(f -> this.idManager.isCityFollowing(f.getId()))
				.map(f -> f.getUsername()).toList();
	}

	public List<String> getAllContributorFollowers(String author) {
		return this.follows.stream().filter(f -> f.getFollowed().equals(author))
				.filter(f -> !this.idManager.isCityFollowing(f.getId()))
				.map(f -> f.getUsername()).toList();
	}
	
}
