package Synk.Api.Controller;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PointHandler;
import Synk.Api.Controller.User.UserHandler;
import Synk.Api.Model.City.City;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;
import Synk.Api.Model.Group.Group;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.User.User;

/**
 * questa classe funge da maschera per il lato controller
 * del progetto e ha la responsabilita' di controllare
 * la validita' delle richieste del lato view
 */
@Service
public class MuniciPathController {
	
	
    private PointHandler poh;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    private PendingHandler peh;
    private IdentifierManager idManager;
    private MuniciPathMediator mediator;
    
    
    public MuniciPathController(PointHandler poh, UserHandler uh, CityHandler ch, GroupHandler gh, PendingHandler peh){
    	this.mediator = new MuniciPathMediator();
        this.idManager = new IdentifierManager();
    	this.uh = uh;
		this.uh.setMediator(mediator);
        this.ch = ch;
        this.ch.setMediator(mediator);
        this.gh = gh;
        this.gh.setMediator(mediator);
        this.poh = poh;
        this.poh.setMediator(mediator);
        this.peh = peh;
        this.peh.setMediator(mediator);
        mediator.setUser(uh);
        mediator.setCity(ch);
        mediator.setGroup(gh);
        mediator.setPoint(poh);
        mediator.setPending(peh);
        /*
         * parallelamente, elimino i post e i gruppi terminati
         * e non persistenti ogni 5 minuti.
         */
        Executors.newScheduledThreadPool(1)
        	.scheduleAtFixedRate(this::checkEnding, 0, 5, TimeUnit.MINUTES);
    }
    
    /**
     * metodo per creare un comune
     * @param username username dell'utente
     * @param cityName nome del comune
     * @param cap cap del comune
     * @param curator curatore del comune
     * @param pos posizione del comune
     * @return true se la creazione e' andata a buon fine, false altrimenti
     */
    public boolean createCity(String username, String cityName, int cap, String curator, Position pos ) {
		if(username == null || (!checkManager(username)))
    		return false;
    	if(cityName == null || curator == null || pos == null)
    		return false;
        return ch.createCity(cityName, cap, curator, pos);
    }
    
    /**
     * metodo per modificare le informazioni di un comune
     * @param username username dell'utente
     * @param id id del comune
     * @param cityName nuovonome del comune
     * @param cap nuovo cap del comune
     * @param curator nuovo curatore del comune
     * @param pos nuova posizione del comune
     * @return true se la modifica e' andata a buon fine, false altrimenti
     */
    public boolean updateCity(String username, String id, String cityName, int cap, String curator, Position pos ){
		if(username == null || (!checkManager(username)))
    		return false;
    	if(id == null || cityName == null || curator == null || pos == null)
    		return false;
		return ch.updateCity(id, cityName, cap, curator, pos);
    }
    
    /**
     * metodo per eliminare un comune
     * @param username username dell'utente
     * @param cityId id del comune
     * @return true se l'eliminazione e' andata a buon fine, false altrimenti
     */
    public boolean deleteCity(String username, String cityId) {
		if(username == null || (!checkManager(username)))
    		return false;
    	if(cityId == null)
    		return false;
    	return ch.deleteCity(cityId);
    }
    
    /**
     * metodo per cercare un comune partendo dal nome
     * @param search comune da cercare
     * @return lista dei comuni con nomi simili
     */
    public List<City> searchCity(String search){
    	if(search == null)
    		search = "";
    	return ch.getCities(search);
    }
	
    /**
     * imposta il ruolo di un utente rispetto ad un dato comune
     * @param username username dell'utente che esegue l'operazione
     * @param toSet username dell'utente da impostare
     * @param cityId id del comune sul quale impostarlo
     * @param role ruolo da impostare
     * @return true se l'operazione e' andata a buon fine, false altimenti
     */
	public boolean setRole(String username, String toSet, String cityId, Role role) {
		if(cityId == null || username == null || (!checkStaff(username, cityId)))
			return false;
		if(toSet == null || role == null)
			return false;
		return this.ch.setRole(toSet, cityId, role);
	}
	
	/**
	 * metodo per ottenere il ruolo di un utente rispetto ad un comune
	 * @param username  username dell'utente
	 * @param cityId id del comune
	 * @return ruolo che ha l'utente rispetto al comune
	 */
	public Role getRole(String username, String cityId) {
		if(username == null)
			return Role.LIMITED;
		if(cityId == null)
			return Role.TOURIST;
		return this.ch.getRole(username, cityId);
	}
	
