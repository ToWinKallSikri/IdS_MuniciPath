package Synk.Api.Model.City;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RoleHandler {
	private List<Licence> authorizations;
	private List<RoleRequest> requests;

	public RoleHandler() {
		authorizations = new ArrayList<Licence>();
		requests = new ArrayList<RoleRequest>();
	}

	public Licence getAuthorization(String username, City city) {
		if(city == null)
			return null;
		if(city.getCurator().equals(username))
			return new Licence(username, city.getId(), Role.CURATOR);
		return authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(city.getId()) && l.getUsername().equals(username))
				.findFirst().orElse(new Licence(username, city.getId(), Role.TOURIST));

	}
	
	private Licence getAuthorization(String username, String city) {
		return authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(city) && l.getUsername().equals(username))
				.findFirst().orElse(new Licence(username, city, Role.TOURIST));
	}

	public List<Licence> getAuthorizations(City city) {
		List<Licence> list = authorizations.stream().parallel()
				.filter(l -> l.getCityId().equals(city.getId()))
				.collect(Collectors.toList());
		list.add(new Licence( city.getCurator(), city.getId(), Role.CURATOR ));
		return list;
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

	public boolean addRequest(String cityId, String username) {
		String requestId = cityId.hashCode() + "." + username.hashCode();
		RoleRequest request = new RoleRequest(cityId, username, requestId);
		if(requests.contains(request))
			return false;
		requests.add(request);
		return true;

	}

	public boolean setRole(String username, String cityId, Role role) {
		if(role == Role.CURATOR || role == Role.MODERATOR)
			return false;
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR)
			return false;
		if(role == Role.TOURIST)
			this.authorizations.remove(licence);
		else licence.setRole(role);
		return true;
	}
	
	public boolean addModerator(String username, String cityId) {
		Licence licence = getAuthorization(username, cityId);
		if(licence.getRole() == Role.MODERATOR)
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
	
}

