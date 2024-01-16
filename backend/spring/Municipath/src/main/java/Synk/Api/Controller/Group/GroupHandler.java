package Synk.Api.Controller.Group;


import java.time.LocalDateTime;
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
import Synk.Api.View.Model.ProtoGroup;

@Service
public class GroupHandler {
	
	/**
	 * mediatore tra i vari handler
	 */
	private MuniciPathMediator mediator;
	/**
	 * variabile di appoggio
	 */
	private int count;
	/**
	 * gestore degli id
	 */
	private IdentifierManager idManager = new IdentifierManager();
	
	/**
	 * repository dei group
	 */
	@Autowired
	private GroupRepository groupRepository;
	
	/**
	 * imposta il mediator
	 * @param mediator mediatore da inserire
	 */
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
	
	/**
	 * Rimuove un postId da ogni gruppo in cui e' contentuto
	 * @param post postId da rimuovere da ogni gruppo
	 */
	public void removeFromAll(String post) {
		String cityId = idManager.getCityId(post);
		groupRepository.findAll().forEach(g -> g.removePost(post));
		checkCompositionOfGroups(cityId);
	}
	
	/**
	 * metodo di servizio che prende tutti i gruppi di un comune
	 * ed elimina tutti coloro che non hanno almeno due post al loro interno
	 * @param cityId comune da controllare
	 */
	private void checkCompositionOfGroups(String cityId) {
		List<Group> toDelete = getAllFromCity(cityId)
				.filter(g -> !g.isGroup()).toList();
		this.groupRepository.deleteAll(toDelete);
	}
	
	/**
	 * rimuove tutti i gruppi di un dato comune
	 * @param cityId id del comune da cui rimuovere tutti i gruppi
	 */
	public void removeAllFromCity(String cityId) {
		this.groupRepository.deleteAll(getAllFromCity(cityId).toList());
	}
	
	/**
	 * restituisce tutti i comuni di un dato comune
	 * @param cityId id del comune da ricercare
	 * @return tutti i gruppi contenuti in quel comune
	 */
	private Stream<Group> getAllFromCity(String cityId){
		return getStreamOfAll()
				.filter(g -> g.getCityId().equals(cityId));
	}
	
	/**
	 * metodo privato di servizio che restituisce 
	 * uno stream parallelo con tutti i gruppi
	 * @return stream con tutti i gruppi
	 */
	private Stream<Group> getStreamOfAll(){
		return StreamSupport.stream(groupRepository.findAll().spliterator(), true);
	}
	
	/**
	 * metodo per creare un gruppo
	 * @param author autore del gruppo
	 * @param cityId id del comune
	 * @param data dati dell'insieme
	 * @return true se la creazione e' andata a buon fine, false altrimenti
	 */
	public boolean createGroup(String author, String cityId, ProtoGroup data) {
		int level = this.mediator.getRoleLevel(cityId, author);
		if(level < 2)
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(data.getPosts());
		if(posts == null || posts.size() < 1 || 
				(!checkTiming(data.getStartTime(), data.getEndTime(), data.isPersistence())))
			return false;
		String id = getId(cityId);
		Group group = new Group(id, cityId, author, level > 2, level > 3, data);
		if(level == 2)
			this.mediator.addPending(id);
		this.groupRepository.save(group);
		return true;
	}
	
	/**
	 * metodo per modificare un gruppo
	 * @param groupId id del gruppo
	 * @param author autore del gruppo
	 * @param data nuovi dati del group
	 * @return true se la modifica e' andata a buon fine, false altrimenti
	 */
	public boolean editGroup(String groupId, String author, ProtoGroup data) {
		Group group = viewGroup(groupId);
		if(!(group != null && group.getAuthor().equals(author) && 
				checkTiming(data.getStartTime(), data.getEndTime(), data.isPersistence())))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(data.getPosts());
		if(posts == null || posts.size() < 1)
			return false;
		if(mediator.canPublish(idManager.getCityId(groupId), author)) {
			group.edit(data);
			groupRepository.save(group);
		}
		else mediator.addGroupPending(groupId, data);
		return true;
	}
	
	/**
	 * metodo per modificare un gruppo da parte del comune
	 * @param groupId id del gruppo
	 * @param data nuovi dati del group
	 * @return true se la modifica e' andata a buon fine, false altrimenti
	 */
	public boolean editGroup(String groupId, ProtoGroup data) {
		Group group = viewGroup(groupId);
		if(!(group != null && checkTiming(data.getStartTime(), data.getEndTime(), data.isPersistence())))
			return false;
		List<Post> posts = this.mediator.getPostsIfAllExists(data.getPosts());
		if(posts == null || posts.size() < 1)
			return false;
		group.edit(data);
		groupRepository.save(group);
		return true;
	}
	
	/**
	 * metodo di modifica di un gruppo
	 * viene chiamato dal medietor, chiamato
	 * a sua volta dal pending handler
	 * @param request pending di modifica accettato
	 */
	public void editGroup(PendingRequest request) {
		Group group = viewGroup(request.getId());
		group.edit(request);
		groupRepository.save(group);
	}
	
