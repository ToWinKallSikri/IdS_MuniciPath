package Synk.Api.Model.Role;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoleHandler {
	private List<Licence> authorizations;
	private List<RoleRequest> requests;

	public RoleHandler() {
		authorizations = new ArrayList<Licence>();
		requests = new ArrayList<RoleRequest>();
	}

	public List<Licence> getAuthorizations(String cityId) {
		return authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(cityId)).toList();
	}

	public Licence getAuthorization(String username, String cityId) {
		return authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(cityId))
				.filter(l -> l.getUsername().equals(username))
				.findFirst().orElse(new Licence(username, cityId, Role.TOURIST));
	}
	

	public boolean setRole(String username, String cityId, Role role) {
		if(role == Role.CURATOR || role == Role.MODERATOR)
			return false;
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR || licence.getRole() == Role.CURATOR)
			return false;
		if(role == Role.TOURIST)
			this.authorizations.remove(licence);
		else {
			Role oldRole = licence.getRole();
			licence.setRole(role);
			if(oldRole.equals(Role.TOURIST))
				this.authorizations.add(licence);
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
		this.authorizations.remove(licence);
		return true;
	}
	
	public void addCity(String cityId, String curator) {
		this.authorizations.add(new Licence(cityId, curator, Role.CURATOR));
	}
	
	public void removeCity(String cityId) {
		this.authorizations.removeAll(getAuthorizations(cityId));
	}

	public boolean addRequest(String cityId, String username) {
		String requestId = cityId.hashCode() + "." + username.hashCode();
		RoleRequest request = new RoleRequest(cityId, username, requestId);
		if(requests.contains(request))
			return false;
		requests.add(request);
		return true;

	}

	public List<RoleRequest> getRequests(String cityId) {
		return this.requests.stream()
				.filter(r -> r.getCityId().equals(cityId)).toList();
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
		return requests.stream().parallel()
				.filter(r -> r.getRequestId().equals(requestId))
				.findFirst();
	}

	private void removeRequest(String requestId) {
		requests.stream().parallel()
				.filter(r -> r.getRequestId().equals(requestId))
				.findFirst().ifPresent(r -> requests.remove(r));
	}
	
}

