package Synk.Api.Model.City;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import Synk.Api.Model.Post.Position;
import Synk.Api.Model.MuniciPathMediator;
import Synk.Api.Model.City.Role.Licence;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleHandler;
import Synk.Api.Model.City.Role.RoleRequest;


@Repository
public class CityHandler {
	/**
	 * Oggetto mediator, utilizzato per far comunicare i vari handler fra di loro
	 */
    private MuniciPathMediator mediator;

	/**
	 * Oggetto roleHandler, utilizzato per gestire i ruoli
	 */
    @Autowired
    private  RoleHandler roleHandler;

	/**
	 * Oggetto cityRepository, utilizzato per gestire la persistenza dei dati
	 */
    @Autowired
	private CityRepository cityRepository;

	/**
	 * Metodo setter per l'oggetto mediator
	 * @param mediator, il mediator da settare
	 */
	public void setMediator(MuniciPathMediator mediator) {
        this.mediator = mediator;
    }

	/**
	 * Metodo per creare una città
	 * @param cityName, il nome della città
	 * @param cap, il codice postale della città
	 * @param curator, il curatore della città
	 * @param pos, la posizione della città
	 * Alla città viene assegnato anche un Id univoco, creato facendo l'hash del nome della città e del cap
	 * @return true se la città è stata creata, false altrimenti
	 */
	public boolean createCity(String cityName, int cap, String curator, Position pos ) {
    	String id = "" + (cityName+cap).hashCode();
        City c1 = new City(id, cityName,pos, curator, cap);
        if (checkIfAlreadyExists(id))
        	return false;
        if(!this.mediator.matchCurator(curator, id)) 
        	return false;
        cityRepository.save(c1);
        this.roleHandler.addCity(id, curator);
        this.mediator.createPostForNewCity(id, cityName, curator, pos);
        return true;
    }

	/**
	 * Metodo per controllare se una città esiste già
	 * @param cityId, l'id della città
	 * @return true se la città esiste, false altrimenti
	 */
    private boolean checkIfAlreadyExists (String cityId) {
    	return this.cityRepository.existsById(cityId);
    }

	/**
	 * Metodo per ottenere una città
	 * @param cityId, l'id della città da ottenere
	 * @return la città se esiste, null altrimenti
	 */
	public City getCity(String cityId) {
    	return this.cityRepository.findById(cityId).orElse(null);
    }

	/**
	 * Metodo per ottenere tutte le città
	 * @return una lista contenente tutte le città
	 */
	public List<City> getCities(){
    	return this.getCities("");
    }

	/**
	 * Metodo per ottenere tutte le città che iniziano con una determinata stringa
	 * @param search, la stringa con cui iniziano le città da ottenere
	 * @return una lista contenente tutte le città che iniziano con la stringa passata come parametro
	 */
	public List<City> getCities(String search) {
    	return StreamSupport.stream(cityRepository.findAll().spliterator(), true)
    			.filter(c -> (c.getName()+c.getCap()).toLowerCase()
    					.startsWith(search.toLowerCase())).toList();
    }

	/**
	 * Metodo per aggiornare una città
	 * @param id, l'id della città da aggiornare
	 * @param cityName, il nome della città
	 * @param cap, il codice postale della città
	 * @param curator, il curatore della città
	 * @param pos, la posizione della città
	 * @return true se la città è stata aggiornata, false altrimenti
	 */
    public boolean updateCity(String id, String cityName, int cap, String curator, Position pos ){
    	City city = getCity(id);
    	if(city == null)
    		return false;
    	if(!city.getCurator().equals(curator))
    		if(!this.mediator.changeCurator(curator, id))
    			return false;
        city.setName(cityName);
        city.setCap(cap);
        city.setCurator(curator);
        city.setPos(pos);
        this.cityRepository.save(city);
		return true;
    }

	/**
	 * Metodo per eliminare una città
	 * @param cityId, l'id della città da eliminare
	 * @return true se la città è stata eliminata, false altrimenti
	 */
    public boolean deleteCity(String cityId) {
    	City city = getCity(cityId);
    	if(city == null)
    		return false;
    	this.mediator.deleteCity(cityId);
    	this.roleHandler.removeCity(cityId);
    	this.cityRepository.delete(city);
    	return true;
    }

	/**
	 * Metodo per controllare se un utente è autorizzato a postare in una determinata città
	 * @param cityId, l'id della città in cui si vuole postare
	 * @param username, l'username dell'utente che vuole postare
	 * @return true se l'utente è autorizzato a postare, false altrimenti
	 */
    public boolean isAuthorized(String cityId, String username) {
    	Role role = getAuthorization(username, cityId).getRole();
    	return role != Role.LIMITED && role != Role.TOURIST;
	}