	/**
	 * metodo per aggiungere un moderatore ad un comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param toSet username dell'utente da impostare
	 * @param cityId id del comune dove impostarlo
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean addModerator(String username, String toSet, String cityId) {
		if(username == null || cityId == null || (!checkCurator(username, cityId)))
			return false;
		if(toSet == null)
			return false;
		return this.ch.addModerator(toSet, cityId);
	}
	
	/**
	 * metodo per rimuovere un moderatore ad un comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param toSet username dell'utente da impostare
	 * @param cityId id del comune dove impostarlo
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean removeModerator(String username, String toSet, String cityId) {
		if(username == null || cityId == null || (!checkCurator(username, cityId)))
			return false;
		if(toSet == null)
			return false;
		return this.ch.removeModerator(toSet, cityId);
	}
	
	/**
	 * metodo per aggiungere una richiesta di permessi di pubblicazione
	 * in un dato comune
	 * @param username username dell'utente
	 * @param cityId id del comune
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean addRequest(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.ch.addRequest(username, cityId);
	}
	
	/**
	 * metodo per ottenere le richieste di permessi di un dato comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param cityId id del comune dove cercare
	 * @return lista delle richieste trovate
	 */
	public List<RoleRequest> getRequests(String username, String cityId){
		if(username == null || cityId == null || (!checkStaff(username, cityId)))
			return null;
		return this.ch.getRequests(cityId);
	}
	
	/**
	 * metodo per giudicare una richiesta di autorizzazione in un dato comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param requestId id della richiesta
	 * @param outcome verdetto sulla richiesta
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean judge(String username, String requestId, boolean outcome) {
		if(username == null || requestId == null || (!checkStaff(username, idManager.getCityId(requestId))))
			return false;
		return this.ch.judge(requestId, outcome);
	}
	
	/**
	 * metodo per creare un gruppo di post
	 * @param title titolo del gruppo
	 * @param author autore del gruppo
	 * @param sorted se il gruppo e' ordinato o meno
	 * @param cityId id del comune
	 * @param postIds identificatori dei post
	 * @param start momento di inizio
	 * @param end momento di fine
	 * @param persistence se e' persistente
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(title == null || author == null || cityId == null || postIds == null)
			return false;
		return this.gh.createGroup(title, author, sorted, cityId, postIds, start, end, persistence);
	}
	
	/**
	 * metodo per modificare un gruppo
	 * @param groupId id del grupp
	 * @param title nuovo titolo del gruppo
	 * @param author autore del gruppo
	 * @param sorted se il gruppo ora e' ordinato
	 * @param postIds nuovi id dei post
	 * @param start nuovo momento di inizio
	 * @param end nuovo momento di fine
	 * @param persistence se e' persistente
	 * @return true se l'operazione e' andata a buon fine, false altimenti
	 */
	public boolean editGroup(String groupId, String title, String author, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(groupId == null || title == null || author == null || postIds == null)
			return false;
		return this.gh.editGroup(groupId, title, author, sorted, postIds, start, end, persistence);
	}
	
	/**
	 * metodo per modificare un gruppo da parte del comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param groupId 
	 * @param title
	 * @param sorted
	 * @param postIds
	 * @param start
	 * @param end
	 * @param persistence
	 * @return
	 */
	public boolean editGroupFromStaff(String username, String groupId, String title, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(username == null || groupId == null ||  title == null 
				|| postIds == null || (!checkStaff(username, idManager.getCityId(groupId))))
			return false;
		return this.gh.editGroup(groupId, title, sorted, postIds, start, end, persistence);
	}
	
	public boolean removeGroup(String author, String groupId) {
		if(author == null || groupId == null)
			return false;
		return this.gh.removeGroup(author, groupId);
	}
	
	public boolean removeGroupFromStaff(String username, String groupId) {
		if(username == null || groupId == null || (!checkStaff(username, idManager.getCityId(groupId))))
			return false;
		return this.gh.removeGroup(groupId);
	}
	
	public Group viewGroup(String groupId) {
		if(groupId == null)
			return null;
		return this.gh.viewGroup(groupId);
	}
	
