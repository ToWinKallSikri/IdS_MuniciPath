package Synk.Api.Controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import Synk.Api.Controller.Report.ReportHandler;
import Synk.Api.Controller.SavedContent.SavedContentHandler;
import Synk.Api.Model.Report.Report;
import org.springframework.stereotype.Service;

import Synk.Api.Controller.City.CityHandler;
import Synk.Api.Controller.Feedback.FeedbackHandler;
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
import Synk.Api.Model.Post.Contribute.Contribute;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.User.User;
import Synk.Api.Model.User.Notification.Notification;
import Synk.Api.ViewModel.ProtoGroup;
import Synk.Api.ViewModel.ProtoPost;

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
    private ReportHandler rph;
    private SavedContentHandler sch;
    private FeedbackHandler fh;
    private IdentifierManager idManager;
    
    
    public MuniciPathController(PointHandler poh, UserHandler uh, CityHandler ch, GroupHandler gh, PendingHandler peh){
        this.idManager = new IdentifierManager();
    	this.uh = uh;
        this.ch = ch;
        this.gh = gh;
        this.poh = poh;
        this.peh = peh;
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
     * metodo per cercare un comune tramite il suo id
     * @param cityId id del comune
     * @return il comune se l'ha trovato, null altrimenti
     */
	public City getCity(String cityId) {
		if(cityId == null)
			return null;
		return ch.getCity(cityId);
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
	 * @param author autore del gruppo
	 * @param cityId id del comune
	 * @param data dati dell'insieme
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean createGroup(String author, String cityId, ProtoGroup data) {
		if(author == null || cityId == null || data == null)
			return false;
		return this.gh.createGroup(author, cityId, data);
	}
	
	/**
	 * metodo per modificare un gruppo
	 * @param groupId id del gruppo
	 * @param username username dell'utente
	 * @param data dati del gruppo
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean editGroup(String groupId, String username, ProtoGroup data) {
		if(groupId == null || username == null || data == null)
			return false;
		if(checkStaff(username, idManager.getCityId(groupId)))
			return this.gh.editGroup(groupId, data);
		return this.gh.editGroup(groupId, username, data);
	}

	/**
	 * metodo per rimuovere un gruppo
	 * @param username  username dell'utente
	 * @param groupId id del gruppo
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean removeGroup(String username, String groupId) {
		if(username == null || groupId == null)
			return false;
		if(checkStaff(username, idManager.getCityId(groupId)))
			return this.gh.removeGroup(groupId);
		return this.gh.removeGroup(username, groupId);
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
	 * metodo per creare un post
	 * @param username nome utente
	 * @param pos posizione
	 * @param cityId id del comune
	 * @param post dati del post
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean createPost(String username, Position pos, String cityId, ProtoPost post) {
		if(username == null || pos == null || cityId == null || post == null)
    		return false;
    	return this.poh.createPost(username, pos, cityId, post);
	}

	/**
	 * Metodo per modificare un post
	 * @param postId id del post da modificare
	 * @param username nome utente dell'utente
	 * @param cityId id del comune
	 * @param data info del post
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean editPost(String postId, String username, String cityId, ProtoPost data) {
    	if(postId == null || username == null || cityId == null ||  data == null)
    		return false;
    	if(checkStaff(username, cityId))
    		return this.poh.editPost(postId, data);
        return this.poh.editPost(postId, username, cityId, data);
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
	 * @param postId id del post da eliminare
	 * @param username username dell'utente
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
    public boolean deletePost (String postId, String username) {
    	if(postId == null || username == null)
    		return false;
    	if(checkStaff(username, idManager.getCityId(postId)))
    		return this.poh.deletePost(postId);
    	return this.poh.deletePost(postId, username);
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
        if (username == null || contentId == null)
            return false;
		return checkStaff(username, idManager.getCityId(contentId))
				|| checkAuthor(username, contentId);
	}
	
	public boolean isLimited(String username, String cityId) {
		if (username == null || cityId == null)
            return true;
		return this.getRole(username, cityId) == Role.LIMITED;
	}
	
	//---------------------------DA QUI E' TUTTA GIUNGLA--------------------------------

    public boolean reportContent(String username, String contentId, String motivation) {
        if (username == null || contentId == null)
            return false;
        if(this.isLimited(username, this.idManager.getCityId(contentId)))
        	return false;
        return rph.reportContent(username, contentId, motivation);
    }

    public List<Report> getReports(String cityId, String username){
        if (cityId == null || username == null || (!this.checkStaff(username, cityId)))
            return null;
        return rph.getReports(cityId);
    }
    
    public boolean valute(String username, String contentId, int vote) {
		if(username == null || contentId == null)
			return false;
		if(this.isLimited(username, this.idManager.getCityId(contentId)))
        	return false;
		return this.fh.valute(username, contentId, vote);
	}
    
    public void notifyEvent(String author, String message, String contentId) {
		if(author == null || message == null || contentId == null)
			return;
		this.uh.notifyEvent(author, message, contentId);
    }

	
	public boolean followContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		return this.uh.followContributor(username, contributor);
	}
	
	public boolean unfollowContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		return this.uh.unfollowContributor(username, contributor);
	}
	
	public boolean followCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		if(this.isLimited(username, cityId))
        	return false;
		return this.uh.followCity(username, cityId);
	}
	
	public boolean unfollowCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		if(this.isLimited(username, cityId))
        	return false;
		return this.uh.unfollowCity(username, cityId);
	}
	
	public boolean follow(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		if(this.isLimited(username, this.idManager.getCityId(contentId)))
        	return false;
		return this.uh.follow(username, contentId);
	}
	
	public boolean unfollow(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		return this.uh.unfollow(username, contentId);
	}
	
	public boolean alreadyFollowing(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		return this.uh.alreadyFollowing(username, contentId);
	}
	
	public boolean alreadyFollowingCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.uh.alreadyFollowingCity(username, cityId);
	}
	
	public boolean alreadyFollowingContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		return this.uh.followContributor(username, contributor);
	}
	
	public List<String> getAllFollowed(String username){
		if(username == null)
			return null;
		return this.uh.getAllFollowed(username);
	}

    public int getNumberOfPartecipants(String contentId){
        if(contentId == null)
            return  0;
        return this.sch.getPartecipants(contentId).size();
    }

    public boolean saveSavedContent(String username, String contentId) {
        if(this.isLimited(username, this.idManager.getCityId(contentId)))
            return false;
       return this.sch.saveSavedContent(username, contentId);
    }

    public boolean removeSavedContent(String username, String contentId) {
        if(this.isLimited(username, this.idManager.getCityId(contentId)))
            return false;
        return this.sch.removeSavedContent(username, contentId);
    }

    public List<String> getSavedContent(String username) {
        if(username == null)
            return null;
       return this.sch.getSavedContent(username);
    }
    
    public List<Notification> getMyMessages(String username){
    	if(username == null)
    		return null;
		return this.uh.getMyMessages(username);
	}
	
	public Notification getMyMessage(String username, String id) {
		if(username == null)
    		return null;
		return this.uh.getMyMessage(username, id);
	}
	
	
}
