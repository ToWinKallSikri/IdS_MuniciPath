package Synk.Api.Controller.Post;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.Contribute.ContributeHandler;
import Synk.Api.Controller.WeatherService.WeatherForecastProxy;
import Synk.Api.Controller.WeatherService.WeatherService;
import Synk.Api.Model.Pending.PendingRequest;
import Synk.Api.Model.Post.Point;
import Synk.Api.Model.Post.PointRepository;
import Synk.Api.Model.Post.Position;
import Synk.Api.Model.Post.Post;
import Synk.Api.Model.Post.PostRepository;
import Synk.Api.Model.Post.PostType;
import Synk.Api.Model.Post.Contribute.Contribute;
import jakarta.annotation.PostConstruct;

@Repository
public class PointHandler {
	
	/**
	 * la mappa dei punti, il servizio meteo
	 * e il medietor, che collega la classe con
	 * gli altri handler
	 */
    private Map<String, List<Point>> points;
    private WeatherService weather;
    private MuniciPathMediator mediator;
    private IdentifierManager idManager;
    
    /**
     * beans iniettati per la persistenza
     */
    @Autowired
	private PostRepository postRepository;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
    private ContributeHandler contributes;
    
	/**
	 * costruttore della classe. inizializza la mappa
	 * per le ricerche rapide e il servizio per le
	 * previsioni meteo.
	 */
    public PointHandler() {
    	points = new HashMap<>();
        weather = new WeatherForecastProxy();
        idManager = new IdentifierManager();
    }
    
