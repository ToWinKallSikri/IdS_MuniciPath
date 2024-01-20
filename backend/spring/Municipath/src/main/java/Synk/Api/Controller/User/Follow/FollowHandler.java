package Synk.Api.Controller.User.Follow;

import java.util.List;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.User.Follow.Follow;
import Synk.Api.Model.User.Follow.FollowRepository;

@Service
public class FollowHandler {
	
	private FollowRepository followRepository;
	private IdentifierManager idManager;
	
	public FollowHandler(FollowRepository followRepository) {
		this.followRepository = followRepository;
		this.idManager = new IdentifierManager();
	}
	
	public boolean followContributor(String username, String contributor) {
		if(username.equals(contributor) || this.alreadyFollowingContributor(username, contributor))
			return false;
		String id = username + ".u." + contributor;
		Follow follow = new Follow(id, contributor, username);
		this.followRepository.save(follow);
		return true;
	}
	
	public boolean unfollowContributor(String username, String contributor) {
		String id = username + ".u." + contributor;
		Follow follow = getFollow(id);
		if(follow == null)
			return false;
		this.followRepository.delete(follow);
		return true;
	}
	
	public boolean followCity(String username, String cityId) {
		if(this.alreadyFollowingCity(username, cityId))
			return false;
		String id = username + ".c." + cityId;
		Follow follow = new Follow(id, cityId, username);
		this.followRepository.save(follow);
		return true;
	}
	
	public boolean unfollowCity(String username, String cityId) {
		String id = username + ".c." + cityId;
		Follow follow = getFollow(id);
		if(follow == null)
			return false;
		this.followRepository.delete(follow);
		return true;
	}
	
	private Follow getFollow(String id) {
		return this.followRepository.findById(id).orElse(null);
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
		String id = username + ".c." + cityId;
		return this.followRepository.existsById(id);
	}
	
	public boolean alreadyFollowingContributor(String username, String contributor) {
		String id = username + ".u." + contributor;
		return this.followRepository.existsById(id);
	}
	
	public List<String> getAllFollowed(String username){
		return this.followRepository.findByUsername(username)
				.stream().map(Follow::getUsername).toList();
	}

	public List<String> getAllCityFollowers(String cityId) {
		return this.followRepository.findByFollowed(cityId).stream()
				.filter(f -> this.idManager.isCityFollowing(f.getId()))
				.map(f -> f.getUsername()).toList();
	}

	public List<String> getAllContributorFollowers(String author) {
		return this.followRepository.findByFollowed(author).stream()
				.filter(f -> !this.idManager.isCityFollowing(f.getId()))
				.map(f -> f.getUsername()).toList();
	}

	public void deleteUser(String username) {
		List<Follow> list = this.followRepository.findByUsername(username);
		this.followRepository.deleteAll(list);
	}
	
}
