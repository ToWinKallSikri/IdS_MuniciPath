package Synk.Api.Model.Group;

import java.util.Date;
import java.util.List;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Post;

public class GroupHandler {
	
	private MuniciPathMediator mediator;
	//TODO
	
	public GroupHandler(MuniciPathMediator mediator) {
		this.mediator = mediator;
		//TODO
	}

	public void removeFromAll(Post post) {
		// TODO Auto-generated method stub
		
	}

	public void removeAllFromCity(String cityId) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean createGroup(String title, String author, boolean sorted, String cityId, List<String> posts, Date start, Date end, boolean persistence) {
		return false;
		//TODO
	}
	
	public boolean updateGroup(String groupId, String title, String author, boolean sorted, String cityId, List<String> posts, Date start, Date end, boolean persistence) {
		return false;
		//TODO
	}
	
	public boolean updateGroup(PendingRequest request) {
		return false;
		//TODO
	}
	
	public Group viewGroup(String groupId) {
		return null;
		//TODO
	}
	
	public Group viewGroupFrom(String postId) {
		return null;
		//TODO
	}
	
	public boolean approveGroup(String groupId) {
		return false;
		//TODO
	}
	
	public void checkEndingGroups() {
		//TODO
	}
	
}
