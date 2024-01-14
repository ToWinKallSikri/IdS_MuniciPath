package Synk.Api.Controller.User.Follow;

import java.util.List;

import Synk.Api.Model.MetaData;

public class FollowHandler {

	public boolean followContributor(String username, String contributor) {
		return false;
	}
	
	public boolean unfollowContributor(String username, String contributor) {
		return false;
	}
	
	public boolean followCity(String username, String cityId) {
		return false;
	}
	
	public boolean unfollowCity(String username, String cityId) {
		return false;
	}
	
	public boolean follow(String username, MetaData data) {
		return false;
	}
	
	public boolean unfollow(String username, MetaData data) {
		return false;
	}
	
	public boolean alreadyFollowing(String username, MetaData data) {
		return false;
	}
	
	public boolean alreadyFollowingCity(String username, String cityId) {
		return false;
	}
	
	public boolean alreadyFollowingContributor(String username, String contributor) {
		return false;
	}
	
	public List<String> getAllFollowed(String username){
		return null;
	}
	
}
