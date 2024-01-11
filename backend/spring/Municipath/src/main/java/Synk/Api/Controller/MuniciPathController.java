package Synk.Api.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Group.GroupHandler;
import Synk.Api.Controller.Pending.PendingHandler;
import Synk.Api.Controller.Post.PostHandler;
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
	
	
    private PostHandler poh;
	private UserHandler uh;
    private CityHandler ch;
    private GroupHandler gh;
    private PendingHandler peh;
    private IdentifierManager idManager;
    private MuniciPathMediator mediator;
    
    
    public MuniciPathController(PostHandler poh, UserHandler uh, CityHandler ch, GroupHandler gh, PendingHandler peh){
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
     * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean createGroup(String title, String author, boolean sorted, String cityId,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(title == null || author == null || cityId == null || postIds == null)
			return false;
		return this.gh.createGroup(title, author, sorted, cityId, postIds, start, end, persistence);
	}
	
	/**
	 * metodo per modificare un gruppo
	 * @param groupId id del gruppo
	 * @param title nuovo titolo del gruppo
	 * @param author autore del gruppo
	 * @param sorted se il gruppo ora e' ordinato
	 * @param postIds nuovi id dei post
	 * @param start nuovo momento di inizio
	 * @param end nuovo momento di fine
	 * @param persistence se e' persistente
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
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
	 * @param groupId id del gruppo
	 * @param title nuovo titolo del gruppo
	 * @param sorted se il gruppo ora e' ordinato
	 * @param postIds nuovi id dei post
	 * @param start nuovo momento di inizio
	 * @param end nuovo momento di fine
	 * @param persistence se e' persistente
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean editGroupFromStaff(String username, String groupId, String title, boolean sorted,
			List<String> postIds, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(username == null || groupId == null ||  title == null 
				|| postIds == null || (!checkStaff(username, idManager.getCityId(groupId))))
			return false;
		return this.gh.editGroup(groupId, title, sorted, postIds, start, end, persistence);
	}

	/**
	 * metodo per rimuovere un gruppo
	 * @param author,  autore del gruppo
	 * @param groupId, id del gruppo
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean removeGroup(String author, String groupId) {
		if(author == null || groupId == null)
			return false;
		return this.gh.removeGroup(author, groupId);
	}

	/**
	 * metodo per rimuovere un gruppo da parte dell'amministratore del comune
	 * @param username username dell'utente che esegue l'operazione
	 * @param groupId id del gruppo da rimuovere
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean removeGroupFromStaff(String username, String groupId) {
		if(username == null || groupId == null || (!checkStaff(username, idManager.getCityId(groupId))))
			return false;
		return this.gh.removeGroup(groupId);
	}

	/**
	 * Metodo per visualizzare un gruppo
	 * @param groupId, id del gruppo da visualizzare
	 * @return il gruppo da visualizzare
	 */
	public Group viewGroup(String groupId) {
		if(groupId == null)
			return null;
		return this.gh.viewGroup(groupId);
	}

	/**
	 * Metodo per visualizzare n gruppi, in base agli Id dei gruppi che si vogliono visualizzare
	 * @param groupIds lista degli id dei gruppi da visualizzare
	 * @return lista dei gruppi da visualizzare
	 */
	public List<Group> viewGroups(List<String> groupIds) {
		if(groupIds == null)
			return null;
		return this.gh.viewGroups(groupIds);
	}

	/**
	 * Metodo per creare un nuovo post
	 * @param title, titolo del post
	 * @param type, tipo del post
	 * @param text, descrizione del post
	 * @param author, autore del post
	 * @param pos, posizione del post
	 * @param cityId, id del comune
	 * @param data, contenuti multimediali del post
	 * @param start, momento di inizio
	 * @param end, momento di fine
	 * @param persistence, booleano che indica se il post e' persistente o meno dopo la scadenza
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, List<String> list, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(title == null || type == null || text == null || author == null 
    			|| pos == null || cityId == null || list == null)
    		return false;
    	return this.poh.createPost(title, type, text, author, pos, cityId, list, start, end, persistence);
    }

	/**
	 * Metodo per modificare un post
	 * @param postId, id del post da modificare
	 * @param title, titolo del post
	 * @param type, tipo del post
	 * @param text, descrizione del post
	 * @param author, autore del post
	 * @param cityId, id del comune
	 * @param data, contenuti multimediali del post
	 * @param start, momento di inizio
	 * @param end, momento di fine
	 * @param persistence, booleano che indica se il post e' persistente o meno, dopo la scadenza
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, List<String> list, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(postId == null || title == null || type == null || text == null 
    			|| author == null ||  cityId == null || list == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, author, cityId, list, start, end, persistence);
    }

	/**
	 * Metodo per modificare un post da parte dell'amministratore del comune
	 * @param username, username dell'utente che esegue l'operazione
	 * @param postId, id del post da modificare
	 * @param title, titolo del post
	 * @param type, tipo del post
	 * @param text, descrizione del post
	 * @param data, contenuti multimediali del post
	 * @param start, momento di inizio
	 * @param end, momento di fine
	 * @param persistence, booleano che indica se il post e' persistente o meno, dopo la scadenza
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean editPostFromStaff(String username, String postId, String title, PostType type, String text,
    		List<String> list, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
			return false;
    	if(title == null || type == null || text == null || list == null)
    		return false;
        return this.poh.editPost(postId, title, type, text, list, start, end, persistence);
    }

	/**
	 * Metodo per ottenere tutti i punti di un comune
	 * @param cityId, id del comune
	 * @param username, username dell'utente che esegue l'operazione
	 * @return lista dei punti del comune
	 */
    public List<Point> getPoints (String cityId, String username) {
    	if(cityId == null || username == null)
    		return null;
        return this.poh.getPoints(cityId, username);
      
    }

	/**
	 * Metodo per visualizzare tutti i post contenuti in un certo punto
	 * @param pointId, id del punto
	 * @param username, username dell'utente che esegue l'operazione
	 * @return lista dei post contenuti nel punto
	 */
    public List<Post> viewPosts (String pointId, String username) {
    	if(pointId == null || username == null)
    		return null;
        return this.poh.viewPosts(pointId, username);
    }

	/**
	 * Metodo per visualizzare tutti i post salvati in una determinata lista, secondo i loro id
	 * @param postIds, lista degli id dei post da visualizzare
	 * @return la lista di post desiderata
	 */
    public List<Post> viewPosts (List<String> postIds) {
    	if(postIds == null)
    		return null;
        return this.poh.getPosts(postIds);
    }

	/**
	 * Metodo per visualizzare un certo post, in base al suo id
	 * @param postId, id del post da visualizzare
	 * @param username, username dell'utente che esegue l'operazione
	 * @return il post desiderato
	 */
    public Post viewPost(String postId, String username) {
    	if(postId == null || username == null)
    		return null;
        return this.poh.getPost(postId, username);
    }

	/**
	 * Metodo per eliminare un certo post, in base al suo id
	 * @param postId, id del post da eliminare
	 * @param author, autore del post (si puo' eliminare solo un post di cui si e' autori)
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean deletePost (String postId, String author) {
    	if(postId == null || author == null)
    		return false;
    	return this.poh.deletePost(postId, author);
    }

	/**
	 * Metodo per eliminare un certo post, in base al suo id, da parte dell'amministratore del comune
	 * @param username, username dell'utente che esegue l'operazione
	 * @param postId, id del post da eliminare
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean deletePostFromStaff (String username, String postId) {
		if(username == null || postId == null || (!checkStaff(username, idManager.getCityId(postId))))
			return false;
    	return this.poh.deletePost(postId);
    }

	/**
	 * Metodo per ottenere tutti i contributi fatti a un certo post (di tipo CONTEST)
	 * @param username, username dell'utente che esegue l'operazione
	 * @param postId, id del post
	 * @return lista dei contributi fatti al post
	 */
	public List<Contribute> getContributes(String username, String postId){
    	if(postId == null || username == null)
    		return null;
		return this.poh.getContributes(username, postId);
	}

	/**
	 * Metodo per aggiungere un contributo ad un post di tipo CONTEST
	 * @param contestAuthor, autore del contest
	 * @param contestId, id del contest
	 * @param content, contenuto del contributo
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean addContentToContest(String contestAuthor, String contestId, List<String> content) {
    	if(contestAuthor == null || contestId == null || content == null)
    		return false;
		return this.poh.addContentToContest(contestAuthor, contestId, content);
	}

	/**
	 * Metodo per decretare il vincitore di un contest
	 * @param author, autore del contest
	 * @param contestId, id del contest
	 * @param winnerId, id del vincitore
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean declareWinner(String author, String contestId, String winnerId) {
		if(author == null || contestId == null || winnerId == null)
			return false;
		return this.poh.declareWinner(author, contestId, winnerId);
	}

	/**
	 * Metodo per giudicare una richiesta in pending
	 * @param username, username dell'utente che esegue l'operazione
	 * @param pendingId, id della richiesta
	 * @param outcome, verdetto sulla richiesta
	 * @param motivation, motivazione del verdetto
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean judge(String username, String pendingId, boolean outcome, String motivation) {
		if(username == null || pendingId == null || (!checkStaff(username, idManager.getCityId(pendingId))))
			return false;
    	if(motivation == null)
    		return false;
		return this.peh.judge(pendingId, outcome, motivation);
	}

	/**
	 * Metodo per ottenere tutte le richieste in pending di un comune
	 * @param username, username dell'utente che esegue l'operazione
	 * @param cityId, id del comune
	 * @return lista delle richieste in pending
	 */
	public List<PendingRequest> getAllRequest(String username, String cityId){
		if(username == null || (!checkStaff(username, cityId)))
			return null;
		if(cityId == null)
			return null;
		return this.peh.getAllRequest(cityId);
	}

	/**
	 * Metodo per ottenere una certa richiesta in pending
	 * @param username, username dell'utente che esegue l'operazione
	 * @param requestId, id della richiesta
	 * @return la richiesta desiderata
	 */
	public PendingRequest getRequest(String username, String requestId) {
		if(username == null || (!checkStaff(username, this.idManager.getCityId(requestId))))
			return null;
		if(requestId == null)
			return null;
		return this.peh.getRequest(requestId);
	}

	/**
	 * Metodo per aggiungere un utente alla piattaforma
	 * @param username, username dell'utente
	 * @param password, password dell'utente
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean addUser(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.addUser(username, password);
	}

	/**
	 * Metodo per rimuovere un utente dalla piattaforma
	 * @param username, username dell'utente che esegue l'operazione
	 * @param toRemove, username dell'utente da rimuovere
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean removeUser(String username, String toRemove) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toRemove == null)
			return false;
		return this.uh.removeUser(toRemove);
	}

	/**
	 * Metodo per controllare se ci sono match della password in chiaro con quella criptata, per un determinato utente
	 * @param username, lo username dell'utente
	 * @param password, la password dell'utente
	 * @return true se la password corrisponde, false altrimenti
	 */
	public boolean isThePassword(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.isThePassword(username, password);
	}

	/**
	 * Metodo per cambiare la password di un utente
	 * @param username, lo username dell'utente
	 * @param password, la nuova password dell'utente
	 * @return true se la password è stata cambiata, false altrimenti
	 */
	public boolean changePassword(String username, String password) {
		if(username == null || password == null)
			return false;
		return this.uh.changePassword(username, password);
	}

	/**
	 * Metodo per convalidare un utente dopo che ha fatto il Sign-In nella piattaforma
	 * @param username, lo username dell'utente da convalidare
	 * @return true se l'utente è stato convalidato, false altrimenti
	 */
	public boolean userValidation(String username, String toValidate) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toValidate == null)
			return false;
		return this.uh.userValidation(toValidate);
	}

	/**
	 * Metodo per gestire l'autorizzazione da Gestore della Piattaforma
	 * @param username, lo username dell'utente da gestire
	 * @param auth, true se si vuole settare come Gestore, false altrimenti
	 * @return true se l'autorizzazione è stata gestita, false altrimenti
	 */
	public boolean manageManager(String username, String toManage, boolean auth) {
		if(username == null || (!checkManager(username)))
    		return false;
		if(toManage == null)
			return false;
		return this.uh.manageManager(toManage, auth);
	}

	/**
	 * Metodo per ottenere tutti gli utenti non convalidati
	 * @param username, username dell'utente che esegue l'operazione
	 * @return una lista di tutti gli utenti non convalidati
	 */
    public List<User> getUsersNotConvalidated(String username){
    	if(username == null || (!checkManager(username)))
    		return null;
    	return this.uh.getNotConvalidatedUsers();
    }

	/**
	 * Metodo per controllare se un post/gruppo a tempo, sia scaduto. L'operazione avviene tramite un thread lanciato
	 * a intervalli di 5 minuti e alla creazione.
	 */
	private void checkEnding() {
		this.poh.checkEndingPosts();
		this.gh.checkEndingGroups();
	}

	/**
	 * Metodo per verificare se un utente sia un gestore della piattaforma o meno
	 * @param username, username dell'utente da verificare
	 * @return un booleano che indica se l'utente è un gestore o meno
	 */
	public boolean checkManager(String username) {
		if(username == null)
			return false;
    	return this.uh.getUser(username).isManager();
    }

	/**
	 * Metodo per verificare se un utente sia un curatore di un certo comune o meno
	 * @param username, username dell'utente da verificare
	 * @param cityId, id del comune in questione
	 * @return un booleano che indica se l'utente è un curatore o meno
	 */
    private boolean checkCurator(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
    	Role role = this.getRole(username, cityId);
    	return role == Role.CURATOR;
    }

	/**
	 * Metodo per verificare se un utente sia un moderatore o curatore di un certo comune o meno
	 * @param username, username dell'utente da verificare
	 * @param cityId, id del comune in questione
	 * @return un booleano che indica se l'utente è un moderatore o curatore o meno
	 */
    private boolean checkStaff(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
    	return this.ch.isTheStaff(cityId, username);
    }

	/**
	 * Metodo per verificare se un utente sia l'autore di un certo post/gruppo o meno
	 * @param username, username del presunto autore
	 * @param contentId, id del post/gruppo in questione
	 * @return un booleano che indica se l'utente è l'autore o meno
	 */
	public boolean checkAuthor(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		return username.equals(this.idManager.isGroup(contentId) ?
				this.gh.getAuthor(contentId) : this.poh.getAuthor(contentId));
	}

	/**
	 * Metodo per verificare se un utente abbia i permessi per modificare o eliminare un certo post/gruppo o meno
	 * @param username, username dell'utente da verificare
	 * @param contentId, id del post/gruppo in questione
	 * @return un booleano che indica se l'utente ha i permessi o meno
	 */
	public boolean havePowerWithIt(String username, String contentId) {
		return checkStaff(username, idManager.getCityId(contentId))
				|| checkAuthor(username, contentId);
	}
    
}
