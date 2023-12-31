package Synk.Api.Model.Group;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Post;

public class GroupHandler {
	
	private MuniciPathMediator mediator;
	private Map<String, List<Group>> groups;
	private int count;
	
	public GroupHandler(MuniciPathMediator mediator) {
		this.mediator = mediator;
		this.groups = new HashMap<String, List<Group>>();
	}
	
	public void loadGroups(List<Group> groups) {
		this.groups = groups.stream()
				.collect(Collectors.groupingBy(Group::getCityId));
	}

	public void removeFromAll(String post) {
		String cityId = this.getCityId(post);
		groups.get(cityId).forEach(g -> g.removePost(post));
		checkCompositionOfGroups(groups.get(cityId));
	}

	private void checkCompositionOfGroups(List<Group> list) {
		ArrayList<Group> toDelete = new ArrayList<>();
		for(Group group : list) {
			if(!group.isGroup()) {
				toDelete.add(group);
			}
		}
		list.removeAll(toDelete);
	}

	public void removeAllFromCity(String cityId) {
		this.groups.remove(cityId);
	}
	
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(!this.mediator.isAuthorizedToPost(cityId, author))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1 || !checkTiming(start, end, persistence))
			return false;
		boolean publish = this.mediator.canPublish(cityId, author);
		String id = getId(cityId);
		Group group = new Group(id, title, author, cityId,
				sorted, publish, persistence, start, end, postIds);
		this.groups.get(cityId).add(group);
		return true;
	}

	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		Group group = viewGroup(groupId);
		if(group == null || !(group.getAuthor().equals(author) || checkTiming(start, end, persistence)))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1)
			return false;
		group.edit(title, sorted, postIds, start, end, persistence);
		return true;
	}
	

	
	public boolean editGroup(PendingRequest request) {
		Group group = viewGroup(request.getId());
		return editGroup(request.getId(), request.getTitle(), group.getAuthor(), request.isSorted(), 
				request.getData(), request.getStartTime(), request.getEndTime(), request.isPersistence());
	}
	
	public boolean removeGroup(String author, String groupId) {
		Group group = viewGroup(groupId);
		if(group == null)
			return false;
		if(!group.getAuthor().equals(author))
			return false;
		this.groups.get(group.getCityId()).remove(group);
		return true;
	}
	
	public boolean removeGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null)
			return false;
		this.groups.get(group.getCityId()).remove(group);
		return true;
	}

	
	private String getId(String cityId) {
		this.count = 0;
		this.groups.get(cityId).forEach(g -> {
			int v = Integer.parseInt(g.getId().split(".")[2]);
		    this.count = count > v ? count : v + 1;
		});
		return cityId+".g."+ this.count;
	}
	
	private boolean checkTiming(LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(persistence && start == null && end == null)
			return true;
		if(start != null && end != null && start.isBefore(end))
			return true;
		return false;
	}
	
	public List<Group> viewGroups(List<String> groupIds) {
		String cityId = getCityId(groupIds.get(0));
		return this.groups.get(cityId).stream()
				.filter(g -> groupIds.contains(g.getId()))
				.toList();
	}
	
	public Group viewGroup(String groupId) {
		String cityId = getCityId(groupId);
		return this.groups.get(cityId).stream()
				.filter(g -> g.getId().equals(groupId))
				.findFirst().orElse(null);
	}
	
	public List<String> viewGroupFrom(String postId) {
		String cityId = getCityId(postId);
		return this.groups.get(cityId).stream().filter(Group::isPublished)
				.filter(g -> g.getPosts().contains(postId))
				.map(g -> g.getId()).toList();
	}
	
	private String getCityId(String id) {
		return id.split(".")[0];
	}
	
	public boolean approveGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null || group.isPublished())
			return false;
		group.setPublished(true);
		return true;
	}
	
	public void checkEndingGroups() {
		LocalDateTime date = LocalDateTime.now();
		this.groups.values().forEach(l -> l.stream()
				.filter(g -> ! g.isPersistence()).forEach(g -> {
			if(g.getEndTime().isBefore(date))
				l.remove(g);
		}));
	}
	
}
