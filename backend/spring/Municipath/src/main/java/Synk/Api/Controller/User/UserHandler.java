package Synk.Api.Controller.User;


import java.util.List;
import java.util.stream.StreamSupport;

import Synk.Api.Controller.IdentifierManager;
import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Controller.User.Follow.FollowHandler;
import Synk.Api.Controller.User.Notification.NotificationHandler;
import Synk.Api.Model.MetaData;
import Synk.Api.Model.User.User;
import Synk.Api.Model.User.UserRepository;
import Synk.Api.Model.User.Notification.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserHandler implements UserProvider {

	/**
	 * Oggetto mediator, utilizzato per far comunicare i vari handler fra di loro
	 */
    private MuniciPathMediator mediator;

	/**
	 * Oggetto encoder, utilizzato per criptare le password
	 */
	private BCryptPasswordEncoder encoder;

	/**
	 * Oggetto userRepository, utilizzato per gestire la JPA
	 */
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FollowHandler followHandler;
	@Autowired
    private NotificationHandler notificationHandler;

	/**
	 * Costruttore della classe UserHandler, per gli oggetti "notifications" e "encoder"
	 */
    public UserHandler() {
        this.notificationHandler = new NotificationHandler();
        this.encoder = new BCryptPasswordEncoder();
    }

	/**
	 * Metodo setter per l'oggetto mediator
	 * @param mediator, il mediator da settare
	 */
	public void setMediator(MuniciPathMediator mediator) {
		this.mediator = mediator;
	}

	/**
	 * Metodo per creare un utente
	 * @param username, lo username dell'utente
	 * @param password, la password dell'utente
	 * @return true se l'utente è stato creato, false altrimenti
	 */
	public boolean addUser(String username, String password) {
		if(username == null || password == null)
			return false;
		if(usernameExists(username))
			return false;
		password = encoder.encode(password);
		this.userRepository.save(new User(username, password, false, false));
		return true;
	}

	/**
	 * Metodo per rimuovere un utente
	 * @param username, lo username dell'utente da rimuovere
	 * @return true se l'utente è stato rimosso, false altrimenti
	 */
	public boolean removeUser(String username) {
		if(username == null)
			return false;
		if(!this.usernameExists(username)) 
			return false;
		this.userRepository.deleteById(username);
		this.followHandler.deleteUser(username);
		this.notificationHandler.deleteUser(username);
		this.mediator.removeAllSaveContentOf(username);
		return true;
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
		User user = getConvalidatedUser(username);
		if(user == null) 
			return false;
		return this.encoder.matches(password, user.getPassword());
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
		User user = getConvalidatedUser(username);
		if(user == null) 
			return false;
		password = encoder.encode(password);
		user.setPassword(password);
		this.userRepository.save(user);
		return true;
	}

	/**
	 * Metodo per convalidare un utente dopo che ha fatto il Sign-In nella piattaforma
	 * @param username, lo username dell'utente da convalidare
	 * @return true se l'utente è stato convalidato, false altrimenti
	 */
	public boolean userValidation(String username) {
		if(username == null)
			return false;
		User user = getUser(username);
		if(user == null || user.isConvalidated()) 
			return false;
		user.setConvalidated(true);
		this.userRepository.save(user);
		return true;
	}

	/**
	 * Metodo per gestire l'autorizzazione da Gestore della Piattaforma
	 * @param username, lo username dell'utente da gestire
	 * @param auth, true se si vuole settare come Gestore, false altrimenti
	 * @return true se l'autorizzazione è stata gestita, false altrimenti
	 */
	public boolean manageManager(String username, boolean auth) {
		if(username == null)
			return false;
		User user = getConvalidatedUser(username);
		if(user == null || user.isManager() == auth) 
			return false;
		user.setManager(auth);
		this.userRepository.save(user);
		return true;
	}

	/**
	 * Metodo per ottenere un utente
	 * @param username, lo username dell'utente da ottenere
	 * @return l'utente se esiste, null altrimenti
	 */
	public User getUser(String username) {
		if(username == null)
			return null;
    	return this.userRepository.findById(username).orElse(null);
    }

	/**
	 * Metodo per ottenere un utente convalidato
	 * @param username, lo username dell'utente da ottenere
	 * @return l'utente se esiste ed è convalidato, null altrimenti
	 */
	public User getConvalidatedUser(String username) {
		if(username == null)
			return null;
    	User user = this.getUser(username);
    	return user != null && user.isConvalidated() ? user : null;
    }

	/**
	 * Metodo per ottenere tutti gli utenti non convalidati
	 * @return una lista di tutti gli utenti non convalidati
	 */
	public List<User> getNotConvalidatedUsers(){
    	return StreamSupport.stream(userRepository.findAll().spliterator(), true)
		.filter(u -> !u.isConvalidated()).toList();
    }

	/**
	 * Metodo per ottenere il curatore di una determinata città
	 * @param cityId, l'id della città di cui si vuole ottenere il curatore
	 * @return l'utente curatore se esiste, null altrimenti
	 */
	private User findCuratorOf(String cityId) {
		if(cityId == null)
			return null;
    	return StreamSupport.stream(userRepository.findAll().spliterator(), true)
				.filter(u -> cityId.equals(u.getCityId())).findFirst().orElse(null);
    }

	/**
	 * Metodo per associare un curatore a una determinata città
	 * @param curator, lo username dell'utente da associare alla città come curatore
	 * @param cityId, l'id della città
	 * @return true se il curatore è stato associato alla città, false altrimenti
	 */
    public boolean matchCurator(String curator, String cityId) {
		if(curator == null || cityId == null)
			return false;
    	User user = getConvalidatedUser(curator);
    	if(user == null || user.isCurator())
    		return false;
    	user.setCityId(cityId);
		this.userRepository.save(user);
    	return true;
    }

	/**
	 * Metodo per cambiare il curatore di una determinata città
	 * @param curator, lo username dell'utente da associare alla città come curatore
	 * @param cityId, l'id della città
	 * @return true se il curatore è stato cambiato, false altrimenti
	 */
    public boolean changeCurator(String curator, String cityId) {
		if(curator == null || cityId == null)
			return false;
    	User _old = findCuratorOf(cityId), _new = getConvalidatedUser(curator);
    	if(_old == null || _new == null || _new.isCurator())
    		return false;
    	_old.setCityId(null);
    	_new.setCityId(cityId);
		this.userRepository.save(_old);
		this.userRepository.save(_new);
		return true;
    }

	/**
	 * Metodo per dissociare un curatore da una determinata città
	 * @param cityId, l'id della città
	 */
	public void discreditCurator(String cityId) {
		if(cityId == null)
			return;
    	User curator = findCuratorOf(cityId);
    	if(curator != null) {
    		curator.setCityId(null);
    		this.userRepository.save(curator);
    	}

    }

	/**
	 * Metodo per verificare l'esistenza di un utente nella piattaforma+
	 * @param username, lo username dell'utente da verificare
	 * @return true se l'utente esiste, false altrimenti
	 */
	public boolean usernameExists(String username) {
		if(username == null)
			return false;
		return this.userRepository.existsById(username);
	}
	
	/**
	 * metodo per ottenere tutti i messaggi
	 * di un dato utente
	 * @param username nome utente
	 * @return messaggi dell'utente
	 */
	public List<Notification> getMyMessages(String username){
		if(username == null)
			return null;
		if (!(this.usernameExists(username)))
			return null;
		return this.notificationHandler.getMyMessages(username);
	}
	
	/**
	 * metodo per ottenere un messaggio
	 * di un utente
	 * @param username nome utente
	 * @param id id del messaggio
	 * @return messaggio ricarcato
	 */
	public Notification getMyMessage(String username, String id) {
		if(username == null || id == null)
			return null;
		if (!(this.usernameExists(username)))
			return null;
		return this.notificationHandler.getMyMessage(username, id);
	}

	/**
	 * metodo per notificare un utente in merito
	 * ad un contenuto. viene usato per notificare
	 * un esito pending o una modifica / eliminazione
	 * di contenuto da parte dello staff di un comune
	 * @param author autore della notifica
	 * @param message messaggio di notifica
	 * @param contentId id del contenuto
	 * @param reciever destinatario del messaggio
	 */
	public void notify(String author, String message, String contentId, String reciever) {
		if(author == null || message == null || contentId == null || reciever == null)
			return;
        if( (!this.usernameExists(reciever)) || (!this.mediator.contentExist(contentId)))
            return;
        notificationHandler.notify(author,message,contentId,reciever);
	}
	
	/**
	 * metodo per notificare un evento a tutti coloro
	 * che lo hanno salvato tra i contenuti salvati.
	 * @param author autore della notifica
	 * @param message messaggio di notifica
	 * @param contentId id del contenuto
	 */
	public void notifyEvent(String author, String message, String contentId) {
		if(author == null || message == null || contentId == null)
			return;
		if (!( this.usernameExists(author) 
				&& mediator.contentExist(contentId) 
				&& mediator.isAuthor(author, contentId) ))
            return;
        List<String> l1 = mediator.getPartecipants(contentId);
		if(mediator.getMetaData(contentId).isOfCity()) {
			String cityName = mediator.getNameOfCity(new IdentifierManager().getCityId(contentId));
	        notificationHandler.notifyEvent(cityName, message, contentId, l1);
		}
        notificationHandler.notifyEvent(author, message, contentId, l1);
    }

	/**
	 * Metodo per notificare la creazione di un contenuto
	 * a tutti gli utenti che seguono un certo
	 * comune o contributor
	 * @param data contenuto da notificare
	 */
	public void notifyCreation(MetaData data) {
		if(data == null)
			return;
		List<String> list;
		String author;
		if(data.isOfCity()) {
			list = this.followHandler.getAllCityFollowers(data.getCityId());
			author = this.mediator.getNameOfCity(data.getCityId());
		} else {
			list = this.followHandler.getAllContributorFollowers(data.getAuthor());
			author = data.getAuthor();
		}
        notificationHandler.notifyEvent(author, "Un nuovo contenuto è stato pubblicato!", data.getId(), list);
	}

	/**
	 * metodo per seguire un contributor
	 * @param username nome utente dell'utente
	 * @param contributor nome utente del contributo
	 * @return true se il following e' andato a buon fine
	 */
	public boolean followContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		if(!(this.usernameExists(username) && this.usernameExists(contributor)))
			return false;
		return this.followHandler.followContributor(username, contributor);
	}
	
	/**
	 * metodo per smettere di seguire un contributor
	 * @param username nome utente dell'utente
	 * @param contributor nome utente del contributor
	 * @return true se l'utente ha da ora smesso di seguire il contributor
	 */
	public boolean unfollowContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		return this.followHandler.unfollowContributor(username, contributor);
	}
	
	/**
	 * metodo per seguire un comune
	 * @param username nome utente
	 * @param cityId id del comune
	 * @return true se il following e' andato a buon fine
	 */
	public boolean followCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		if(!this.usernameExists(username))
			return false;
		if(!this.mediator.checkCity(cityId))
			return false;
		return this.followHandler.followCity(username, cityId);
	}
	
	/**
	 * metodo per smettere di seguire un comune
	 * @param username nome utente dell'utente
	 * @param cityId id del comune
	 * @return true se l'utente ha smesso di seguire il dato comune
	 */
	public boolean unfollowCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		return this.followHandler.unfollowCity(username, cityId);
	}
	
	/**
	 * metodo per seguire il proprietario di un contenuto
	 * @param username nome utente del contenuto
	 * @param contentId id del contenuto
	 * @return truese il following e' andato a buon fine
	 */
	public boolean follow(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		if(!this.usernameExists(username))
			return false;
		MetaData meta = this.mediator.getMetaData(contentId);
		if(meta == null) 
			return false;
		return this.followHandler.follow(username, meta);
	}
	
	/**
	 * metodo per smettere di seguire da un contenuto
	 * @param username nome utente dell'utente
	 * @param contentId id del contenuto
	 * @return true se l'utente ha smesso di seguire il
	 * proprietario di un contenuto.
	 */
	public boolean unfollow(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		MetaData meta = this.mediator.getMetaData(contentId);
		if(meta == null) 
			return false;
		return this.followHandler.unfollow(username, meta);
	}
	
	/**
	 * metodo che dice se si sta seguendo il proprietario
	 * di un dato contenuto
	 * @param username nome utente dell'utente
	 * @param contentId id del contenuto
	 * @return true se l'utente sta attualmente seguendo il contenuto
	 */
	public boolean alreadyFollowing(String username, String contentId) {
		if(username == null || contentId == null)
			return false;
		MetaData meta = this.mediator.getMetaData(contentId);
		if(meta == null) 
			return false;
		return this.followHandler.alreadyFollowing(username, meta);
	}
	
	/**
	 * metodo per verificare se 
	 * un utente segue attualmente
	 * un dato comune
	 * @param username nome utente
	 * @param cityId id del comune
	 * @return true se attualmente 
	 * lo segue, false altrimenti.
	 */
	public boolean alreadyFollowingCity(String username, String cityId) {
		if(username == null || cityId == null)
			return false;
		if(!this.usernameExists(username))
			return false;
		if(!this.mediator.checkCity(cityId))
			return false;
		return this.followHandler.alreadyFollowingCity(username, cityId);
	}
	
	/**
	 * metodo per verificare se un utente 
	 * segue attualmente un dato contributor
	 * @param username nome utente
	 * @param contributor nome utente del contributor
	 * @return true se attualmente lo segue
	 */
	public boolean alreadyFollowingContributor(String username, String contributor) {
		if(username == null || contributor == null)
			return false;
		if(!(this.usernameExists(username) && this.usernameExists(contributor)))
			return false;
		return this.followHandler.alreadyFollowingContributor(username, contributor);
	}
	
	/**
	 * metodo che restituisce tutti i comuni e
	 * contributor attualmente seguiti
	 * @param username nome utente
	 * @return lista dei seguiti
	 */
	public List<String> getAllFollowed(String username){
		if(username == null)
			return null;
		if(!this.usernameExists(username))
			return null;
		return this.followHandler.getAllFollowed(username);
	}
    
}