	/**
	 * Metodo per controllare se un utente può pubblicare in una determinata città
	 * @param cityId, l'id della città in cui si vuole postare
	 * @param author, l'username dell'utente che vuole postare
	 * @return true se l'utente può pubblicare, false altrimenti
	 */
	public boolean canPublish(String cityId, String author) {
		Role role = getAuthorization(author, cityId).getRole();
		return role == Role.CURATOR || role == Role.CONTR_AUTH || role == Role.MODERATOR;
	}

	/**
	 * Metodo per ottenere la "licenza" di un utente in una determinata città
	 * @param username, l'username dell'utente di cui si vuole ottenere la licenza
	 * @param cityId, l'id della città in cui si vuole ottenere il ruolo
	 * @return la licenza se l'utente esiste, una nuova licenza con ruolo di tipo LIMITED altrimenti
	 */
	public Licence getAuthorization(String username, String cityId) {
		if (!this.mediator.usernameExists(username)) 
			return new Licence(cityId, username, Role.LIMITED);
		return this.roleHandler.getAuthorization(username, cityId);
	}

	/**
	 * Metodo per ottenere tutte le licenze di una determinata città
	 * @param cityId, l'id della città di cui si vogliono ottenere le licenze
	 * @return una lista contenente tutte le licenze della città
	 */
	public List<Licence> getAuthorizations(String cityId) {
		return this.roleHandler.getAuthorizations(cityId);
	}

	/**
	 * Metodo per settare il ruolo di un utente in una determinata città
	 * @param username, l'username dell'utente di cui si vuole settare il ruolo
	 * @param cityId, l'id della città in cui si vuole settare il ruolo
	 * @param role, il ruolo da settare
	 * @return true se il ruolo è stato settato, false altrimenti
	 */
	public boolean setRole(String username, String cityId, Role role) {
		if(!this.mediator.usernameExists(username))
			return false;
		if(getCity(cityId) == null)
			return false;
		return this.roleHandler.setRole(username, cityId, role);
	}

	/**
	 * Metodo per aggiungere un moderatore a una determinata città
	 * @param username, l'username dell'utente da settare come moderatore
	 * @param cityId, l'id della città in cui si vuole settare il moderatore
	 * @return true se il moderatore è stato aggiunto, false altrimenti
	 */
	public boolean addModerator(String username, String cityId) {
		if(!this.mediator.usernameExists(username))
			return false;
		if(getCity(cityId) == null)
			return false;
		return this.roleHandler.addModerator(username, cityId);
	}

	/**
	 * Metodo per rimuovere un moderatore da una determinata città
	 * @param username, l'username dell'utente da rimuovere come moderatore
	 * @param cityId, l'id della città in cui si vuole rimuovere il moderatore
	 * @return true se il moderatore è stato rimosso, false altrimenti
	 */
	public boolean removeModerator(String username, String cityId) {
		if(!this.mediator.usernameExists(username))
			return false;
		if(getCity(cityId) == null)
			return false;
		return this.roleHandler.removeModerator(username, cityId);
	}

	/**
	 * Metodo per ottenere il ruolo di un utente in una determinata città
	 * @param username, l'username dell'utente di cui si vuole ottenere il ruolo
	 * @param cityId, l'id della città in cui si vuole ottenere il ruolo
	 * @return il ruolo se l'utente esiste, null altrimenti
	 */
	public Role getRole(String username, String cityId) {
		return this.getAuthorization(username, cityId).getRole();
	}

	/**
	 * Metodo per aggiungere una richiesta di promozione di ruolo
	 * @param username, l'username dell'utente che fa la richiesta
	 * @param cityId, l'id della città in cui si vuole fare la richiesta
	 * @return true se la richiesta è stata aggiunta, false altrimenti
	 */
	public boolean addRequest(String username, String cityId) {
		if(getCity(cityId) == null || (!this.mediator.usernameExists(username)))
			return false;
		return this.roleHandler.addRequest(cityId, username);
	}

	/**
	 * Metodo per ottenere tutte le richieste di promozione di ruolo di una determinata città
	 * @param cityId, l'id della città di cui si vogliono ottenere le richieste
	 * @return una lista contenente tutte le richieste di promozione di ruolo della città
	 */
	public List<RoleRequest> getRequests(String cityId) {
		if(!this.checkIfAlreadyExists(cityId))
			return null;
		return this.roleHandler.getRequests(cityId);
	}

	/**
	 * Metodo per giudicare una richiesta di promozione di ruolo
	 * @param requestId, l'id della richiesta da giudicare
	 * @param outcome, l'esito della richiesta
	 * @return true se la richiesta è stata giudicata, false altrimenti
	 */
	public boolean judge(String requestId, boolean outcome) {
		return this.roleHandler.judge(requestId, outcome);
	}

}