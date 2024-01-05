package Synk.Api.Controller.User;


import java.util.List;
import java.util.stream.StreamSupport;


import Synk.Api.Controller.MuniciPathMediator;
import Synk.Api.Model.User.NotificationHandler;
import Synk.Api.Model.User.User;
import Synk.Api.Model.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserHandler {

	/**
	 * Oggetto NotificatioHandler, utilizzato per gestire le notifiche
	 */
    private NotificationHandler notifications;

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

	/**
	 * Costruttore della classe UserHandler, per gli oggetti "notifications" e "encoder"
	 */
    public UserHandler() {
        this.notifications = new NotificationHandler();
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
		if(!this.usernameExists(username)) 
			return false;
		this.userRepository.deleteById(username);
		return true;
	}

	/**
	 * Metodo per controllare se ci sono match della password in chiaro con quella criptata, per un determinato utente
	 * @param username, lo username dell'utente
	 * @param password, la password dell'utente
	 * @return true se la password corrisponde, false altrimenti
	 */
	public boolean isThePassword(String username, String password) {
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
	public boolean changePassowrd(String username, String password) {
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
    	return this.userRepository.findById(username).orElse(null);
    }

	/**
	 * Metodo per ottenere un utente convalidato
	 * @param username, lo username dell'utente da ottenere
	 * @return l'utente se esiste ed è convalidato, null altrimenti
	 */
	public User getConvalidatedUser(String username) {
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
		return this.userRepository.existsById(username);
	}

	/**
	 * Metodo per inviare una notifica a un utente
	 * @param username, lo username dell'utente a cui inviare la notifica
	 * @param message, il messaggio da inviare nella notifica
	 */
	public void send(String username, String message) {
		if(usernameExists(username))
			this.notifications.send(username, message);
	}
    
}