    /**
     * Inserisce il mediator nella classe
     * @param mediator mediator da inserire
     */
    public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }
    
    /**
     * caricamento di dati nella mappa alla creazione
     */
    @PostConstruct
    public void loadData() {
    	this.points = StreamSupport.stream(pointRepository.findAll().spliterator(), false)
    			.map(p -> {
    				p.setPosts(StreamSupport.stream(postRepository.findAll().spliterator(), false)
    					.filter(po -> po.getPos().equals(p.getPos()))
    					.toList()); return p; })
    			.collect(Collectors.groupingBy(Point::getCityId));
    }
    
    /**
     * Metodo che crea una citta'
     * viene chiamato dal moderatore, 
     * in seguito alla logica di CityHandler.
     * @param cityId id della città da inserire
     * @return true se la citta' non esisteva, false altrimenti
     */
    public boolean addNewCity(String cityId) {
    	if(this.points.containsKey(cityId))
    		return false;
    	this.points.put(cityId, new ArrayList<Point>());
    	return true;
    }
    
    /**
     * Metodo per creare un nuovo post
     * 
     * @param title titolo del post
     * @param type tipo di post
     * @param text testo del post
     * @param author autore del post
     * @param pos posizione del post
     * @param cityId citta' del post
     * @param data contenuti multimediali del post
     * @param start momento di inizio del post
     * @param end momento di fine del post
     * @param persistence se il post persiste dopo la sua fine
     * @return true se la creazione e' andata a buon fine, false altrimenti
     */
    public boolean createPost(String title, PostType type, String text, String author, Position pos,
            String cityId, List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	if(!(this.mediator.isAuthorizedToPost(cityId, author) && checkTiming(type, start, end, persistence)))
    		return false;
    	boolean published = this.mediator.canPublish(cityId, author);
        Post post = new Post(title, type, text, author, pos, cityId, null, data, published, start, end, persistence);
        Point point = getPoint(pos, cityId);
        post.setPostId(point.getNewPostId());
        if(!checkContest(post.getPostId(), type, published))
        	return false;
    	this.pointRepository.save(point);
        this.postRepository.save(post);
        point.getPosts().add(post);
        if(!published)
        	this.mediator.addPending(post.getPostId());
        return true;
    }
    
    /**
     * Metodo per la modifica di un post da parte dell'autore
     * @param postId id del post
     * @param title nuovo titolo del post
     * @param type nuovo tipo del post
     * @param text nuovo testo del post
     * @param author autore del post
     * @param cityId citta' del post
     * @param data nuovi contenuti del post
     * @param start nuovo momento di inizio del post
     * @param end nuovo momento di fine del post
     * @param persistence modifica persistenza
     * @return true se la modifica e' andata a buon fine, false altrimenti
     */
	public boolean editPost(String postId, String title, PostType type, String text,
    		String author, String cityId, List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	Post post = this.getPost(postId);
    	if(post == null|| (!post.getAuthor().equals(author)) || (!checkTiming(type, start, end, persistence)) || isPrime(post))
    		return false;
    	boolean published = this.mediator.canPublish(cityId, author);
        if(!checkContest(post.getPostId(), post.getType(), type, published))
        	return false;
    	if(published) {
    		post.updateInfo(title, type, text, data, start, end, persistence);
            this.postRepository.save(post);
    	}
    	else this.mediator.addPostPending(postId, title, type, text, data, start, end, persistence);
        return true;
    }
	
	/**
	 * Metodo per la modifica di un post da parte del comune
	 * @param postId id del post da modificare
	 * @param title nuovo titolo del post
	 * @param type nuovo tipo del post
	 * @param text nuovo testo del post
	 * @param data nuovi contenuti del post
	 * @param start nuovo inizio del post
	 * @param end nuova fine del post
	 * @param persistence modifica persistenza
	 * @return true se la modifica e' andata a buon fine, false altrimenti
	 */
	public boolean editPost(String postId, String title, PostType type, String text,
    		 List<String> data, LocalDateTime start, LocalDateTime end, boolean persistence) {
    	Post post = this.getPost(postId);
    	if(post == null || (!checkTiming(type, start, end, persistence)) || isPrime(post))
    		return false;
        if(!checkContest(post.getPostId(), post.getType(), type, true))
        	return false;
    	post.updateInfo(title, type, text, data, start, end, persistence);
        this.postRepository.save(post);
        return true;
    }
    
	/**
	 * metodo per la modifica di un post da parte di una richiesta
	 * prima in pending e ora accettata. viene usato dal mediator
	 * chiamato dal PendingHandler
	 * @param request richiesta di modifica accettata
	 */
    public void editPost(PendingRequest request) {
    	Post post = getPost(request.getId());
    	post.updateInfo(request);
        this.postRepository.save(post);
    }
    
    /**
     * metodo privato per controllare se un post di tipo contest e'
     * stato formulato nel modo corretto
     * @param id id del post
     * @param type tipo del post
     * @param published se e' pubblicato
     * @return true se e' formulato nel modo corretto, false altrimenti
     */
	private boolean checkContest(String id, PostType type, boolean published) {
		if(type == PostType.CONTEST) {
			if(!published)
				return false;
			this.contributes.addContest(id);
		}
		return true;
	}
	
	/**
	 * metodo privato per controllare se una modifica di un post
	 * lo porti eventalmente a cambiare le sue dinamiche come contest
	 * @param id id del post
	 * @param type vecchio tipo del post
	 * @param newType nuovo tipo del post
	 * @param published se e' pubblicato
	 * @return true se la modifica e' corretta. false altrimenti
	 */
	private boolean checkContest(String id, PostType type, PostType newType, boolean published) {
		if(type == PostType.CONTEST || newType == PostType.CONTEST ) {
			if(!published)
				return false;
			if(type != PostType.CONTEST || newType == PostType.CONTEST )
				this.contributes.addContest(id);
			else if (type == PostType.CONTEST || newType != PostType.CONTEST)
				this.contributes.removeContest(id);
		}
		return true;
	}
	
	/**
	 * metodo privato che controlla se una combinazione di tipo, momenti e persistenza
	 * e' corretta in funzione del tipo di post
	 * @param type tipo di post
	 * @param start momento di inizio
	 * @param end momento di fine
	 * @param persistence se e' persistente
	 * @return true se e' corretto, false altrimenti
	 */
	private boolean checkTiming(PostType type, LocalDateTime start, LocalDateTime end, boolean persistence) {
		if(isNotTemp(type) && persistence && start == null && end == null)
			return true;
		if(type == PostType.EVENT && start != null && end != null && start.isBefore(end))
			return true;
		if(type == PostType.CONTEST && start == null && end != null && persistence)
			return true;
		return false;
	}
    
	/**
	 * metodo privato che permette di verificare se un dato tipo non
	 * e' soggetto al tempo
	 * @param type tipo da controllare
	 * @return true se e' di un tipo non temporale, false altrimenti
	 */
    private boolean isNotTemp(PostType type) {
    	return type != PostType.EVENT && type != PostType.CONTEST;
    }
    
    /**
     * meotod privato per ottenere un punto partendo da una posizione
     * e l'id di un comune
     * @param pos posizione del punto
     * @param cityId id del comune
     * @return punto trovato, o un nuovo punto vuoto
     */
    private Point getPoint(Position pos, String cityId) {
    	return this.points.get(cityId).stream().filter(p -> p.getPos().equals(pos))
    			.findFirst().orElse(makeNewPoint(pos, cityId));
    }
    
    /**
     * crea un nuovo punto partendo da una posizione e un id di un comune
     * @param pos posizione
     * @param cityId id del comune
     * @return nuovo punto creato
     */
    private Point makeNewPoint(Position pos, String cityId) {
    	Point point = new Point(cityId+"."+pos, pos, cityId);
    	this.points.get(cityId).add(point);
    	this.pointRepository.save(point);
    	return point;
    }
    
    /**
     * metodo che ritorna tutti i punti di una citta'
     * funziona in base all'username perche' nasconde
     * quelli che non dovrebbero essere visti dall'utente in questione
     * @param cityId id del comune
     * @param username nome utente
     * @return lista di punti
     */
    public List<Point> getPoints (String cityId, String username) {
          return this.points.get(cityId).stream()
        		  .filter( p -> p.getPosts().stream().anyMatch(po -> toShow(po, username))).toList();
    }
    
    /**
     * aggiorna le informazioni di un dato post
     * con il meteo attuale e i gruppi di cui fa parte
     * l'username serve per controllare la visibilita' dei gruppi
     * @param post post da aggiornare
     * @param username username per la visualizzazione
     * @return post aggiornato
     */
    private Post updatePost(Post post, String username) {
    	post.setMeteo(this.weather.getWeather(post.getPos(), post.getMeteoDate()));
    	post.setGroups(this.mediator.viewGroupFrom(post, username));
    	return post;
    }
    
    /**
     * elimina tutti i punti e i relativi posts
     * e gruppi di una citta'
     * viene chiamato dal mediator, chiamato a sua volta dal
     * CityHandler
     * @param cityId id del comune da cancellare
     */
    public void deleteCityPoints (String cityId) {
        List<Point> ps = this.points.get(cityId);
        ps.forEach(p ->  this.postRepository.deleteAll(p.getPosts()));
        this.pointRepository.deleteAll(ps);
        this.points.remove(cityId);
        this.mediator.removeAllCityGroups(cityId);
    }
    
    /**
     * ritorna la lista dei post presenti attualmente
     * in un dato punto. l'username serve per verificare se
     * un dato utente ha la possibilita' di vedere un post
     * o meno.
     * @param pointId id del punto
     * @param username nome utente del visualizzatore
     * @return lista di post da vedere
     */
    public List<Post> viewPosts (String pointId, String username) {
        return searchPoint(pointId).getPosts().stream()
        		.filter(p -> toShow(p, username)).toList();
    }
    
    /**
     * metodo privato che controlla se un contenuto debba essere visto
     * o meno da un dato utente
     * @param post post da verificare
     * @param username nome utente
     * @return true se il post e' visibilie, false altrimenti
     */
    private boolean toShow(Post post, String username) {
    	if(post.getAuthor().equals(username))
    		return true;
    	if(!post.isPublished())
    		return false;
    	return post.getType() != PostType.EVENT || post.getEndTime().isAfter((LocalDateTime.now()));
    }
    
    /**
     * metodo per ottenere un post. il metodo e' destinato
     * ad essere usato dal controller
     * @param postId id del post
     * @param username nome utente
     * @return post ricercato
     */
    public Post getPost(String postId, String username) {
    	Point point = searchPointFromPost(postId);
    	Post post = point.getPosts().stream()
    			.filter(p -> p.getPostId().equals(postId))
    			.findFirst().orElse(null);
    	return post == null ? null : updatePost(post, username);
    }
    
    /**
     * metodo per ottenere un post. il metodo e' destinato
     * ad essere usato dalla logica interna del model
     * @param postId id del post
     * @return post ricercato
     */
    public Post getPost(String postId) {
    	Point point = searchPointFromPost(postId);
    	Post post = point.getPosts().stream()
    			.filter(p -> p.getPostId().equals(postId))
    			.findFirst().orElse(null);
    	return post == null ? null : updatePost(post, null);
    }
    
    /**
     * metodo privato per ottenere il punto che contiene
     * un post passando solo il suo id.
     * @param postId id del post
     * @return punto che lo contiene
     */
    private Point searchPointFromPost(String postId) {
    	return searchPoint(idManager.FromPostToPoint(postId));
    }
    
    /**
     * metood privato per cercare un punto dato il pointId
     * @param pointId id del punto
     * @return punto ricercato
     */
    private Point searchPoint (String pointId) {
        return this.points.get(idManager.getCityId(pointId))
        		.stream().filter(p -> p.getPointId().equals(pointId))
        		.findFirst().orElse(new Point());
    }
    
    /**
     * metodo privato per cercare un punto dato un id di comune e una posizone
     * @param cityId id del comune
     * @param positon posizione
     * @return point ricercato
     */
    private Point searchPoint (String cityId,Position positon) {
        return this.points.get(cityId).stream()
        		.filter(p -> p.getPos().equals(positon))
        		.findFirst().orElse(new Point());
    }
    
    /**
     * metodo per elimanare un post. e' destinato 
     * all'uso dell'autore
     * @param postId id del post
     * @param author autore del psot
     * @return true se e' stato eliminato, false altimenti
     */
    public boolean deletePost (String postId, String author) {
    	Post post = getPost(postId);
    	if(!(post != null && post.getAuthor().equals(author)))
    		return false;
    	return deletePost(post);
    }
    
    /**
     * metodo per eliminare un post. e' destinato
     * all'uso del comune
     * @param postId postId da eliminare
     * @return true se e' stato eliminato, false altrimenti
     */
    public boolean deletePost (String postId) {
    	Post post = getPost(postId);
    	if(post == null)
    		return false;
    	return deletePost(post);
    }
    
    /**
     * metodo privato con la logica dell'eliminazione
     * @param post post da eliminare
     * @return true se il post e' stato eliminato. false altrimenti
     */
    private boolean deletePost(Post post) {
    	if(isPrime(post))
    		return false;
    	if(post.getType() == PostType.CONTEST)
    		this.contributes.removeContest(post.getPostId());
    	Point point = searchPointFromPost(post.getPostId());
    	point.getPosts().remove(post);
    	this.postRepository.delete(post);
    	if(point.getPosts().isEmpty()) {
    		this.points.get(point.getCityId()).remove(point);
        	this.pointRepository.delete(point);
    	}
    	this.mediator.removeFromAllGroups(post.getPostId());
    	return true;
    }
    
    /**
     * metodo privato per controllare se un post e' il primo del comune
     * non puo' essere modificato e puo' essere eliminato solo 
     * cancellando l'intera citta'.
     * @param post post da controllare
     * @return true se il post e' prime, false altrimenti
     */
	private boolean isPrime(Post post) {
		Position posCity = this.mediator.getCity(post.getCityID()).getPos();
		Point point = searchPoint(post.getCityID(), posCity);
		return post.getPostId().equals(point.getPointId()+".0");
	}
	
	/**
	 * data una lista di id di post, ritorna tutti i post a
	 * cui corripondono gli id
	 * @param postIds id dei post
	 * @return lista di post ricercati
	 */
	public List<Post> getPosts(List<String> postIds){
		String cityId = idManager.getCityId(postIds.get(0));
		return this.points.get(cityId).stream()
				.map(p -> p.getPosts()).flatMap(List::stream).
				filter(post -> postIds.contains(post.getPostId()))
			    .collect(Collectors.toList());
	}
	
	/**
	 * data una lista di postIds, ritorna una lista di tutti
	 * i post corrispondenti
	 * @param postIds ids da ricercare
	 * @return una lista contenente tutti i post se tutti gli id ricercati
	 * esistono, altrimenti null.
	 */
	public List<Post> getPostsIfAllExists(List<String> postIds) {
		List<Post> posts = getPosts(postIds);
		return posts.size() == postIds.size() ? posts : null;
	}
	
	/**
	 * metodo per approvare un post.
	 * viene chiamato dal moderator, che a sua volta
	 * viene chiamato dal PendingHandler
	 * @param postId id del post da approvare
	 * @return se il post e' stato pubblicato true, false altrimenti.
	 */
	public boolean approvePost(String postId) {
		Post post = getPost(postId);
		if(post == null || post.isPublished())
			return false;
		post.setPublished(true);
		return true;
	}
	
	/**
	 * metodo per ottenere tutti i contributi di un dato post
	 * @param username autore del post
	 * @param postId id del post
	 * @return contributi del post se l'autore e' corretto, null altrimenti
	 */
	public List<Contribute> getContributes(String username, String postId){
		Post post = getPost(postId);
		if(post == null || (!post.getAuthor().equals(username)))
			return null;
		return this.contributes.getContributes(postId);
	}
	
	/**
	 * metodo per ottenere tutti i contributi di un dato post
	 * @param postId id del post
	 * @return contributi del post se esso esiste, null altrimenti
	 */
	public List<Contribute> getContributes(String postId){
		if(getPost(postId) == null)
			return null;
		return this.contributes.getContributes(postId);
	}
	
	/**
	 * metodo per aggiungere contenuti ad un contest
	 * @param contestAuthor autore del contest
	 * @param contestId id del contest
	 * @param content contenuti da aggiungere
	 * @return true se i contenuti sono stati aggiunti, false altrimenti
	 */
	public boolean addContentToContest(String contestAuthor, String contestId, List<String> content) {
		if(!this.mediator.usernameExists(contestAuthor))
			return false;
		Post contest = getPost(contestId);
		if(contest == null || contest.getEndTime().isBefore(LocalDateTime.now()))
			return false;
		return this.contributes.addContributeToContest(contestAuthor, contestId, content);
	}
	
	/**
	 * metodo per dichiarare il vincitore di un contest
	 * @param author autore del contest
	 * @param contestId id del contest
	 * @param winnerId id del vincitore
	 * @return true se la dichiarazione e' andata a buon fine, false altrimenti
	 */
	public boolean declareWinner(String author, String contestId, String winnerId) {
		Post post = getPost(contestId);
		if(post == null || post.getType() != PostType.CONTEST 
				|| (!post.getAuthor().equals(author)) || post.getEndTime().isAfter(LocalDateTime.now()))
			return false;
		List<String> winnercontent = this.contributes.declareWinner(contestId, winnerId);
		if(winnercontent == null)
			return false;
		PendingRequest edit = new PendingRequest(post.getPostId(), post.getTitle(), "", 
				true, PostType.SOCIAL, winnercontent, null, null);
		editPost(edit);
		post.setAuthor(winnerId);
		this.postRepository.save(post);
		return true;
	}
	
	/**
	 * metodo che si assicura che tutti gli eventi finiti
	 * che non hanno persistenza vengano rimossi dal sistema.
	 */
	public void checkEndingPosts() {
		LocalDateTime date = LocalDateTime.now();
		List<Post> toDelete = new ArrayList<>();
		StreamSupport.stream(postRepository.findAll().spliterator(), true)
				.filter(po -> ! po.isPersistence())
				.filter(po -> po.getType() == PostType.EVENT)
				.forEach(po -> {
					if(po.getEndTime().isBefore(date))
						toDelete.add(po); });
		toDelete.forEach(po -> deletePost(po));
	}
	
	/**
	 * Ottieni autore del post. viene usato dal moderator per
	 * sapere a chi mandare l'esito di un pending.
	 * @param pendingId id del contenuto.
	 * @return autore del contenuto
	 */
	public String getAuthor(String pendingId) {
		Post post = this.getPost(pendingId);
		return post == null ? null : post.getAuthor();
	}
    
}
