package Synk.Api.Controller;

import java.util.ArrayList;
import java.util.List;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Feedback.FeedbackHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.View.Model.ProtoGroup;
import Synk.Api.View.Model.ProtoPost;

/**
 * classe che implementa il pattern
 * mediator per gli handler del progetto
 */
public class MuniciPathMediator {
	
	private PointHandler point;
	private UserHandler user;
	private CityHandler city;
	private GroupHandler group;
	private PendingHandler pending;
	private FeedbackHandler feedback;
	private IdentifierManager idManager = new IdentifierManager();
	
	
	public void setPoint(PointHandler point) {
		this.point = point;
	}
	
	public void setUser(UserHandler user) {
		this.user = user;
	}
	
	public void setCity(CityHandler city) {
		this.city = city;
	}
	
	public void setGroup(GroupHandler group) {
		this.group = group;
	}
	
	public void setPending(PendingHandler pending) {
		this.pending = pending;
	}
	
	public void setFeedback(FeedbackHandler feedback) {
		this.feedback = feedback;
	}
	
	/**
	 * controlla se un dato autore può postare in un dato comune
	 * @param cityId id del comune
	 * @param author username dell'autore
	 * @return true se puo' postare, false altrimenti
	 */
	public boolean isAuthorizedToPost(String cityId, String author) {
		return this.city.isAuthorized(cityId, author);
	}
	
	/**
	 * controlla se un dato autore può pubblicare in un dato comune
	 * @param cityId id del comune
	 * @param author username dell'autore
	 * @return true se puo' pubblicare, false altrimenti
	 */
	public boolean canPublish(String cityId, String author) {
		return this.city.canPublish(cityId, author);
	}
	
	/**
	 * prende il ruolo e ne ricava un numero 
	 * che rapprensenta quanto
	 * è autorizzato in quel comune.
	 * @param cityId
	 * @param author
	 * @return un intero che rappresenta il grado del suo permesso.
	 */
	public int getRoleLevel(String cityId, String author) {
		Role role = this.city.getRole(author, cityId);
		switch(role) {
			case CURATOR: 
				return 5;
			case MODERATOR: 
				return 4;
			case CONTR_AUTH: 
				return 3;
			case CONTR_NOT_AUTH: 
				return 2;
			case TOURIST: 
				return 1;
			default:
				return 0;
		}
	}
	

	/**
	 * metodo per vedere se in un dato comune, un utente e' moderatore o curatore
	 * @param cityId
	 * @param author
	 * @return
	 */
	public boolean isTheStaff(String cityId, String author) {
		return this.city.isTheStaff(cityId, author);
	}
	
	/**
	 * metodo per aggiungere un pending di creazione al pending handler
	 * @param id id del contenuto da inserire
	 */
	public void addPending(String id) {
		this.pending.addRequest(id);
	}
	
	/**
	 * metodo per aggiungere un pending di modifica post al pending handler
	 * @param postId id del post
	 * @param data dati del post
	 */
	public void addPostPending(String postId, ProtoPost data) {
		this.pending.addPostRequest(postId, data);
	}
	
	/**
	 * metodo per aggiungere un pending di modifica group al pending handler
	 * @param groupId id del gruppo
	 * @param data dati del group
	 */
	public void addGroupPending(String groupId, ProtoGroup data) {
		this.pending.addGroupRequest(groupId, data);
	}
	
	/**
	 * metodo per rimuovere tutti i gruppi di un dato comune
	 * @param cityId id del comune
	 */
	public void removeAllCityGroups(String cityId) {
		this.group.removeAllFromCity(cityId);
		
	}
	
	/**
	 * metodo per rimuovere da tutti i gruppi un certo post 
	 * @param post post id da rimuovere
	 */
	public void removeFromAllGroups(String post) {
		this.group.removeFromAll(post);
	}
	
	/**
	 * metodo per ottenere un comune dall'id
	 * @param cityID id del comune
	 * @return comune ricercato se esiste, null altrimenti
	 */
	public City getCity(String cityID) {
		return this.city.getCity(cityID);
	}
	
	/**
	 * metodo per associare un curatore ad un comune
	 * @param curator username del nuovo curatore
	 * @param id id del comune
	 * @return true se e' stato associato, false altrimenti
	 */
	public boolean matchCurator(String curator, String id) {
		return this.user.matchCurator(curator, id);
	}

