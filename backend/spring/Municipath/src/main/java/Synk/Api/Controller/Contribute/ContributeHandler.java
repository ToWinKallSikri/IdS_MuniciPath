package Synk.Api.Controller.Contribute;

import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Contribute.ContributeRepository;

@Service
public class ContributeHandler {
	
	
	private ContributeRepository contributesRepository;
	
	/**
	 * costruttore del bean
	 * @param contributesRepository repository dei contributi
	 */
	public ContributeHandler(ContributeRepository contributesRepository) {
		this.contributesRepository = contributesRepository;
	}
	
	/**
	 * metodo per aggiungere contenuti ad un contest
	 * @param author autore del contributo
	 * @param contestId id del contest
	 * @param content contenuto da aggiungere
	 * @return true se il contenuto è stato aggiunto, false altrimenti
	 */
	public boolean addContributeToContest(String author, String contestId, List<String> content) {
		if(!canPartecipate(author, contestId))
			return false;
		Contribute contribute = new Contribute(contestId+"."+author, author, contestId, content);
		this.contributesRepository.save(contribute);
		return true;
	}
	
	/**
	 * metodo per ottenere i contributi di un dato contest
	 * @param contestId da ricercare
	 * @return lista di contributi, o null se non esiste
	 */
	public List<Contribute> getContributes(String contestId){
		return StreamSupport.stream(contributesRepository.findAll().spliterator(), true)
				.filter(c -> c.getContestId().equals(contestId)).toList();
	}
	
	/**
	 * metodo per concludere un contest dichiarando un vincitore
	 * @param contestId id del contest
	 * @param winnerId vincitore del contest
	 * @return lista con i contributi vincenti, o null se non e' possibile dichiararlo vincitore
	 */
	public List<String> declareWinner(String contestId, String winnerId) {
		List<String> content = getContributes(contestId).stream()
				.filter(c -> c.getAuthor().equals(winnerId))
				.map(c -> c.getContribute()).findFirst().orElse(null);
		removeContest(contestId);
		return content;
	}
	
	/**
	 * metodo per rimuovere un contest
	 * @param contestId id del contest da rimuovere
	 */
	public void removeContest(String contestId) {
		this.contributesRepository.deleteAll(getContributes(contestId));
	}
	
	/**
	 * metodo privato per controllare se un dato utente
	 * ha gia' partecipato ad un dato contest
	 * @param username username da controllare
	 * @param contestId contest da controllare
	 * @return true se non ha gia' contribuito, false altrimenti
	 */
	private boolean canPartecipate(String username, String contestId) {
		return getContributes(contestId).stream()
				.noneMatch(c -> c.getAuthor().equals(username));
	}
	

}
