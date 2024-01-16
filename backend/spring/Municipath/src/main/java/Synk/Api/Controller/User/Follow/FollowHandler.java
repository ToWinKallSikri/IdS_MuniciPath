package Synk.Api.Controller.User.Follow;

import java.util.ArrayList;
import java.util.List;

import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.User.Follow.Follow;

public class FollowHandler {
	
	private List<Follow> follows;
	private MuniciPathMediator mediator;
	
	public FollowHandler() {
		this.follows = new ArrayList<>();
	}
	
	/**
	 * imposta il mediator
	 * @param mediator mediatore da inserire
	 */
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
	
	public boolean followContributor(String username, String contributor) {
		if(username.equals(contributor) || this.alreadyFollowingContributor(username, contributor))
			return false;
		return true;
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
		if(data.isOfCity())
			return this.followCity(username, data.getCityId());
		return this.followContributor(username, data.getAuthor());
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
