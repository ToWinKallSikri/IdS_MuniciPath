package Synk.Api.Model.City.Role;


import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;


@Repository
public class RoleHandler {
	
	private RequestRepository requestRepository;
	private LicenceRepository licenceRepository;
	

	public RoleHandler(RequestRepository requestRepository, LicenceRepository licenceRepository) {
		this.licenceRepository = licenceRepository;
		this.requestRepository = requestRepository;
	}

	public List<Licence> getAuthorizations(String cityId) {
		return StreamSupport.stream(licenceRepository.findAll().spliterator(), true)
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}

	public Licence getAuthorization(String username, String cityId) {
		return this.licenceRepository.findById(cityId+"."+username)
				.orElse(new Licence(cityId, username, Role.TOURIST));
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
			licence.setRole(role);
			this.licenceRepository.save(licence);
		}
		return true;
	}
	
	public boolean addModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		licence.setRole(Role.MODERATOR);
		this.licenceRepository.save(licence);
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
		RoleRequest request = new RoleRequest(cityId, username);
		if (requestRepository.existsById(request.getRequestId()))
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
		return requestRepository.findById(requestId);
	}

	private void removeRequest(String requestId) {
		requestRepository.deleteById(requestId);
	}
	
}