	/**
	 * metodo per inizializzare il point handler 
	 * rispetto ad un nuovo comune
	 * @param id id del nuovo comune
	 * @param cityName nome del comune
	 * @param curator curatore del comune
	 * @param pos posizione del comune
	 */
	public void createPostForNewCity(String id, String cityName, String curator, Position pos) {
		ProtoPost post = new ProtoPost();
		post.setMultimediaData(new ArrayList<>());
		post.setPersistence(true);
		post.setTitle("Comune di "+cityName);
		post.setMultimediaData(new ArrayList<>());
		post.setType(PostType.INSTITUTIONAL);
		post.setText("");
        this.point.createPost(curator, pos, id, post);
	}
	
	/**
	 * metodo per cambiare curatore di un comune
	 * @param curator nuovo curatore
	 * @param cityId id del comune
	 * @return true se il curatore e' stato cambiato, false altrimenti
	 */
	public boolean changeCurator(String curator, String cityId) {
		return this.user.changeCurator(curator, cityId);
	}
	
	/**
	 * metodo chiamato da city handler per eliminare un comune
	 * si assicura che anche l'user hander e point handler vengano aggiornati
	 * @param cityId id del comune da eliminare
	 */
	public void deleteCity(String cityId) {
    	this.user.discreditCurator(cityId);
    	this.point.deleteCityPoints(cityId);
	}
	
	/**
	 * metodo che controlla se un dato username esiste
	 * @param username username da controllare
	 * @return true se esiste, false altrimenti
	 */
	public boolean usernameExists(String username) {
		return this.user.usernameExists(username);
	}
	
	
	/**
	 * metodo chiamato dal group handler per controllare se
	 * un insieme di id di post corrispondono ad un uguale numero
	 * di post esistenti
	 * @param postIds id dei post da controllare
	 * @return lista dei post se tutti esistono, null altrimenti
	 */
	public List<Post> getPostsIfAllExists(List<String> postIds) {
		return this.point.getPostsIfAllExists(postIds);
	}
	
	/**
	 * metodo chiamato dal point handler per aggiornare
	 * un post quando viene visualizzato
	 * @param post post visualizzato
	 * @param username visualizzatore
	 * @return lista di gruppi di cui il post fa parte
	 */
	public List<String> viewGroupFrom(Post post, String username) {
		return this.group.viewGroupFrom(post.getId(), username);
	}
	
	/**
	 * metodo per ottenere l'autore di un post o di un
	 * gruppo tramite l'id
	 * @param pendingId id del contenuto
	 * @return l'autore se esiste, false altrimenti
	 */
	public String getAuthor(String pendingId) {
		return idManager.isGroup(pendingId) ? group.getAuthor(pendingId) : point.getAuthor(pendingId);
	}
	
	/**
	 * metodo per inviare un messaggio ad un dato utente
	 * @param username destinatario del messaggio
	 * @param message messaggio da inviare
	 */
	public void send(String username, String message) {
		this.user.send(username, message);
	}
	
	/**
	 * metodo per gestire una richiesta in pending di gruppo approvata
	 * @param request richiesta da gestire
	 */
	public void manageGroupRequest(PendingRequest request) {
		if(request.isNew())
			this.group.approveGroup(request.getId());
		else this.group.editGroup(request);
	}
	
	/**
	 * metodo per gestire una richiesta di pensing di post approvata
	 * @param request richiesta da gestire
	 */
	public void managePostRequest(PendingRequest request) {
		if(request.isNew())
			this.point.approvePost(request.getId());
		else this.point.editPost(request);
	}
	
	/**
	 * metodo per eliminare un group in pending
	 * @param pendingId id del gruppo
	 */
	public void deletePendingGroup(String pendingId) {
		this.group.removeGroup(pendingId);
		
	}
	
	/**
	 * metodo per eliminare un post in pending
	 * @param pendingId id del post
	 */
	public void deletePendingPost(String pendingId) {
		this.point.deletePost(pendingId);
	}

	public float getVoteOf(String id) {
		return this.feedback.getFeedback(id);
	}

	public boolean contentExist(String contentId) {
		return getMetaData(contentId) != null;
	}

	public boolean checkCity(String cityId) {
		return this.city.getCity(cityId) != null;
	}

	public MetaData getMetaData(String contentId) {
		if(this.idManager.isGroup(contentId))
			return this.group.viewGroup(contentId);
		return this.point.getPost(contentId);
	}

    public List<String> getPartecipants(String contentId) {
        return null;
    }
}
