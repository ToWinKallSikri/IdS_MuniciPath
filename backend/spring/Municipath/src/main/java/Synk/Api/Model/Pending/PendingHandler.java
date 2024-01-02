package Synk.Api.Model.Pending;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.Post.PostType;
import jakarta.annotation.PostConstruct;

@Repository
public class PendingHandler {
	
	private List<PendingRequest> requests;
	private MuniciPathMediator mediator;
	@Autowired
	private PendingRepository pendingRepository;

	public PendingHandler() {
		this.requests = new ArrayList<>();
	}
	
	@PostConstruct
	public void init() {
		List<String> data = new ArrayList<>();
		data.add("miao");
		data.add("bau");
		this.pendingRepository.save(new PendingRequest("123.456.789", "la vita è il nemico", 
				"enzomma ciai capito daje", true, PostType.SOCIAL, data, null, null));
	}

	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	
	public void addRequest(String id) {
		this.requests.add(new PendingRequest(id));
	}

	
	public void addPostRequest(String postId, String title, PostType type, String text,
			List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
		requests.add(new PendingRequest(postId, title, text, persistence, type, data, start, end));
	}

	public void addGroupRequest(String groupId, String title, boolean sorted, List<String> postIds, 
			LocalDateTime start, LocalDateTime end, boolean persistence) {
		requests.add(new PendingRequest(groupId, title, sorted, persistence, postIds, start, end));
	}
	
	public boolean judge(String pendingId, boolean outcome, String motivation) {
		PendingRequest request = getRequest(pendingId);
		if(request == null)
			return false;
		if(outcome) {
			if(pendingId.split(".")[1].equals("g")) 
				mediator.manageGroupRequest(request);
			else mediator.managePostRequest(request);
		}
		String username = this.mediator.getAuthor(pendingId);
		String response = "Pending " + (outcome ? "accettato" : "rifiutato") + ".\n";
		this.mediator.send(username, response+outcome);
		this.requests.remove(request);
		return true;
	}


	public List<PendingRequest> getAllRequest(String cityId){
		return requests.stream().filter(p -> p.getId().split(".")[0].equals(cityId)).toList();
	}
	
	public PendingRequest getRequest(String requestId) {
		return requests.stream().filter(p -> p.getId().equals(requestId)).findFirst().orElse(null);
	}


	
}
