package Synk.Api.Controller.Contribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Contribute.ContributeRepository;
import jakarta.annotation.PostConstruct;

@Repository
public class ContributeHandler {
	
	/**
	 * mappa con i contributi associati con gli id dei post
	 */
	private Map<String, List<Contribute>> contributes;
	private ContributeRepository contributesRepository;
	
	/**
	 * costruttore del bean
	 * @param contributesRepository repository dei contributi
	 */
	public ContributeHandler(ContributeRepository contributesRepository) {
		this.contributesRepository = contributesRepository;
		this.contributes = new HashMap<>();
	}
	
	@PostConstruct
	public void loadContributes() {
		this.contributes = StreamSupport.stream(contributesRepository.findAll().spliterator(), false)
			.collect(Collectors.groupingBy(c -> c.getContestId()));
	}
	
	/**
	 * metodo per aggiungere contenuti ad un contest
	 * @param author autore del contributo
	 * @param contestId id del contest
	 * @param content contenuto da aggiungere
	 * @return true se il contenuto è stato aggiunto, false altrimenti
	 */
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
	
	/**
	 * metodo per ottenere i contributi di un dato contest
	 * @param contestId da ricercare
	 * @return lista di contributi, o null se non esiste
	 */
	public List<Contribute> getContributes(String contestId){
		return this.contributes.getOrDefault(contestId, null);
	}
	
	/**
	 * metodo per concludere un contest dichiarando un vincitore
	 * @param contestId id del contest
	 * @param winnerId vincitore del contest
	 * @return lista con i contributi vincenti, o null se non e' possibile dichiararlo vincitore
	 */
	public List<String> declareWinner(String contestId, String winnerId) {
		if(!this.contributes.containsKey(contestId))
			return null;
		List<String> content = this.contributes.get(contestId).stream()
				.filter(c -> c.getAuthor().equals(winnerId))
				.map(c -> c.getContribute()).findFirst().orElse(null);
		removeContest(contestId);
		return content;
	}
	
	/**
	 * metodo per aggiungere un contest
	 * @param contestId id del contest da aggiungere
	 * @return true se il contest e' stato aggiunto, false altrimenti
	 */
	public boolean addContest(String contestId) {
		if(this.contributes.containsKey(contestId))
			return false;
		this.contributes.put(contestId, new ArrayList<>());
		return true;
	}
	
	/**
	 * metodo per rimuovere un contest
	 * @param contestId id del contest da rimuovere
	 * @return true se il contest e' stato rimosso, false altrimenti
	 */
	public boolean removeContest(String contestId) {
		if(!this.contributes.containsKey(contestId))
			return false;
		this.contributesRepository.deleteAll(this.contributes.get(contestId));
		this.contributes.remove(contestId);
		return true;
	}
	
	/**
	 * metodo privato per controllare se un dato utente
	 * ha gia' partecipato ad un dato contest
	 * @param username username da controllare
	 * @param contestId contest da controllare
	 * @return true se non ha gia' contribuito, false altrimenti
	 */
	private boolean canPartecipate(String username, String contestId) {
		return this.contributes.get(contestId).stream()
				.noneMatch(c -> c.getAuthor().equals(username));
	}
	

}