	/**
	 * metodo per rimuovere un gruppo
	 * @param author autore del gruppo
	 * @param groupId id del gruppo
	 * @return true se la rimozione e' andata a buon fine, false altrimenti
	 */
	public boolean removeGroup(String author, String groupId) {
		Group group = viewGroup(groupId);
		if(group == null || (!group.getAuthor().equals(author)))
			return false;
		this.groupRepository.delete(group);
		return true;
	}
	
	/**
	 * metodo per rimuovere un gruppo da parte del comune
	 * @param groupId id del gruppo
	 * @return true se la rimozione e' andata a buon fine, false altrimenti
	 */
	public boolean removeGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null)
			return false;
		this.groupRepository.delete(group);
		return true;
	}

	/**
	 * metodo privato per calcolare il nuovo id 
	 * di un gruppo
	 * @param cityId id del comune
	 * @return nuovo id
	 */
	private String getId(String cityId) {
		this.count = 0;
		getAllFromCity(cityId).forEach(g -> {
			int v = Integer.parseInt(idManager.getContentId(g.getId()));
		    this.count = count > v ? count : v + 1;
		});
		return cityId+".g."+ this.count;
	}
	
	/**
	 * metodo privato per controllare se i seguenti paramentri
	 * hanno senso nella loro combinazione
	 * @param start momento di inizio
	 * @param end momento di fine
	 * @param persistence se e' persistente
	 * @return true se sono corretti insieme, false altrimenti
	 */
	private boolean checkTiming(LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(persistence && start == null && end == null)
			return true;
		if(start != null && end != null && start.isBefore(end))
			return true;
		return false;
	}
	
	/**
	 * metodo per ottenere tutti i gruppi contenuti 
	 * in una lista di id di gruppi
	 * @param groupIds lista di id di gruppi
	 * @return lista di gruppi
	 */
	public List<Group> viewGroups(List<String> groupIds) {
		return getStreamOfAll()
				.filter(g -> groupIds.contains(g.getId()))
				.toList();
	}
	
	/**
	 * metodo per ottenere un gruppo conoscendone l'id
	 * @param groupId id del gruppo
	 * @return gruppo ricercato se esiste, altrimenti null
	 */
	public Group viewGroup(String groupId) {
		Group group = getStreamOfAll()
				.filter(g -> g.getId().equals(groupId))
				.findFirst().orElse(null);
		if(group == null)
			return null;
		group.addOneView();
		group.setVote(this.mediator.getVoteOf(groupId));
		this.groupRepository.save(group);
		return group;
	}
	
	/**
	 * metodo per ottenere tutti gli id di gruppi che
	 * contengono un dato postId, in base a cio' che 
	 * un dato username dovrebbe poter vedere
	 * @param postId id del post
	 * @param username username del visualizzatore
	 * @return lista degli id dei gruppi
	 */
	public List<String> viewGroupFrom(String postId, String username) {
		return getStreamOfAll().filter(g -> g.getPosts().contains(postId))
				.filter(g -> toShow(g, username))
				.map(g -> g.getId()).toList();
	}
	
	/**
	 * metodo privato per verificare se un dato utente
	 * puo' vedere un certo gruppo
	 * @param group gruppo da verificare
	 * @param username utente visualizzatore
	 * @return true se puo' vederlo, false altrimenti
	 */
	private boolean toShow(Group group, String username) {
    	if(group.getAuthor().equals(username))
    		return true;
    	if(!group.isPublished())
    		return false;
    	return group.getEndTime() == null || group.getEndTime().isAfter(LocalDateTime.now());
    }
	
	/**
	 * metodo per approvare un gruppo non pubblicato
	 * @param groupId id del gruppo da pubblicare
	 * @return true se il gruppo e' stato pubblicato, false altrimenti
	 */
	public boolean approveGroup(String groupId) {
		Group group = viewGroup(groupId);
		if(group == null || group.isPublished())
			return false;
		group.setPublished(true);
		group.setPublicationTime(LocalDateTime.now());
		this.groupRepository.save(group);
		return true;
	}
	
	/**
	 * metodo che controlla tutti i gruppi ed
	 * elimina quelli a tempo e non persistenti
	 * che sono scaduti
	 */
	public void checkEndingGroups() {
		LocalDateTime date = LocalDateTime.now();
		getStreamOfAll()
			.filter(g -> ! g.isPersistence()).forEach(g -> {
				if(g.getEndTime().isBefore(date))
					groupRepository.delete(g);
			});
	}
	
	/**
	 * metodo per ottenere l'autore di un dato gruppo
	 * @param groupId id del gruppo
	 * @return l'username dell'autore se esiste, null altrimenti
	 */
	public String getAuthor(String groupId) {
		Group group = this.viewGroup(groupId);
		return group == null ? null : group.getAuthor();
	}
	
}
