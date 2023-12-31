package Synk.Api.Model.Pending;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Post.PostType;

public class PendingHandler {
	
	private MuniciPathMediator mediator;

	private List<PendingRequest> pr;

	public PendingHandler(MuniciPathMediator mediator) {
		this.mediator = mediator;
	}

	public void addPostRequest(String id, String cityId) {
		//mediator check??
		pr.add(new PendingRequest(this.mediator.getPost(id).getPostId(), this.mediator.getPost(id).getTitle(),
				this.mediator.getPost(id).getText(), this.mediator.getPost(id).isPublished(),
				this.mediator.getPost(id).isPersistence(), this.mediator.getPost(id).getType(),
				this.mediator.getPost(id).getMultimediaData(), this.mediator.getPost(id).getStart(),
				this.mediator.getPost(id).getEnd()));
		
	}

	public void addPostRequest(String postId, String title, PostType type, String text,
			ArrayList<String> data, Date start, Date end, boolean persistence, String cityId) {
		// TODO Auto-generated method stub
		
	}
	
	public void addGroupRequest(String id, String cityId) {
		//TODO
	}
	
	public void addGroupRequest(String groupId, String title, boolean sorted, List<String> postIds, 
			Date start, Date end, boolean persistence, String cityId) {
		//TODO
	}
	
	public boolean judge(String pendingId, boolean outcome, String motivation) {
		return false;
		//TODO
	}
	
	public List<PendingRequest> getAllRequest(String cityId){
		return null;
		//TODO
	}
	
	public PendingRequest getRequest(String requestId) {
		return null;
		//TODO
	}
	
	private boolean checkPendingId(String pendingId) {
		return false;
		//TODO
	}
	
}