	public List<Group> viewGroups(List<String> groupIds) {
		if(groupIds == null)
			return null;
		return this.gh.viewGroups(groupIds);
	}
	
    
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(title == null || type == null || text == null || author == null 
    			|| pos == null || cityId == null || data == null)
    		return false;
    	return this.poh.createPost(title, type, text, author, pos, cityId, data, start, end, persistence);
    }
    
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(postId == null || title == null || type == null || text == null 
    			|| author == null ||  cityId == null || data == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, author, cityId, data, start, end, persistence);
    }
    
    
    public boolean editPostFromStaff(String username, String postId, String title, PostType type, String text,
    		ArrayList<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
			return false;
    	if(title == null || type == null || text == null || data == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, data, start, end, persistence);
    }
    
    public List<Point> getPoints (String cityId, String username) {
    	if(cityId == null || username == null)
    		return null;
        return this.poh.getPoints(cityId, username);
      
    }
    
    public List<Post> viewPosts (String pointId, String username) {
    	if(pointId == null || username == null)
    		return null;
        return this.poh.viewPosts(pointId, username);
    }
    
    public List<Post> viewPosts (List<String> postIds) {
    	if(postIds == null)
    		return null;
        return this.poh.getPosts(postIds);
    }
    
    public Post viewPost(String postId, String username) {
    	if(postId == null || username == null)
    		return null;
        return this.poh.getPost(postId, username);
    }
    
    public boolean deletePost (String postId, String author) {
    	if(postId == null || author == null)
    		return false;
    	return this.poh.deletePost(postId, author);
    }
    
    
    public boolean deletePostFromStaff (String username, String postId) {
		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
			return false;
    	return this.poh.deletePost(postId);
    }
    
    
    public List<Contribute> getContributes(String username, String postId){
    	if(postId == null || username == null)
    		return null;
		return this.poh.getContributes(username, postId);
	}
    
    
    public boolean addContentToContest(String contestAuthor, String contestId, List<String> content) {
    	if(contestAuthor == null || contestId == null || content == null)
    		return false;
		return this.poh.addContentToContest(contestAuthor, contestId, content);
	}
	
	
	public boolean declareWinner(String author, String contestId, String winnerId) {
		if(author == null || contestId == null || winnerId == null)
			return false;
		return this.poh.declareWinner(author, contestId, winnerId);
	}
    
    
    public boolean judge(String username, String pendingId, boolean outcome, String motivation) {
		if(username == null || pendingId == null || (!checkStaff(username, idManager.getCityId(pendingId))))
			return false;
    	if(motivation == null)
    		return false;
		return this.peh.judge(pendingId, outcome, motivation);
	}
	
    
	public List<PendingRequest> getAllRequest(String username, String cityId){
		if(username == null || (!checkStaff(username, cityId)))
			return null;
		if(cityId == null)
			return null;
		return this.peh.getAllRequest(cityId);
	}
	
	public PendingRequest getRequest(String username, String requestId) {
		if(username == null || (!checkStaff(username, this.idManager.getCityId(requestId))))
			return null;
		if(requestId == null)
			return null;
		return this.peh.getRequest(requestId);
	}
	
	
	
	public boolean addUser(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.addUser(username, password);
	}
	
	
	public boolean removeUser(String username, String toRemove) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toRemove == null)
			return false;
		return this.uh.removeUser(toRemove);
	}
	
	public boolean isThePassword(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.isThePassword(username, password);
	}
	
	public boolean changePassword(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.changePassword(username, password);
	}
	
	
	public boolean userValidation(String username, String toValidate) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toValidate == null)
			return false;
		return this.uh.userValidation(toValidate);
	}
	
	
	public boolean manageManager(String username, String toManage, boolean auth) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toManage == null)
			return false;
		return this.uh.manageManager(toManage, auth);
	}
    
    
    public List<User> getUsersNotConvalidated(String username){
    	if(username == null || (!checkManager(username)))
    		return null;
    	return this.uh.getNotConvalidatedUsers();
    }
	
	
	private void checkEnding() {
		this.poh.checkEndingPosts();
		this.gh.checkEndingGroups();
	}
    
	
    public boolean checkManager(String username) {
		if(username == null)
			return false;
    	return this.uh.getUser(username).isManager();
    }

    private boolean checkCurator(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
    	Role role = this.getRole(username, cityId);
    	return role == Role.CURATOR;
    }
    
    private boolean checkStaff(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
    	Role role = this.getRole(username, cityId);
    	return role == Role.CURATOR || role == Role.MODERATOR;
    }
	
	public boolean isAuthor(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		return this.idManager.isGroup(contentId) ? username.equals(this.gh.getAuthor(contentId)) : username.equals(this.gh.getAuthor(contentId));
	}
    
}
