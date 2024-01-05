package Synk.Api.Controller.Group;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Group.GroupRepository;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Post;

@Service
public class GroupHandler {
	
	private MuniciPathMediator mediator;
	private int count;
	private IdentifierManager idManager = new IdentifierManager();
	
	@Autowired
	private GroupRepository groupRepository;
	
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	public void removeFromAll(String post) {
		String cityId = idManager.getCityId(post);
		groupRepository.findAll().forEach(g -> g.removePost(post));
		checkCompositionOfGroups(cityId);
	}

	private void checkCompositionOfGroups(String cityId) {
		List<Group> toDelete = getAllFromCity(cityId)
				.filter(g -> !g.isGroup()).toList();
		this.groupRepository.deleteAll(toDelete);
	}

	public void removeAllFromCity(String cityId) {
		this.groupRepository.deleteAll(getAllFromCity(cityId).toList());
	}
	
	private Stream<Group> getAllFromCity(String cityId){
		return getStreamOfAll()
				.filter(g -> g.getCityId().equals(cityId));
	}
	
	private Stream<Group> getStreamOfAll(){
		return StreamSupport.stream(groupRepository.findAll().spliterator(), false);
	}
	
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(!this.mediator.isAuthorizedToPost(cityId, author))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1 || (!checkTiming(start, end, persistence)))
			return false;
		boolean publish = this.mediator.canPublish(cityId, author);
		String id = getId(cityId);
		Group group = new Group(id, title, cityId, author,
				sorted, publish, persistence, start, end, postIds);
		if(!publish)
			this.mediator.addPending(id);
		this.groupRepository.save(group);
		return true;
	}

	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		Group group = viewGroup(groupId);
		if(!(group != null && group.getAuthor().equals(author) && checkTiming(start, end, persistence)))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1)
			return false;
		if(mediator.canPublish(idManager.getCityId(groupId), author)) {
			group.edit(title, sorted, postIds, start, end, persistence);
			groupRepository.save(group);
		}
		else mediator.addGroupPending(groupId, title, sorted, postIds, start, end, persistence);
		return true;
	}
	
	public boolean editGroup(String groupId, String title, boolean sorted, List<String> postIds,
			LocalDateTime start, LocalDateTime end, boolean persistence) {
		Group group = viewGroup(groupId);
		if(!(group != null && checkTiming(start, end, persistence)))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(postIds);
		if(posts == null || posts.size() < 1)
			return false;
		group.edit(title, sorted, postIds, start, end, persistence);
		groupRepository.save(group);
		return true;
	}
	
	public boolean editGroup(PendingRequest request) {
		List<String> list = new ArrayList<>();
		list.addAll(request.getData());
		return editGroup(request.getId(), request.getTitle(), request.isSorted(),
				list, request.getStartTime(), request.getEndTime(), request.isPersistence());
	}

	public boolean removeGroup(String author, String groupId) {
		Group group = viewGroup(groupId);
		if(group == null || (!group.getAuthor().equals(author)))
			return false;
		this.groupRepository.delete(group);
		return true;
	}
	
	public boolean removeGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null)
			return false;
		this.groupRepository.delete(group);
		return true;
	}

	
	private String getId(String cityId) {
		this.count = 0;
		getAllFromCity(cityId).forEach(g -> {
			int v = Integer.parseInt(idManager.getContentId(g.getId()));
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
		return getStreamOfAll()
				.filter(g -> groupIds.contains(g.getId()))
				.toList();
	}
	
	public Group viewGroup(String groupId) {
		return getStreamOfAll()
				.filter(g -> g.getId().equals(groupId))
				.findFirst().orElse(null);
	}
	
	public List<String> viewGroupFrom(String postId, String username) {
		return getStreamOfAll().filter(g -> g.getPosts().contains(postId))
				.filter(g -> toShow(g, username))
				.map(g -> g.getId()).toList();
	}
	
	private boolean toShow(Group group, String username) {
    	if(group.getAuthor().equals(username))
    		return true;
    	if(!group.isPublished())
    		return false;
    	return group.getEndTime() == null || group.getEndTime().isAfter(LocalDateTime.now());
    }
	
	public boolean approveGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null || group.isPublished())
			return false;
		group.setPublished(true);
		this.groupRepository.save(group);
		return true;
	}
	
	public void checkEndingGroups() {
		LocalDateTime date = LocalDateTime.now();
		getStreamOfAll()
			.filter(g -> ! g.isPersistence()).forEach(g -> {
				if(g.getEndTime().isBefore(date))
					groupRepository.delete(g);
			});
	}

	public String getAuthor(String pendingId) {
		return this.viewGroup(pendingId).getAuthor();
	}
	
}