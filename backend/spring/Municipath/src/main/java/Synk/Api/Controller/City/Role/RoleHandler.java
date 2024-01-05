package Synk.Api.Controller.City.Role;


import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import Synk.Api.Model.City.Role.Licence;
import Synk.Api.Model.City.Role.LicenceRepository;
import Synk.Api.Model.City.Role.RequestRepository;
import Synk.Api.Model.City.Role.Role;
import Synk.Api.Model.City.Role.RoleRequest;


@Service
public class RoleHandler {

	/**
	 * Oggetto requestRepository, utilizzato per gestire la persistenza delle richieste
	 */
	private RequestRepository requestRepository;

	/**
	 * Oggetto licenceRepository, utilizzato per gestire la persistenza delle autorizzazioni
	 */
	private LicenceRepository licenceRepository;
	
	/**
	 * Costruttore per l'oggetto RoleHandler
	 * @param requestRepository, la repository delle richieste
	 * @param licenceRepository, la repository delle autorizzazioni
	 */
	public RoleHandler(RequestRepository requestRepository, LicenceRepository licenceRepository) {
		this.licenceRepository = licenceRepository;
		this.requestRepository = requestRepository;
	}

	/**
	 * Metodo per ottenere le licenze di tutti gli utenti di una città
	 * @param cityId, l'id della città di cui si vogliono ottenere le licenze
	 * @return una lista delle licenze degli utenti della città
	 */
	public List<Licence> getAuthorizations(String cityId) {
		return StreamSupport.stream(licenceRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}

	/**
	 * Metodo per ottenere la licenza di un utente di una città
	 * @param username, l'username dell'utente di cui si vuole ottenere la licenza
	 * @param cityId, l'id della città in cui si vuole ottenere la licenza
	 * @return la licenza dell'utente nella città
	 */
	public Licence getAuthorization(String username, String cityId) {
		return this.licenceRepository.findById(cityId+"."+username)
				.orElse(new Licence(cityId, username, Role.TOURIST));
	}

	/**
	 * Metodo per settare il ruolo di un utente in una città
	 * @param username, l'username dell'utente di cui si vuole settare il ruolo
	 * @param cityId, l'id della città in cui si vuole settare il ruolo
	 * @param role, il ruolo da settare
	 * @return true se il ruolo è stato settato, false altrimenti
	 */
	public boolean setRole(String username, String cityId, Role role) {
		if(role == Role.CURATOR || role == Role.MODERATOR)
			return false;
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		if(role == Role.TOURIST)
			this.licenceRepository.delete(licence);
		else {
			licence.setRole(role);
			this.licenceRepository.save(licence);
		}
		return true;
	}

	/**
	 * Metodo per aggiungere un moderatore a una città
	 * @param username, l'username dell'utente da settare come moderatore
	 * @param cityId, l'id della città in cui si vuole settare il moderatore
	 * @return true se il moderatore è stato aggiunto, false altrimenti
	 */
	public boolean addModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		licence.setRole(Role.MODERATOR);
		this.licenceRepository.save(licence);
		return true;
	}

	/**
	 * Metodo per rimuovere un moderatore da una città
	 * @param username, l'username dell'utente da rimuovere come moderatore
	 * @param cityId, l'id della città in cui si vuole rimuovere il moderatore
	 * @return true se il moderatore è stato rimosso, false altrimenti
	 */
	public boolean removeModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() != Role.MODERATOR)
			return false;
		this.licenceRepository.delete(licence);
		return true;
	}

	/**
	 * Metodo per assegnare la città al suo corrispettivo curatore.
	 * Metodo invocato da CityHandler, nel metodo createCity
	 * @param cityId l'id della città in cui si vuole aggiungere il curatore
	 * @param curator l'username del curatore
	 */
	public void addCity(String cityId, String curator) {
		this.licenceRepository.save(new Licence(cityId, curator, Role.CURATOR));
		this.licenceRepository.save(new Licence(cityId, "unregistered_tourist", Role.LIMITED));
	}

	/**
	 * Metodo per rimuovere tutte le licenze corrispettive a una città dal database
	 * @param cityId, l'id della città da rimuovere
	 */
	public void removeCity(String cityId) {
		this.licenceRepository.deleteAll(getAuthorizations(cityId));
	}

	/**
	 * Metodo per aggiungere una richiesta di promozione di ruolo
	 * @param cityId, l'id della città in cui si vuole fare la richiesta di promozione
	 * @param username, l'username dell'utente che fa la richiesta
	 * @return true se la richiesta è stata aggiunta, false altrimenti
	 */
	public boolean addRequest(String cityId, String username) {
		RoleRequest request = new RoleRequest(cityId, username);
		if (requestRepository.existsById(request.getRequestId()))
			return false;
		requestRepository.save(request);
		return true;

	}

	/**
	 * Metodo per ottenere tutte le richieste di promozione di ruolo di una determinata città
	 * @param cityId, l'id della città di cui si vogliono ottenere le richieste
	 * @return una lista delle richieste di promozione di ruolo della città
	 */
	public List<RoleRequest> getRequests(String cityId) {
		return StreamSupport.stream(requestRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}

	/**
	 * Metodo per giudicare una richiesta di promozione di ruolo
	 * @param requestId, l'id della richiesta da giudicare
	 * @param outcome, l'esito della richiesta
	 * @return true se la richiesta è stata giudicata, false altrimenti
	 */
	public boolean judge(String requestId, boolean outcome) {
		Optional<RoleRequest> oRequest = getRequest(requestId);
		if (oRequest.isEmpty())
			return false;
		if (outcome) {
			String user = oRequest.get().getUsername();
			String city = oRequest.get().getCityId();
			Role role = getAuthorization(user, city).getRole();
			role = switch (role) {
				case CONTR_NOT_AUTH -> Role.CONTR_AUTH;
				case TOURIST -> Role.CONTR_NOT_AUTH;
				default -> role;
			};
			setRole(user, city, role);
		}
		removeRequest(requestId);
		return true;
	}

	/**
	 * Metodo per ottenere una determinata richiesta di promozione di ruolo
	 * @param requestId, l'id della richiesta da ottenere
	 * @return la richiesta se esiste, null altrimenti
	 */
	private Optional<RoleRequest> getRequest(String requestId) {
		return requestRepository.findById(requestId);
	}

	/**
	 * Metodo per rimuovere una richiesta dal database
	 * @param requestId, l'id della richiesta da rimuovere
	 */
	private void removeRequest(String requestId) {
		requestRepository.deleteById(requestId);
	}
	
}

