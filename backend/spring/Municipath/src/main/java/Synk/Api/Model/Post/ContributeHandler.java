package Synk.Api.Model.Post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContributeHandler {
	
	private Map<String, List<Contribute>> contributes;
	
	public ContributeHandler() {
		this.contributes = new HashMap<>();
	}
	
	public void loadContributes(List<Contribute> contributes) {
		this.contributes = contributes.stream()
			.collect(Collectors.groupingBy(c -> c.getContestId()));
	}

	public boolean addContestToContest(String author, String contestId, List<String> content) {
		if(!this.contributes.containsKey(contestId))
			return false;
		if(!canPartecipate(author, contestId))
			return false;
		this.contributes.get(contestId).add(new Contribute(author, contestId, content));
		return true;
	}
	
	public List<Contribute> getContributes(String contestId){
		return this.contributes.getOrDefault(contestId, null);
	}

	public List<String> declareWinner(String contestId, String winnerId) {
		if(!this.contributes.containsKey(contestId))
			return null;
		List<String> content = this.contributes.get(contestId).stream()
				.filter(c -> c.getAuthor().equals(winnerId))
				.map(c -> c.getContribute()).findFirst().orElse(null);
		removeContest(contestId);
		return content;
	}
	
	public boolean addContest(String contestId) {
		if(this.contributes.containsKey(contestId))
			return false;
		this.contributes.put(contestId, new ArrayList<>());
		return true;
	}
	
	public boolean removeContest(String contestId) {
		if(!this.contributes.containsKey(contestId))
			return false;
		this.contributes.remove(contestId);
		return true;
	}
	
	private boolean canPartecipate(String username, String contestId) {
		return this.contributes.get(contestId).stream()
				.noneMatch(c -> c.getAuthor().equals(username));
	}
	

}
