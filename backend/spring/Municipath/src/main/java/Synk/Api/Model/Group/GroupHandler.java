package Synk.Api.Model.Group;

import java.util.ArrayList;
import java.util.Date;
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

	public void removeFromAll(Post post) {
		String cityId = post.getCityID();
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
			List<String> postIds, Date start, Date end, boolean persistence) {
		if(!this.mediator.isAuthorizedToPost(cityId, author))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1 || !checkTiming(start, end, persistence))
			return false;
		boolean publish = this.mediator.canPublish(cityId, author);
		String id = getId(cityId);
		Group group = new Group(id, title, author, cityId,
				sorted, publish, persistence, start, end, posts);
		this.groups.get(cityId).add(group);
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

	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, Date start, Date end, boolean persistence) {
		Group group = viewGroup(groupId);
		if(group == null || !(group.getAuthor().equals(author) || checkTiming(start, end, persistence)))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1)
			return false;
		group.edit(title, sorted, posts, start, end, persistence);
		return true;
	}
	
	private boolean checkTiming(Date start, Date end, boolean persistence) {
		if(persistence && start == null && end == null)
			return true;
		if(start != null && end != null && start.before(end))
			return true;
		return false;
	}
	
	public boolean editGroup(PendingRequest request) {
		Group group = viewGroup(request.getId());
		return editGroup(request.getId(), request.getTitle(), group.getAuthor(), request.isSorted(), 
				request.getData(), request.getStart(), request.getEnd(), request.isPersistence());
	}
	
	public Group viewGroup(String groupId) {
		String cityId = getCityId(groupId);
		return this.groups.get(cityId).stream()
				.filter(g -> g.getId().equals(groupId))
				.findFirst().orElse(null);
	}
	
	public List<Group> viewGroupFrom(String postId) {
		String cityId = getCityId(postId);
		Post post = new Post();
		post.setPostId(postId);
		return this.groups.get(cityId).stream().filter(Group::isPublished)
				.filter(g -> g.getPosts().contains(post)).toList();
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
		Date date = new Date();
		this.groups.values().forEach(l -> l.stream()
				.filter(g -> ! g.isPersistence()).forEach(g -> {
			if(g.getEnd().before(date))
				l.remove(g);
		}));
	}
	
}
