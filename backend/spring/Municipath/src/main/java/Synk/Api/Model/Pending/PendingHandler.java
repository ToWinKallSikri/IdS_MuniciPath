package Synk.Api.Model.Pending;

import java.time.LocalDateTime;
import java.util.List;

import Synk.Api.Model.Group.Group;
import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;

public class PendingHandler {
	
	private List<PendingRequest> pr;
	private MuniciPathMediator mediator;

	public PendingHandler(MuniciPathMediator mediator) {
		this.mediator = mediator;
	}

	
	public void addPostRequest(String id, String cityId) {
		Post p1 = this.mediator.getPost(id);
		if(!this.mediator.postExist(id)) {
			return;
		}
		if (!this.mediator.checkCityId(cityId)){
			return;
		}
		pr.add(new PendingRequest(id, p1.getTitle(), p1.getText(), true, p1.isPersistence(),
				p1.getType(), p1.getMultimediaData(), p1.getStartTime(), p1.getEndTime()));
	}

	
	public void addPostRequest(String postId, String title, PostType type, String text,
			List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence, String cityId) {
		if(!this.mediator.postExist(postId)) {
			return;
		}
		if (!this.mediator.checkCityId(cityId)){
			return;
		}
		pr.add(new PendingRequest(postId, title, text,false, persistence, type, data, start, end));
	}
	
	public void addGroupRequest(String id, String cityId) {
		Group g1 = this.mediator.getGroup(id);
		if (g1 == null || !this.mediator.checkCityId(cityId)) {
			return;
		} else {
			/*
			pr.add(new PendingRequest(id, g1.getTitle(), true, g1.isSorted(), g1.isPersistence(),
					g1.getPosts(), g1.getStart(), g1.getEnd()));
					*/
		}
	}

	public void addGroupRequest(String groupId, String title, boolean sorted, List<String> postIds, 
			LocalDateTime start, LocalDateTime end, boolean persistence, String cityId) {
		Group g1 = this.mediator.getGroup(groupId);
		if (g1 == null || !this.mediator.checkCityId(cityId)) {
			return;
		} else {
			pr.add(new PendingRequest(groupId, title, false, sorted, persistence, postIds, start, end));
		}
	}
	
	public boolean judge(String pendingId, boolean outcome, String motivation) {
		return false;
		//TODO
		// NON HO TEMPO LOL SCUSA
	}
	
	public List<PendingRequest> getAllRequest(String cityId){
		return pr.stream().filter(p -> p.getId().split(".")[0].equals(cityId)).toList();
	}
	
	public PendingRequest getRequest(String requestId) {
		return pr.stream().filter(p -> p.getId().equals(requestId)).findFirst().orElse(null);
	}
	
	private boolean checkPendingId(String pendingId) {
		pr.stream().anyMatch(p -> p.getId().equals(pendingId));
		return true;
	}

	public void removePendingRequest(String pendingId) {
		if (!checkPendingId(pendingId)) {
			return;
		}
		pr.stream().forEach(p -> {
			if (p.getId().equals(pendingId)) {
				pr.remove(p);
			}
		});
	}


	
}
