package Synk.Api.Model.City.Role;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class RoleHandler {
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private LicenceRepository licenceRepository;

	public List<Licence> getAuthorizations(String cityId) {
		return StreamSupport.stream(licenceRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}

	public Licence getAuthorization(String username, String cityId) {
		return this.licenceRepository.findById(cityId+"."+username)
				.orElse(new Licence(username, cityId, Role.TOURIST));
	}
	

	public boolean setRole(String username, String cityId, Role role) {
		if(role == Role.CURATOR || role == Role.MODERATOR)
			return false;
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		if(role == Role.TOURIST)
			this.licenceRepository.delete(licence);
		else {
			Role oldRole = licence.getRole();
			licence.setRole(role);
			if(oldRole.equals(Role.TOURIST))
				this.licenceRepository.save(licence);
		}
		return true;
	}
	
	public boolean addModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		licence.setRole(Role.MODERATOR);
		return true;
	}
	
	public boolean removeModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() != Role.MODERATOR)
			return false;
		this.licenceRepository.delete(licence);
		return true;
	}
	
	public void addCity(String cityId, String curator) {
		this.licenceRepository.save(new Licence(cityId, curator, Role.CURATOR));
	}
	
	public void removeCity(String cityId) {
		this.licenceRepository.deleteAll(getAuthorizations(cityId));
	}

	public boolean addRequest(String cityId, String username) {
		String requestId = cityId.hashCode() + "." + username.hashCode();
		RoleRequest request = new RoleRequest(cityId, username, requestId);
		if (requestRepository.existsById(requestId))
			return false;
		requestRepository.save(request);
		return true;

	}

	public List<RoleRequest> getRequests(String cityId) {
		return StreamSupport.stream(requestRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}
	


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

	private Optional<RoleRequest> getRequest(String requestId) {
		return StreamSupport.stream(requestRepository.findAll().spliterator(), true)
				.filter(r -> r.getRequestId().equals(requestId))
				.findFirst();
	}

	private void removeRequest(String requestId) {
		StreamSupport.stream(requestRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(requestId)).findFirst().
				ifPresent(r -> requestRepository.delete(r));
	}
	
}

