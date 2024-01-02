package Synk.Api.Model.Post.Contribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class ContributeHandler {
	
	private Map<String, List<Contribute>> contributes;
	
	private ContributeRepository contributesRepository;
	
	public ContributeHandler(ContributeRepository contributesRepository) {
		this.contributesRepository = contributesRepository;
		this.contributes = new HashMap<>();
	}
	
	@PostConstruct
	public void loadContributes() {
		this.contributes = StreamSupport.stream(contributesRepository.findAll().spliterator(), false)
			.collect(Collectors.groupingBy(c -> c.getContestId()));
	}

	public boolean addContributeToContest(String author, String contestId, List<String> content) {
		if(!this.contributes.containsKey(contestId))
			return false;
		if(!canPartecipate(author, contestId))
			return false;
		Contribute contribute = new Contribute(contestId+"."+author, author, contestId, content);
		this.contributes.get(contestId).add(contribute);
		this.contributesRepository.save(contribute);
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
		this.contributesRepository.deleteAll(this.contributes.get(contestId));
		this.contributes.remove(contestId);
		return true;
	}
	
	private boolean canPartecipate(String username, String contestId) {
		return this.contributes.get(contestId).stream()
				.noneMatch(c -> c.getAuthor().equals(username));
	}
	

}
